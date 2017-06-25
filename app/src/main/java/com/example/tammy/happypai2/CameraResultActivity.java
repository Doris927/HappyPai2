package com.example.tammy.happypai2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CameraResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_result);

        String path = getIntent().getStringExtra("picPath");
        ImageView imageView = (ImageView)findViewById(R.id.picResult);
        try {
            FileInputStream fis = new FileInputStream(path);
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            Matrix matrix = new Matrix();
            matrix.setRotate(270);
            bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
            imageView.setImageBitmap(bitmap);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        Bitmap bitmap = BitmapFactory.decodeFile(path);
//        imageView.setImageBitmap(bitmap);
    }
}
