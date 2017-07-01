package com.example.tammy.happypai2.effect;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.tammy.happypai2.AskPositionActivity;
import com.example.tammy.happypai2.CameraActivity;
import com.example.tammy.happypai2.R;
import com.example.tammy.happypai2.util.MyCropView;
import com.example.tammy.happypai2.util.Util;
import com.soundcloud.android.crop.Crop;
import com.soundcloud.android.crop.CropImageActivity;

import java.io.File;

public class EditActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView iv_crop;

    private String path;
    private Bitmap bm;

    ImageButton ibt_crop,ibt_rotate,ibt_back,ibt_sure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        initView();

        Intent intent=getIntent();
        path=intent.getStringExtra("path");
        Log.v("path",path);
        bm = BitmapFactory.decodeFile(path);

        initView();

        iv_crop.setImageBitmap(bm);

    }

    private void initView(){
        iv_crop = (ImageView)findViewById(R.id.iv_crop);
        ibt_crop = (ImageButton)findViewById(R.id.bt_crop);
        ibt_rotate = (ImageButton)findViewById(R.id.bt_rotate);
        ibt_back = (ImageButton)findViewById(R.id.bt_back);
        ibt_sure = (ImageButton)findViewById(R.id.bt_sure);

        ibt_crop.setOnClickListener(this);
        ibt_rotate.setOnClickListener(this);
        ibt_back.setOnClickListener(this);
        ibt_sure.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bt_crop:
                File file = new File(path);
                Crop.of(Uri.fromFile(file), Uri.fromFile(file)).start(this);
                break;
            case R.id.bt_rotate:
                bm=rotateBitmap(bm,90);
                iv_crop.setImageBitmap(bm);
                break;
            case R.id.bt_back:
                finish();
                break;
            case R.id.bt_sure:
                String fileName = "temp_effect.png";
                Util.saveImage(getApplicationContext(),bm,fileName);
                Intent intent = new Intent(EditActivity.this, EffectActivity.class);
                setResult(RESULT_OK,intent);
                finish();
                break;
            default:break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            bm = BitmapFactory.decodeFile(path);
            iv_crop.setImageBitmap(bm);
        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (degrees == 0 || null == bitmap) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (null != bitmap) {
            bitmap.recycle();
        }
        return bmp;
    }

}
