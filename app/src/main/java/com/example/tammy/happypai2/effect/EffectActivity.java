package com.example.tammy.happypai2.effect;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tammy.happypai2.R;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;

public class EffectActivity extends AppCompatActivity implements View.OnClickListener{


    ImageView imageView;
    ImageButton ibt_back,ibt_sure;
    Button bt_edit, bt_strengthen, bt_effect, bt_clear;

    private GPUImage gpuImage;

    private String path;
    private Bitmap bm;
    private Bitmap bm_effect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effect);

        Intent intent=getIntent();
        path=intent.getStringExtra("path");
        bm = BitmapFactory.decodeFile(path);

        initView();

        imageView.setImageBitmap(bm);



    }

    private void initView(){
        imageView = (ImageView) findViewById(R.id.iv_effect_pic);
        ibt_back = (ImageButton)findViewById(R.id.bt_back);
        ibt_sure = (ImageButton)findViewById(R.id.bt_sure);
        bt_edit = (Button)findViewById(R.id.bt_edit);
        bt_strengthen = (Button)findViewById(R.id.bt_strengthen);
        bt_effect = (Button)findViewById(R.id.bt_effect);
        bt_clear = (Button)findViewById(R.id.bt_clear);

        ibt_back.setOnClickListener(this);
        ibt_sure.setOnClickListener(this);
        bt_edit.setOnClickListener(this);
        bt_strengthen.setOnClickListener(this);
        bt_effect.setOnClickListener(this);
        bt_clear.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_back:
                finish();
                break;
            case R.id.bt_sure:
                testGPUImage();
                break;
            case R.id.bt_edit:
                Intent intent=new Intent();
                intent.setClass(this,EditActivity.class);
                intent.putExtra("path", path);
                startActivity(intent);
                break;
            case R.id.bt_strengthen:
                break;
            case R.id.bt_effect:
                break;
            case R.id.bt_clear:
                break;
            default:
                break;
        }
    }

    public void testGPUImage(){
        // 使用GPUImage处理图像
        gpuImage = new GPUImage(this);
        gpuImage.setImage(bm);
        gpuImage.setFilter(new GPUImageGrayscaleFilter());
        bm_effect = gpuImage.getBitmapWithFilterApplied();
        //显示处理后的图片
        imageView.setImageBitmap(bm_effect);
    }


}
