package com.vst.addmorephoto.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.vst.addmorephoto.R;
import com.vst.addmorephoto.bean.ImageBean;
import com.vst.addmorephoto.utils.AddImageUtils;
import com.vst.addmorephoto.utils.ImageLoader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by zwy on 2017/4/24.
 * email:16681805@qq.com
 */

public class ImageSelectAdapter extends BaseAda<ImageBean> {

    private ImageView iv_image_select_item;
    private ImageView iv_status_image_select_item;
    private ImageLoader mImageLoader;
    private HashSet<String> mAddList = new HashSet<>();
    private HashSet<String> mRemoveList = new HashSet<>();
    private ArrayList<String> mContainList = new ArrayList<>();
    private int mTotalCount;

    ImageLoader.ImageCallback mImageCallback = new ImageLoader.ImageCallback() {
        @Override
        public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params) {
            if (imageView != null && bitmap != null) {
                String url = (String) params[0];
                if (url != null && url.equals((String) imageView.getTag())) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    };

    public ImageSelectAdapter(Context context) {
        super(context);
        mImageLoader = new ImageLoader();
        initImageSelect();
    }

    @Override
    public void setGroup(List<ImageBean> g) {
        super.setGroup(g);
        initImageSelect();
    }

    private void initImageSelect() {
        for (ImageBean imageBean : group) {
            if (AddImageUtils.bitmapPaths.contains(imageBean.imagePath) || AddImageUtils.bitmapPaths.contains(AddImageUtils.compressPathMap.get(imageBean.imagePath))) {
                imageBean.isSelected = true;
                mContainList.add(imageBean.imagePath);
            }

        }
        mTotalCount = AddImageUtils.imageCount;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_image_select_gv, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final ImageBean imageBean = getItem(position);
        mImageLoader.displayBmp(vh.iv_image_select_item, imageBean.thumbnailPath, imageBean.imagePath, mImageCallback);
        if (imageBean.isSelected) {
            vh.iv_status_image_select_item.setVisibility(View.VISIBLE);
        } else {
            vh.iv_status_image_select_item.setVisibility(View.INVISIBLE);
        }

        final ViewHolder finalVh = vh;
        vh.iv_image_select_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = imageBean.imagePath;
                if (mTotalCount < AddImageUtils.MAX_IMAGE_COUNT) {
                    imageBean.isSelected = !imageBean.isSelected;
                    if (imageBean.isSelected) {
                        finalVh.iv_status_image_select_item.setVisibility(View.VISIBLE);
                        mAddList.add(path);
                        mTotalCount++;
                    } else {
                        finalVh.iv_status_image_select_item.setVisibility(View.INVISIBLE);
                        mAddList.remove(path);
                        if (mContainList.contains(path)) {
                            mRemoveList.add(path);
                            mContainList.remove(path);
                        }
                        mTotalCount--;
                    }
                } else {
                    if (imageBean.isSelected) {
                        imageBean.isSelected = !imageBean.isSelected;
                        finalVh.iv_status_image_select_item.setVisibility(View.INVISIBLE);
                        mAddList.remove(path);
                        if (mContainList.contains(path)) {
                            mRemoveList.add(path);
                            mContainList.remove(path);
                        }
                        mTotalCount--;
                    } else {
                        Toast.makeText(mContext, "最多选择" + AddImageUtils.MAX_IMAGE_COUNT + "张图片", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return convertView;
    }

    public static class ViewHolder {
        public View rootView;
        public ImageView iv_image_select_item;
        public ImageView iv_status_image_select_item;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.iv_image_select_item = (ImageView) rootView.findViewById(R.id.iv_image_select_item);
            this.iv_status_image_select_item = (ImageView) rootView.findViewById(R.id.iv_status_image_select_item);
        }

    }

    public HashSet<String> getmAddList() {
        return mAddList;
    }

    public HashSet<String> getmRemoveList() {
        return mRemoveList;
    }
}
