package com.example.tammy.happypai2;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.tammy.happypai2.effect.EffectActivity;
import com.example.tammy.happypai2.share.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {

    private View view1, view2;
    private ViewPager viewPager;  //对应的viewPager

    private static final int IMAGE = 1;


    private List<View> viewList;//view数组

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        LayoutInflater inflater=getLayoutInflater();
        view1 = inflater.inflate(R.layout.main_layout1, null);
        view2 = inflater.inflate(R.layout.main_layout2,null);


        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
        viewList.add(view1);
        viewList.add(view2);


        PagerAdapter pagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(viewList.get(position));


                return viewList.get(position);
            }
        };


        viewPager.setAdapter(pagerAdapter);

        ImageButton bt_right = (ImageButton)view1.findViewById(R.id.button_right);
        ImageButton bt_left = (ImageButton)view2.findViewById(R.id.button_left);
        Button bt_camera = (Button) view1.findViewById(R.id.button_camera);
        Button bt_ask = (Button) view1.findViewById(R.id.button_ask);
        Button bt_effect = (Button) view1.findViewById(R.id.button_effect);
        Button bt_album = (Button) view1.findViewById(R.id.button_album);
        Button bt_share = (Button) view1.findViewById(R.id.button_share);
        Button bt_cm1 = (Button) view1.findViewById(R.id.button_cm1);
        Button bt_help = (Button) view2.findViewById(R.id.button_help);
        Button bt_cm2 = (Button) view2.findViewById(R.id.button_cm2);

        bt_right.setOnClickListener(this);
        bt_left.setOnClickListener(this);
        bt_camera.setOnClickListener(this);
        bt_ask.setOnClickListener(this);
        bt_effect.setOnClickListener(this);
        bt_album.setOnClickListener(this);
        bt_share.setOnClickListener(this);
        bt_cm1.setOnClickListener(this);
        bt_help.setOnClickListener(this);
        bt_cm2.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_right:
                Log.v("button_test","button_right");
                viewPager.setCurrentItem(1);
                break;
            case R.id.button_left:
                Log.v("button_test","button_left");
                viewPager.setCurrentItem(0);
                break;
            case R.id.button_camera:
                startActivity(new Intent(this,CameraActivity.class));
                Log.v("button_test","button_camera");
                break;
            case R.id.button_ask:
                startActivity(new Intent(this,AskActivity.class));
                Log.v("button_test","button_ask");
                break;
            case R.id.button_effect:
                Log.v("button_test","button_effect");
                Toast.makeText(this, "choose a photo", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE);

                break;
            case R.id.button_album:
                Log.v("button_test","button_album");
                startActivity(new Intent(this,AlbumActivity.class));
                break;
            case R.id.button_share:
                startActivity(new Intent(this,LoginActivity.class));
                Log.v("button_test","button_share");
                break;
            case R.id.button_cm1:
                Log.v("button_test","button_cm1");
                startActivity(new Intent(this,AskPositionActivity.class));
                break;
            case R.id.button_cm2:
                Log.v("button_test","button_cm2");
                startActivity(new Intent(this,TestActivity.class));
                break;
            case R.id.button_help:
                Log.v("button_test","button_help");
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
//            showImage(imagePath);
            c.close();

            Intent intent=new Intent();
            intent.setClass(this,EffectActivity.class);
            intent.putExtra("path", imagePath);
            startActivity(intent);

        }


    }

}
