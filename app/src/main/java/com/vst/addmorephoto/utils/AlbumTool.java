package com.vst.addmorephoto.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.vst.addmorephoto.bean.AlbumBean;
import com.vst.addmorephoto.bean.ImageBean;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by zwy on 2017/4/23.
 * email:16681805@qq.com
 */

public class AlbumTool {
    private static AlbumTool mInstance;
    private Context mContext;
    private ContentResolver mCR;


    private HashMap<String, String> mThumbnailList = new HashMap<>();

    private HashMap<String, AlbumBean> mAlbumList = new HashMap<>();

    private AlbumTool() {
    }

    private AlbumTool(Context context) {
        this.mContext = context;
        this.mCR = context.getContentResolver();
    }

    public static AlbumTool getInstance(Context context) {
        if (mInstance == null) {
            synchronized (AlbumTool.class) {
                if (mInstance == null) {
                    mInstance = new AlbumTool(context);
                }
            }
        }
        return mInstance;
    }

    public List<AlbumBean> getAlbumList() {
        mAlbumList.clear();

        /**
         * 构造缩略图索引
         */
        String[] projection = {MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.IMAGE_ID, MediaStore.Images.Thumbnails.DATA};
        Cursor cursor = mCR.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);
        /**
         * 从数据库中得到缩略图
         */
        if (cursor != null && cursor.moveToFirst()) {
            int _id;
            int image_id;
            String image_path;
            int _idColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails._ID);
            int image_idColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
            int dataColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
            do {
                _id = cursor.getInt(_idColumn);
                image_id = cursor.getInt(image_idColumn);
                image_path = cursor.getString(dataColumn);
                mThumbnailList.put("" + image_id, image_path);
            } while (cursor.moveToNext());
        }
        // 构造相册索引
        String columns[] = new String[] { MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media.PICASA_ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.TITLE, MediaStore.Images.Media.SIZE, MediaStore.Images.Media.BUCKET_DISPLAY_NAME };
        // 得到一个游标
        Cursor cur = mCR.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, null);
        if (cur != null && cur.moveToLast()) {
            // 获取指定列的索引
            int photoIDIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int photoPathIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            int photoNameIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            int photoTitleIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
            int photoSizeIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
            int bucketDisplayNameIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int bucketIdIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);
            int picasaIdIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.PICASA_ID);
            // 获取图片总数
            int totalNum = cur.getCount();

            do {
                String _id = cur.getString(photoIDIndex);
                String name = cur.getString(photoNameIndex);
                String path = cur.getString(photoPathIndex);
                String title = cur.getString(photoTitleIndex);
                String size = cur.getString(photoSizeIndex);
                String albumName = cur.getString(bucketDisplayNameIndex);
                String bucketId = cur.getString(bucketIdIndex);
                String picasaId = cur.getString(picasaIdIndex);

                if (TextUtils.isEmpty(size) || Integer.valueOf(size) <= 0 || !new File(path).exists()) {// 排除无效图
                    continue;
                }

                AlbumBean albumBean = mAlbumList.get(bucketId);
                if (albumBean == null) {
                    albumBean = new AlbumBean();
                    mAlbumList.put(bucketId, albumBean);
                    albumBean.imageList = new ArrayList<ImageBean>();
                    albumBean.albumName = albumName;
                }
                albumBean.count++;
                ImageBean imageItem = new ImageBean();
                imageItem.imageId = _id;
                imageItem.imagePath = path;
                imageItem.thumbnailPath = mThumbnailList.get(_id);
                albumBean.imageList.add(imageItem);

            }
            while (cur.moveToPrevious());
        }

        Iterator<Map.Entry<String, AlbumBean>> itr = mAlbumList.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, AlbumBean> entry = (Map.Entry<String, AlbumBean>) itr.next();
            AlbumBean albumBean = entry.getValue();
            for (int i = 0; i < albumBean.imageList.size(); ++i) {
                ImageBean imageBean = albumBean.imageList.get(i);
            }
        }

        List<AlbumBean> tmpList = new ArrayList<AlbumBean>();
        Iterator<Map.Entry<String, AlbumBean>> itr2 = mAlbumList.entrySet().iterator();
        while (itr2.hasNext()) {
            Map.Entry<String, AlbumBean> entry = (Map.Entry<String, AlbumBean>) itr2.next();
            tmpList.add(entry.getValue());
        }
        return tmpList;
    }


}
