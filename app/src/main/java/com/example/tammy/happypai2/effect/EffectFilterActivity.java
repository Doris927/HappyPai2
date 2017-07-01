package com.example.tammy.happypai2.effect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.tammy.happypai2.R;
import com.example.tammy.happypai2.util.Util;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageColorBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDissolveBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSwirlFilter;

public class EffectFilterActivity extends AppCompatActivity {

    ImageView imageView;
    ImageButton bt_test01;
    ImageButton bt_test02;
    ImageButton bt_test03;
    ImageButton bt_test04;
    ImageButton bt_back, bt_sure;

    private GPUImage gpuImage;

    private String path;
    private Bitmap bm;
    private Bitmap bm_effect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        Intent intent=getIntent();
        path=intent.getStringExtra("path");
        bm = BitmapFactory.decodeFile(path);

        initView();

        imageView.setImageBitmap(bm);
    }

    private void initView(){
        imageView = (ImageView) findViewById(R.id.iv_test_image);
        bt_test01 = (ImageButton)findViewById(R.id.bt_test01);
        bt_test01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testGPUImage(1);
            }
        });

        bt_test02 = (ImageButton)findViewById(R.id.bt_test02);
        bt_test02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testGPUImage(2);
            }
        });

        bt_test03 = (ImageButton)findViewById(R.id.bt_test03);
        bt_test03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testGPUImage(3);
            }
        });

        bt_test04 = (ImageButton)findViewById(R.id.bt_test04);
        bt_test04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testGPUImage(4);
            }
        });

        bt_back = (ImageButton)findViewById(R.id.bt_back);
        bt_sure = (ImageButton)findViewById(R.id.bt_sure);

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = "temp_effect.png";
                Util.saveImage(getApplicationContext(),bm_effect,fileName);
                Intent intent = new Intent(EffectFilterActivity.this, EffectActivity.class);
                setResult(RESULT_OK,intent);
                finish();
            }
        });



    }

    public void testGPUImage(int filter){
        // 使用GPUImage处理图像
        gpuImage = new GPUImage(this);
        gpuImage.setImage(bm);

        switch (filter){
            case 1:
                gpuImage.setFilter(new GPUImageGrayscaleFilter());
                break;
            case 2:
                gpuImage.setFilter(new GPUImageSwirlFilter());
                break;
            case 3:
                gpuImage.setFilter(new GPUImageDissolveBlendFilter());
                break;
            case 4:
                gpuImage.setFilter(new GPUImageColorBlendFilter());
                break;
            default:break;
        }

        bm_effect = gpuImage.getBitmapWithFilterApplied();
        //显示处理后的图片
        imageView.setImageBitmap(bm_effect);
    }
}
