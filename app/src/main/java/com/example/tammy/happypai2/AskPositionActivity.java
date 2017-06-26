package com.example.tammy.happypai2;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.example.tammy.happypai2.util.DrawView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AskPositionActivity extends AppCompatActivity {

    Bitmap mBitmap;
    DrawView drawView;
    ImageButton ibt_sure,ibt_back;

    String fileName="test.png";
    String filePath=null;

    private ProgressDialog pd;
    /** Called when the activity is first created. */

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_position);

        Intent intent=getIntent();
        final String path=intent.getStringExtra("path");
        Bitmap bm = BitmapFactory.decodeFile(path);

        drawView = (DrawView) findViewById(R.id.ask_person);
        ibt_back = (ImageButton)findViewById(R.id.bt_back);
        ibt_sure = (ImageButton)findViewById(R.id.bt_sure);

        int screenWidth = getScreenWidth(this);
        ViewGroup.LayoutParams lp = drawView.getLayoutParams();
        lp.width = screenWidth;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        drawView.setLayoutParams(lp);

        drawView.setMaxWidth(screenWidth);
        drawView.setMaxHeight(screenWidth * 5);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.person);
        drawView.setDrawBitmap(mBitmap);
        drawView.setImageBitmap(bm);




        ibt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pd = ProgressDialog.show(AskPositionActivity.this, "保存", "保存中...");

                /* 开启一个新线程，在新线程里执行耗时的方法 */
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        viewSaveToImage(drawView);
                        handler.sendEmptyMessage(0);// 执行耗时的方法之后发送消给handler
                    }

                }).start();



//                Intent intent = new Intent(AskPositionActivity.this, AskActivity.class);
//
//                intent.putExtra("path", path);
//                setResult(RESULT_OK); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
//                finish();//此处一定要调用finish()方法
            }
        });





    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(null != mBitmap) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }



    public void viewSaveToImage(View view) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setDrawingCacheBackgroundColor(Color.WHITE);

        // 把一个View转换成图片
        Bitmap cachebmp = getViewBitmap(drawView);

//        // 添加水印
//        Bitmap bitmap = Bitmap.createBitmap(createWatermarkBitmap(cachebmp,
//                "@ tang"));

        FileOutputStream fos;
        try {
            // 判断手机设备是否有SD卡
            boolean isHasSDCard = Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED);
            if (isHasSDCard) {
                // SD卡根目录
                File sdRoot = Environment.getExternalStorageDirectory();
                SimpleDateFormat sTimeFormat=new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss");
                String date=sTimeFormat.format(new Date());
                fileName = date+".jpg";
                File file = new File(sdRoot,fileName);
                filePath = file.getAbsolutePath();
                fos = new FileOutputStream(file);
            } else
                throw new Exception("创建文件失败!");

            cachebmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

//        view.destroyDrawingCache();
    }

    private Bitmap getViewBitmap(View v) {

        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        // Draw background
        Drawable bgDrawable = v.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(c);
        else
            c.drawColor(Color.TRANSPARENT);
        // Draw view to canvas
        v.draw(c);
        return b;
    }



    // 为图片target添加水印
    private Bitmap createWatermarkBitmap(Bitmap target, String str) {
        int w = target.getWidth();
        int h = target.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bmp);
//
//        Paint p = new Paint();
//
//        // 水印的颜色
//        p.setColor(Color.RED);
//
//        // 水印的字体大小
//        p.setTextSize(16);
//
//        p.setAntiAlias(true);// 去锯齿
//
//        canvas.drawBitmap(target, 0, 0, p);
//
//        // 在中间位置开始添加水印
//        canvas.drawText(str, w / 2, h / 2, p);
//
//        canvas.save(Canvas.ALL_SAVE_FLAG);
//        canvas.restore();

        return bmp;
    }

    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {// handler接收到消息后就会执行此方法
            pd.dismiss();// 关闭ProgressDialog
            Intent intent = new Intent(AskPositionActivity.this, AskActivity.class);
            intent.putExtra("path", filePath);
            setResult(RESULT_OK,intent); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
            finish();//此处一定要调用finish()方法
        }
    };


}
