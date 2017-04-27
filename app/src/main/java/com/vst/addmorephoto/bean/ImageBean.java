package com.vst.addmorephoto.bean;

import java.io.Serializable;

/**
 * Created by zwy on 2017/4/23.
 * email:16681805@qq.com
 * 图片对象
 */

public class ImageBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public String imageId;
    public String thumbnailPath;
    public String imagePath;
    public boolean isSelected = false;
}
