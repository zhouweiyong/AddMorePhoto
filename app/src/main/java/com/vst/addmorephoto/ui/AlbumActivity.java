package com.vst.addmorephoto.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.vst.addmorephoto.R;
import com.vst.addmorephoto.bean.AlbumBean;
import com.vst.addmorephoto.utils.AlbumTool;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zwy on 2017/4/23.
 * email:16681805@qq.com
 * 相册页面
 */

public class AlbumActivity extends Activity {

    private TextView btn_left;
    private GridView gv_album;
    private List<AlbumBean> mAlbumBeanList;
    private AlbumTool mAlbumTool;
    private AlbumGvAdapter mAlbumGvAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        initView();
    }

    private void initView() {
        btn_left = (TextView) findViewById(R.id.btn_left);
        gv_album = (GridView) findViewById(R.id.gv_album);

        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mAlbumTool = AlbumTool.getInstance(this);
        mAlbumGvAdapter = new AlbumGvAdapter(this);
        gv_album.setAdapter(mAlbumGvAdapter);

        mAlbumBeanList = mAlbumTool.getAlbumList();
        mAlbumGvAdapter.setGroup(mAlbumBeanList);
        gv_album.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AlbumActivity.this, ImageSelectActivity.class);
                intent.putExtra(ImageSelectActivity.INTENT_IMAGE_BEAN, (Serializable) mAlbumBeanList.get(position).imageList);
                startActivity(intent);
            }
        });
    }

}
