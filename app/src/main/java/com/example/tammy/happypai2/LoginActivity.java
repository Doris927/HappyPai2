package com.example.tammy.happypai2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends Activity implements View.OnClickListener{

    private Button bt_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bt_login = (Button)findViewById(R.id.bt_login);
        bt_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_login:
                startActivity(new Intent(this,ShareMainActivity.class));
                Log.v("button_test","button_share_main");
                break;
            default:
                break;
        }
    }
}
