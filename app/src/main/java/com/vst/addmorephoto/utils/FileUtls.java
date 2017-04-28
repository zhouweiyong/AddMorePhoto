package com.vst.addmorephoto.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zwy on 2017/4/22.
 * email:16681805@qq.com
 */

public class FileUtls {
    public static final int IMG_MAX_SIZE = 320 * 480 * 4;// 上传的图片最大尺寸

    /**
     * 判断是否有sd卡
     *
     * @return
     */
    public static boolean isSDCardExist() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 取得图片路径
     */
    public static File initNewPhotoFile(Context context) {
        File tempImageDir = new File(context.getExternalCacheDir(), "images");
        //这个路径 6.0用不了
//        File tempImageDir = new File(context.getCacheDir(), "images");
//        File tempImageDir = new File(Environment.getExternalStorageDirectory(), "images");
        Log.i("zwy", tempImageDir.getAbsolutePath());

        if (!tempImageDir.exists()) {
            tempImageDir.mkdirs();
            try {
                File nomedia = new File(tempImageDir, ".nomedia");
                nomedia.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File photoFile = new File(tempImageDir, getNewPhotoFileName());
        if (!photoFile.exists()) {
            try {
                photoFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return photoFile;
    }

    /**
     * 用当前时间给取得的图片命名
     */
    public static String getNewPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SSS");
        return dateFormat.format(date) + ".jpg";
    }


    /**
     * 保存拍照后的图片,用于上传
     */
    public static File saveNewPhotoFile(String tempFilePath,Context context) {
        BitmapFactory.Options opts = new BitmapFactory.Options();// 获取缩略图显示到屏幕上
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(tempFilePath, opts);

        int srcSize = opts.outHeight * opts.outWidth;
        if (srcSize > IMG_MAX_SIZE) {// 超过最大值
            opts.inSampleSize = computeSampleSize(opts, -1, IMG_MAX_SIZE);
        }
        else {
            opts.inSampleSize = 1;
        }
        opts.inJustDecodeBounds = false;
        // 拿到之前旋转的角度
        int degree = readPictureDegree(tempFilePath);

        if (opts.inSampleSize == 1 && degree == 0) {// 既没有旋转也没有超过大小，直接上传原图
            return new File(tempFilePath);
        }

        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);

        Bitmap bitmap = null;
        Bitmap resizedBitmap = null;
        File picFile = null;
        FileOutputStream fos = null;
        try {
            bitmap = BitmapFactory.decodeFile(tempFilePath, opts);

            // 创建新的图片
            resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            try {
                picFile = initNewPhotoFile(context);
                fos = new FileOutputStream(picFile);
                if (opts.inSampleSize > 1 && opts.inSampleSize <= 4) {// 测试结果
                    int rate = (int) (100 * (1 - (srcSize - IMG_MAX_SIZE) * 0.2 / IMG_MAX_SIZE));
                    rate = Math.max(rate, 50);
                    rate = Math.min(rate, 100);
                    Log.v("fan", srcSize + "压缩rate=" + rate);
                    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, rate, fos);
                }
                else {
                    int divide = opts.inSampleSize * IMG_MAX_SIZE;
                    int rate = (int) (100 * (1 - (srcSize - divide) * 0.015 / divide));
                    rate = Math.max(rate, 50);
                    rate = Math.min(rate, 100);
                    Log.v("fan", srcSize + "压缩rate=" + rate);
                    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, rate, fos);
                }
                fos.flush();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        finally {
            if (bitmap != null)
                bitmap.recycle();
            if (resizedBitmap != null)
                resizedBitmap.recycle();
            if (fos != null)
                try {
                    fos.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return picFile;
    }

    /**
     * 计算缩放比例
     */
    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        }
        else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        }
        else if (minSideLength == -1) {
            return lowerBound;
        }
        else {
            return upperBound;
        }
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path
     *            图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
}
