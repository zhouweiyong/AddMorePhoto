package com.vst.addmorephoto.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.vst.addmorephoto.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by zwy on 2017/4/22.
 * email:16681805@qq.com
 */

public class AddImageUtils {
    //显示的图片最大值
    public static final int MAX_IMAGE_COUNT = 10;
    //图片数量
    public static int imageCount = 0;
    public static ArrayList<String> bitmapPaths = new ArrayList<>();
    //压缩图片原路径和压缩路径的键值对
    public static HashMap<String, String> compressPathMap = new HashMap<>();

    private static Bitmap addBitmap;


    public static void addImage(String path) {
        bitmapPaths.add(path);
        refresh();
    }

    public static void addImages(Collection collection) {
        bitmapPaths.addAll(collection);
        refresh();
    }

    public static void removeImage(String path) {
        bitmapPaths.remove(path);
        refresh();
    }

    public static void clear() {
        bitmapPaths.clear();
        imageCount = 0;
    }

    public static void removeImage(int position) {
        bitmapPaths.remove(position);
        refresh();
    }

    private static void refresh() {
        imageCount = bitmapPaths.size();
        if (bitmapPaths.contains("")) {
            imageCount--;
        }
    }

    public static Bitmap getAddBitmap(Context context) {
        if (addBitmap == null) {
            addBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_add_pic);
        }
        return addBitmap;
    }
}
