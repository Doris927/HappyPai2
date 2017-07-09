package com.example.tammy.happypai2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class CMActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cm);
    }

    public void cm(View view){
        startActivity(new Intent(this,Send_messagActivity.class));
    }
}
