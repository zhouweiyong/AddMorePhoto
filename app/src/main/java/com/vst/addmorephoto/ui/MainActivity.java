package com.vst.addmorephoto.ui;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.vst.addmorephoto.R;
import com.vst.addmorephoto.utils.AddImageUtils;
import com.vst.addmorephoto.utils.DialogUtils;
import com.vst.addmorephoto.utils.FileUtls;
import com.vst.addmorephoto.utils.PhotoUtils;

import java.io.File;


/**
 * 可能造成错误的地方：
 * 1，图片保存位置
 * 2,6.0版本需动态申请权限
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Button btn_add_pic;
    private LinearLayout layout_add_pic;
    private GridView gv_display_add_pic;
    private DisplayGvAdapter mDisplayGvAdapter;
    private File mNewPhotoFile;

    private static final int CODE_REQUEST_CAMERA = 0X110;
    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btn_add_pic = (Button) findViewById(R.id.btn_add_pic);
        layout_add_pic = (LinearLayout) findViewById(R.id.layout_add_pic);
        gv_display_add_pic = (GridView) findViewById(R.id.gv_display_add_pic);
        btn_add_pic.setOnClickListener(this);
        mDisplayGvAdapter = new DisplayGvAdapter(this);
        gv_display_add_pic.setAdapter(mDisplayGvAdapter);

        gv_display_add_pic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getCount() - 1 == position && AddImageUtils.imageCount < AddImageUtils.MAX_IMAGE_COUNT) {
                    addPic();
                } else {
                    Intent intent = new Intent(MainActivity.this, ShowImageActivity.class);
                    intent.putExtra(ShowImageActivity.INTENT_POSITION, position);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_pic:
                addPic();
                break;
        }
    }

    private void addPic() {
        mDialog = DialogUtils.createTwoChoicAlertNoTitle(this, "拍照", "从相册选择", new DialogUtils.OnDialogItemClickListener() {
            @Override
            public void onDialogItemClick(View v, int position) {
                if (position == 0) {
                    if (!FileUtls.isSDCardExist()) {
                        Toast.makeText(MainActivity.this, "sd卡不可用", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent cameraIntent = null;
                    mNewPhotoFile = FileUtls.initNewPhotoFile(MainActivity.this);
                    cameraIntent = PhotoUtils.getTakeCameraIntent(Uri.fromFile(mNewPhotoFile));
                    startActivityForResult(cameraIntent, CODE_REQUEST_CAMERA);
                    mDialog.dismiss();
                } else if (position == 1) {
                    startActivity(new Intent(MainActivity.this, AlbumActivity.class));
                }
            }
        });
        mDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case CODE_REQUEST_CAMERA:
                if (AddImageUtils.imageCount < AddImageUtils.MAX_IMAGE_COUNT && mNewPhotoFile != null) {
                    String newPhotoPath = mNewPhotoFile.getAbsolutePath();
                    mNewPhotoFile = FileUtls.saveNewPhotoFile(newPhotoPath, MainActivity.this);
                    AddImageUtils.addImage(newPhotoPath);
                }
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AddImageUtils.bitmapPaths != null && AddImageUtils.imageCount > 0) {
            AddImageUtils.bitmapPaths.remove("");
            layout_add_pic.setVisibility(View.GONE);
            gv_display_add_pic.setVisibility(View.VISIBLE);
            if (AddImageUtils.imageCount < AddImageUtils.MAX_IMAGE_COUNT) {
                AddImageUtils.bitmapPaths.add("");
            }
            mDisplayGvAdapter.setGroup(AddImageUtils.bitmapPaths);
        } else {
            layout_add_pic.setVisibility(View.VISIBLE);
            gv_display_add_pic.setVisibility(View.GONE);
        }
    }
}
