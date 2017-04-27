package com.vst.addmorephoto.ui;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.vst.addmorephoto.R;
import com.vst.addmorephoto.utils.AddImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zwy on 2017/4/25.
 * email:16681805@qq.com
 * 显示图片，可以删除指定
 */

public class ShowImageActivity extends Activity implements View.OnClickListener {

    public static final String INTENT_POSITION = "position";
    private ViewPager vp_show_image;
    private Button btn_del;
    private ArrayList<View> mViews;
    private ShowImageAdapter mShowImageAdapter;
    private int mCurrentPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        mCurrentPosition = getIntent().getIntExtra(INTENT_POSITION, 0);
        initView();
    }

    private void initView() {
        mViews = new ArrayList<>();
        vp_show_image = (ViewPager) findViewById(R.id.vp_show_image);
        btn_del = (Button) findViewById(R.id.btn_del);

        btn_del.setOnClickListener(this);

        vp_show_image.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        for (int i = 0; i < AddImageUtils.imageCount; i++) {
            ImageView iv = new ImageView(this);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            iv.setLayoutParams(lp);
            iv.setBackgroundColor(Color.BLACK);
            iv.setImageURI(Uri.parse(AddImageUtils.bitmapPaths.get(i)));
            mViews.add(iv);
        }
        mShowImageAdapter = new ShowImageAdapter(mViews);
        vp_show_image.setAdapter(mShowImageAdapter);
        vp_show_image.setCurrentItem(mCurrentPosition);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_del:
                delImage();
                break;
        }
    }


    private void delImage() {
        if (mViews.size() <= 1) {
            AddImageUtils.clear();
            finish();
            return;
        }
        AddImageUtils.removeImage(mCurrentPosition);
        vp_show_image.removeAllViews();
        mViews.remove(mCurrentPosition);
        mShowImageAdapter.setData(mViews);
        mShowImageAdapter.notifyDataSetChanged();

    }

    class ShowImageAdapter extends PagerAdapter {
        private List<View> mDatas;
//        private int size;

        public ShowImageAdapter(List<View> mDatas) {
            this.mDatas = mDatas;
//            size = mDatas == null ? 0 : mDatas.size();
        }

        public void setData(List<View> mViews) {
            this.mDatas = mViews;
//            size = mDatas == null ? 0 : mDatas.size();
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mDatas.get(position), 0);
            return mDatas.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView(mDatas.get(position % size));
            container.removeView((View) object);
        }
    }
}
