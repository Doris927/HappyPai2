package com.example.tammy.happypai2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageColorBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDissolveBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSwirlFilter;

public class Test2Activity extends AppCompatActivity {

    ImageView imageView;
    Button bt_test;

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
        bt_test = (Button)findViewById(R.id.bt_test01);
        bt_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testGPUImage(1);
            }
        });

        bt_test = (Button)findViewById(R.id.bt_test02);
        bt_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testGPUImage(2);
            }
        });

        bt_test = (Button)findViewById(R.id.bt_test03);
        bt_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testGPUImage(3);
            }
        });

        bt_test = (Button)findViewById(R.id.bt_test04);
        bt_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testGPUImage(4);
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
