package com.example.tammy.happypai2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.example.tammy.happypai2.util.DrawView;

public class AskPositionActivity extends AppCompatActivity {

    Bitmap mBitmap;
    DrawView drawView;
    /** Called when the activity is first created. */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_position);

        Intent intent=getIntent();
        String path=intent.getStringExtra("path");
        Bitmap bm = BitmapFactory.decodeFile(path);

        drawView = (DrawView) findViewById(R.id.ask_person);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.person);
        drawView.setDrawBitmap(mBitmap);
        drawView.setImageBitmap(bm);



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
    private class DragView extends View {
        private int mMotionX = 0;
        private int mMotionY = 0;
        private Paint paint;
        public DragView(Context context)
        {
            super(context);
            paint = new Paint();
        }
        @Override
        public void draw(Canvas canvas)
        {
            super.draw(canvas);
            canvas.drawBitmap(mBitmap, mMotionX, mMotionY, paint);
        }
        @Override
        public boolean onTouchEvent(MotionEvent ev)
        {
            if(ev.getAction() == MotionEvent.ACTION_DOWN)
            {
                mMotionX = (int) ev.getX();
                mMotionY = (int) ev.getY();
                invalidate();
                return true;
            }else {
                return super.onTouchEvent(ev);
            }
        }
    }

}
