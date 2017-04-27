package com.vst.addmorephoto.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vst.addmorephoto.R;
import com.vst.addmorephoto.bean.AlbumBean;
import com.vst.addmorephoto.utils.ImageLoader;

/**
 * Created by zwy on 2017/4/23.
 * email:16681805@qq.com
 */

public class AlbumGvAdapter extends BaseAda<AlbumBean> {
    private ImageLoader mImageLoader;

    ImageLoader.ImageCallback mImageCallback = new ImageLoader.ImageCallback() {
        @Override
        public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params) {
            if (imageView != null && bitmap != null) {
                String url = (String) params[0];
                if (url !=null&&url.equals((String) imageView.getTag())) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    };

    public AlbumGvAdapter(Context context) {
        super(context);
        mImageLoader = new ImageLoader();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_album_gv, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        AlbumBean albumBean = getItem(position);
        vh.name_album_item.setText(albumBean.albumName);

        vh.num_album_item.setText(String.valueOf(albumBean.count));
        if (albumBean.imageList != null && albumBean.imageList.size() > 0) {
            String thumbnailPath = albumBean.imageList.get(0).thumbnailPath;
            String imagePath = albumBean.imageList.get(0).imagePath;
            mImageLoader.displayBmp(vh.iv_album_item, imagePath, imagePath, mImageCallback);
        }
        return convertView;
    }

    public static class ViewHolder {
        public ImageView iv_album_item;
        public TextView name_album_item;
        public TextView num_album_item;

        public ViewHolder(View rootView) {
            this.iv_album_item = (ImageView) rootView.findViewById(R.id.iv_album_item);
            this.name_album_item = (TextView) rootView.findViewById(R.id.name_album_item);
            this.num_album_item = (TextView) rootView.findViewById(R.id.num_album_item);
        }

    }
}
