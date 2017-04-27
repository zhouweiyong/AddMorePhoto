package com.vst.addmorephoto.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.vst.addmorephoto.R;

/**
 * Created by zwy on 2017/4/22.
 * email:16681805@qq.com
 */

public class DialogUtils {
    public   interface  OnDialogItemClickListener{
        void onDialogItemClick(View v,int position);
    }

    // begin---底部弹出宽，类似苹果的//////////////////////////////////////////
    private static Dialog createShowAlert(final Context context, int layoutId) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(layoutId, null);
        final int cFullFillWidth = 10000;
        layout.setMinimumWidth(cFullFillWidth);

        // set a large value put it in bottom
        final Dialog dlg = new Dialog(context, R.style.MMTheme_DataSheet);
        Window w = dlg.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        final int cMakeBottom = -1000;
        lp.y = cMakeBottom;
        lp.gravity = Gravity.BOTTOM;// 改变显示位置
        dlg.onWindowAttributesChanged(lp);
        dlg.setCanceledOnTouchOutside(true);
        dlg.setContentView(layout);
        return dlg;
    }

    public static Dialog createTwoChoicAlertNoTitle(final Context context, String firstItem, String secondItem, final OnDialogItemClickListener onItemClickListener) {
        final Dialog dlg = createShowAlert(context, R.layout.dialog_two_item);
        dlg.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }

        });
        TextView item_first = (TextView) dlg.findViewById(R.id.item_first);
        item_first.setText(firstItem);
        item_first.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dlg.dismiss();
                if (onItemClickListener != null)
                    onItemClickListener.onDialogItemClick(v, 0);
            }
        });
        TextView item_second = (TextView) dlg.findViewById(R.id.item_second);
        item_second.setText(secondItem);
        item_second.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dlg.dismiss();
                if (onItemClickListener != null)
                    onItemClickListener.onDialogItemClick(v, 1);
            }
        });
        return dlg;
    }

}
