package com.vst.addmorephoto.ui;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.vst.addmorephoto.R;

/**
 * Created by zwy on 2017/4/22.
 * email:16681805@qq.com
 */

public class DisplayGvAdapter extends BaseAda<String> {
    public DisplayGvAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_show_image_gv, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        String imagePath = getItem(position);
        if ("".equals(imagePath)) {
            vh.iv_show_item.setImageResource(R.mipmap.icon_add_pic);
        } else {
            vh.iv_show_item.setImageURI(Uri.parse(imagePath));
        }
        return convertView;
    }

    public static class ViewHolder {
        public ImageView iv_show_item;

        public ViewHolder(View rootView) {
            this.iv_show_item = (ImageView) rootView.findViewById(R.id.iv_show_item);
        }

    }
}
