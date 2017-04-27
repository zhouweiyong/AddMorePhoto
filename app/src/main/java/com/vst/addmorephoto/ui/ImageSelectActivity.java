package com.vst.addmorephoto.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.vst.addmorephoto.R;
import com.vst.addmorephoto.bean.ImageBean;
import com.vst.addmorephoto.utils.AddImageUtils;
import com.vst.addmorephoto.utils.FileUtls;

import java.io.File;
import java.util.HashSet;
import java.util.List;

/**
 * Created by zwy on 2017/4/24.
 * email:16681805@qq.com
 */

public class ImageSelectActivity extends Activity implements View.OnClickListener {

    private TextView btn_left;
    private GridView gv_image_select;
    private Button btn_image_select;
    public static final String INTENT_IMAGE_BEAN = "imageBeans";
    private List<ImageBean> mImageBeens;
    private ImageSelectAdapter mImageSelectAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);
        Intent intent = getIntent();
        mImageBeens = (List<ImageBean>) intent.getSerializableExtra(INTENT_IMAGE_BEAN);
        initView();
    }

    private void initView() {
        btn_left = (TextView) findViewById(R.id.btn_left);
        gv_image_select = (GridView) findViewById(R.id.gv_image_select);
        btn_image_select = (Button) findViewById(R.id.btn_image_select);

        btn_left.setOnClickListener(this);
        btn_image_select.setOnClickListener(this);

        mImageSelectAdapter = new ImageSelectAdapter(this);
        gv_image_select.setAdapter(mImageSelectAdapter);
        mImageSelectAdapter.setGroup(mImageBeens);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                onBackPressed();
                break;
            case R.id.btn_image_select:
                confirm();
                break;
        }
    }

    private void confirm() {
        HashSet<String> addList = mImageSelectAdapter.getmAddList();
        HashSet<String> removeList = mImageSelectAdapter.getmRemoveList();
        for (String path : removeList) {
            if (AddImageUtils.bitmapPaths.contains(path)) {
                AddImageUtils.removeImage(path);
            }
            if (AddImageUtils.bitmapPaths.contains(AddImageUtils.compressPathMap.get(path))) {
                AddImageUtils.removeImage(AddImageUtils.compressPathMap.get(path));
                AddImageUtils.compressPathMap.remove(path);
            }
        }

//        AddImageUtils.addImages(addList);
        //读取的图片先压缩为指定的像素值
        for (String path : addList) {
            File compressFile = FileUtls.saveNewPhotoFile(path, this);
            if (!path.equals(compressFile.getAbsolutePath())) {
                AddImageUtils.compressPathMap.put(path, compressFile.getAbsolutePath());
            }
            AddImageUtils.addImage(compressFile.getAbsolutePath());
        }
        Log.i("zwy", "imageCount:" + AddImageUtils.imageCount);
        finish();
    }
}
