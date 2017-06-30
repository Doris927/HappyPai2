package com.example.tammy.happypai2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.tammy.happypai2.util.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AskActivity extends AppCompatActivity implements View.OnClickListener,SurfaceHolder.Callback{

    private Camera mCamera;
    private SurfaceView mPreview;
    private SurfaceHolder mHolder;


    private Context context = null;
    private PopupWindow popupWindow;

    private static final int IMAGE = 1;
    private static final int POSITION=2;
    //hahhahahhahahhah
    //lallalallllal
    ImageButton bt_capture;
    ImageView iv_refer;

    ImageLoader imageLoader;


    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File tempFile = new File("/sdcard/temp.png");
            try {
                FileOutputStream fos = new FileOutputStream(tempFile);
                fos.write(data);
                fos.close();
                Intent intent = new Intent(AskActivity.this, CameraResultActivity.class);
                intent.putExtra("picPath",tempFile.getAbsolutePath());
                startActivity(intent);
//                CameraActivity.this.finish();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_ask);
        bt_capture = (ImageButton) findViewById(R.id.btCapture);
        iv_refer = (ImageView)findViewById(R.id.image_refer);

        bt_capture.setOnClickListener(this);
        iv_refer.setOnClickListener(this);

        mPreview=(SurfaceView)findViewById(R.id.preview);
        mHolder = mPreview.getHolder();
        mHolder.addCallback(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btCapture:
                Log.v("button_test","button_capture");
                capture();
                break;
            case R.id.image_refer:
                Log.v("button_test","image_refer");
                initPopupWindow();
                break;
            default:
                break;
        }
    }

    class popupDismissListener implements PopupWindow.OnDismissListener{

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }

    }



    protected void initPopupWindow(){
        View popupWindowView = getLayoutInflater().inflate(R.layout.popmenu_ask, null);
        //内容，高度，宽度

        popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setAnimationStyle(R.style.AnimationBottomFade);
        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        popupWindow.setBackgroundDrawable(dw);
        //宽度
        //popupWindow.setWidth(LayoutParams.WRAP_CONTENT);
        //高度
//        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //显示位置


        popupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_main, null), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);

        //设置背景半透明
        backgroundAlpha(0.5f);
        //关闭事件
        popupWindow.setOnDismissListener(new popupDismissListener());

        popupWindowView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                /*if( popupWindow!=null && popupWindow.isShowing()){
                    popupWindow.dismiss();
                    popupWindow=null;
                }*/
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                return false;
            }
        });

        Button btn_takephoto = (Button)popupWindowView.findViewById(R.id.ask_btn_takephoto);
        Button btn_choose = (Button)popupWindowView.findViewById(R.id.ask_btn_choose);
        Button btn_cancel = (Button)popupWindowView.findViewById(R.id.ask_btn_cancel);


        btn_takephoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context, "take a photo", Toast.LENGTH_LONG).show();
                popupWindow.dismiss();
            }
        });

        btn_choose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context, "choose a photo", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE);

                popupWindow.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context, "cancel", Toast.LENGTH_LONG).show();
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 选择完图片
     * @param requestCode
     * @param resultCode
     * @param data
     */
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
            intent.setClass(this,AskPositionActivity.class);
            intent.putExtra("path", imagePath);
            Bundle bundle=new Bundle();
            intent.putExtras(bundle);
            startActivityForResult(intent, POSITION);
//            startActivity(intent);

        }else if(requestCode == POSITION && resultCode == Activity.RESULT_OK){
            String path = data.getStringExtra("path");
            Log.v("path",path);
            //imageLoader.loadImage(path,iv_refer);
            showImage(path);
            Toast.makeText(this,path,Toast.LENGTH_SHORT).show();
        }


    }

    //加载图片
    private void showImage(String imaePath){
        Bitmap bm = BitmapFactory.decodeFile(imaePath);
        iv_refer.setImageBitmap(bm);
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    private void capture(){
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setPreviewSize(800,480);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if(success){
                    mCamera.takePicture(null,null,mPictureCallback);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mCamera == null){
            mCamera = getCamera();

            if (mCamera == null){
                Log.v("error","no camera");
            }

            if (mHolder != null ){
                setStartPreview(mCamera,mHolder);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        releaseCamera();
    }

    /**
     * get camera
     * @return
     */
    private Camera getCamera(){
        Camera camera;
        try {
            camera = Camera.open(0);
        }catch (Exception e){
            camera = null;
            e.printStackTrace();
        }

        return camera;
    }

    /**
     * start to show the preview
     */
    private void setStartPreview(Camera camera, SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            //set camera vertical
            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * release the camera resource
     */
    private void releaseCamera(){
        if(mCamera!=null){
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
//            mHolder = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setStartPreview(mCamera, mHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();
        setStartPreview(mCamera,mHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }
}
