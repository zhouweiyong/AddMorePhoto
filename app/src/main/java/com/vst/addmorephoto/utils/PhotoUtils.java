package com.vst.addmorephoto.utils;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by zwy on 2017/4/22.
 * email:16681805@qq.com
 */

public class PhotoUtils {
    /**
     * 请求去拍照
     */
    public static Intent getTakeCameraIntent(Uri photoUri) {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        return openCameraIntent;
    }
}
