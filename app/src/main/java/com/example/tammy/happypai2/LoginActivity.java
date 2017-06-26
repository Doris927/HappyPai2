package com.example.tammy.happypai2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends Activity implements View.OnClickListener{

    private Button bt_login;
    private Button bt_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();


    }

    private void initView(){
        bt_login = (Button)findViewById(R.id.bt_login);
        bt_login.setOnClickListener(this);

        bt_register = (Button)findViewById(R.id.bt_register);
        bt_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_login:
                startActivity(new Intent(this,ShareMainActivity.class));
                Log.v("button_test","button_login");
                break;
            case R.id.bt_register:
                startActivity(new Intent(this,RegisterActivity.class));
                Log.v("button_test","button_register");
                break;
            default:
                break;
        }
    }
}
