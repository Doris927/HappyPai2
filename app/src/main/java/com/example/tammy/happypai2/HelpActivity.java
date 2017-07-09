package com.example.tammy.happypai2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }



    public void about_us(View view){
        startActivity(new Intent(this,About_usActivity.class));
    }
    public void send_message(View view){
        Intent data=new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:tammytangg@gmail.com"));
        data.putExtra(Intent.EXTRA_SUBJECT, "にくにくちゃんへ");
        data.putExtra(Intent.EXTRA_TEXT, "内容");
        startActivity(data);
    }


}
