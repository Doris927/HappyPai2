package com.example.tammy.happypai2;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class CameraActivity extends Activity implements View.OnClickListener,SurfaceHolder.Callback{

    private Camera mCamera;
    private SurfaceView mPreview;
    private SurfaceHolder mHolder;
    private ImageButton bt_capture,bt_cancel,bt_turn,bt_flash;

    private int mCurrentCameraId = 0; // 1是前置 0是后置


    private boolean select = false;

    private final int POSITION=1;


    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File tempFile =null;
            if (select){
                tempFile = new File("/sdcard/temp_ask.png");
            }else {
                tempFile = new File("/sdcard/temp.png");
            }

            try {

                FileOutputStream fos = new FileOutputStream(tempFile);
                fos.write(data);
                fos.close();

                if (select){
                    Intent intent = new Intent(CameraActivity.this, AskPositionActivity.class);
                    intent.putExtra("select",true);
                    intent.putExtra("path", tempFile.getAbsolutePath());
                    startActivityForResult(intent,POSITION);
                }else{
                    Intent intent = new Intent(CameraActivity.this, CameraResultActivity.class);
                    intent.putExtra("picPath", tempFile.getAbsolutePath());
                    startActivity(intent);
                }
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
        setContentView(R.layout.activity_camera);
        initView();



        mHolder = mPreview.getHolder();
        mHolder.addCallback(this);

        Intent intent=getIntent();
        select= intent.getBooleanExtra("select",false);


    }

    public void initView(){
        bt_capture = (ImageButton) findViewById(R.id.btCapture);
        bt_cancel=(ImageButton)findViewById(R.id.btCancel);
        bt_turn = (ImageButton)findViewById(R.id.btTurnCamera);
        bt_flash = (ImageButton)findViewById(R.id.btFlash);

        mPreview=(SurfaceView)findViewById(R.id.preview);


        bt_capture.setOnClickListener(this);
        bt_turn.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);
        bt_flash.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btCapture:
                Log.v("button_test","button_capture");
                capture();
                break;
            case R.id.btTurnCamera:
                Log.v("button_test","button_turn_camera");
                switchCamera();
                break;
            case R.id.btCancel:
                this.finish();
                break;
            case R.id.btFlash:
                Log.v("button_test","button_turn_light");
                turnLight();
                break;
            default:
                break;
        }
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
        releaseCamera();
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

    /**
     * 切换前后置摄像头
     */
    private void switchCamera() {
        mCurrentCameraId = (mCurrentCameraId + 1) % Camera.getNumberOfCameras();
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        try {
            mCamera = Camera.open(mCurrentCameraId);
            setStartPreview(mCamera,mHolder);
        } catch (Exception e) {
            Toast.makeText(this, "未发现相机", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * 闪光灯开关 开->关->自动
     */
    private void turnLight() {
        if (mCamera == null || mCamera.getParameters() == null
                || mCamera.getParameters().getSupportedFlashModes() == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        String flashMode = mCamera.getParameters().getFlashMode();
        List<String> supportedModes = mCamera.getParameters()
                .getSupportedFlashModes();
        if (Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)
                && supportedModes.contains(Camera.Parameters.FLASH_MODE_ON)) {// 关闭状态
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            mCamera.setParameters(parameters);
//            flashBtn.setImageResource(R.drawable.camera_flash_on);
        } else if (Camera.Parameters.FLASH_MODE_ON.equals(flashMode)) {// 开启状态
            if (supportedModes.contains(Camera.Parameters.FLASH_MODE_AUTO)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
//                flashBtn.setImageResource(R.drawable.camera_flash_auto);
                mCamera.setParameters(parameters);
            } else if (supportedModes
                    .contains(Camera.Parameters.FLASH_MODE_OFF)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//                flashBtn.setImageResource(R.drawable.camera_flash_off);
                mCamera.setParameters(parameters);
            }
        } else if (Camera.Parameters.FLASH_MODE_AUTO.equals(flashMode)
                && supportedModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(parameters);
//            flashBtn.setImageResource(R.drawable.camera_flash_off);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if(requestCode == POSITION && resultCode == Activity.RESULT_OK){
            String path = data.getStringExtra("path");
            Intent intent = new Intent(CameraActivity.this, AskActivity.class);
            intent.putExtra("path", path);
            setResult(RESULT_OK,intent); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
            finish();//此处一定要调用finish()方法
        }
    }
}


