package com.example.tammy.happypai2.effect;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.tammy.happypai2.R;
import com.example.tammy.happypai2.util.MyCropView;

public class EditActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView iv_crop;
    ImageView iv_rotate;

    private String path;
    private Bitmap bm;
    private Bitmap bm_crop;
    private Bitmap bm_rotate;

    Button bt_crop_sure;
    ImageButton ibt_crop,ibt_rotate,ibt_back,ibt_sure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        initView();

        Intent intent=getIntent();
        path=intent.getStringExtra("path");
        bm = BitmapFactory.decodeFile(path);

        initView();

        iv_crop.setImageBitmap(bm);
        iv_rotate.setImageBitmap(bm);

    }

    private void initView(){
        iv_crop = (ImageView)findViewById(R.id.iv_crop);
        iv_rotate = (ImageView)findViewById(R.id.iv_rotate);
        ibt_crop = (ImageButton)findViewById(R.id.bt_crop);
        ibt_rotate = (ImageButton)findViewById(R.id.bt_rotate);
        ibt_back = (ImageButton)findViewById(R.id.bt_back);
        ibt_sure = (ImageButton)findViewById(R.id.bt_sure);
        bt_crop_sure = (Button)findViewById(R.id.bt_crop_sure);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_crop_sure:
//                clipPhoto(Uri.fromFile());
                break;
        }
    }

    /**
     * 裁剪图片方法实现
     * @param uri
     */
    public void clipPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop = true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例，这里设置的是正方形（长宽比为1:1）
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
//        startActivityForResult(intent, CUT_OK);
    }
}
