package com.example.tammy.happypai2.share;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tammy.happypai2.R;
import com.example.tammy.happypai2.bean.ShareBean;
import com.example.tammy.happypai2.effect.EditActivity;
import com.hdl.myhttputils.MyHttpUtils;
import com.hdl.myhttputils.bean.CommCallback;
import com.hdl.myhttputils.utils.FailedMsgUtils;

import java.util.HashMap;
import java.util.Map;

public class CommentEditActivity extends AppCompatActivity implements View.OnClickListener{
    private Button mShareBtn;
    private EditText mAddEt;
    private String state_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_edit);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.comment_edit_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("AddComments");

        Intent intent = getIntent();
        state_id = intent.getStringExtra("state_id");

        mShareBtn = (Button)findViewById(R.id.comment_edit_addBtn);
        mAddEt = (EditText)findViewById(R.id.comment_edit_et);

        mShareBtn.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.comment_edit_addBtn:
                shareComment(mAddEt.getText().toString());
                break;
            default:break;
        }
    }

    private void shareComment(String content){
        SharedPreferences sharedPreferences2 = getApplicationContext()
                .getSharedPreferences("user", Context.MODE_PRIVATE);
        String user_id = sharedPreferences2.getString("user_id", "null");

        Map<String, Object> params = new HashMap<>();//构造请求的参数
        params.put("action", "addComment");
        params.put("user_id",user_id);
        params.put("content",content);
        params.put("state_id",state_id);

        MyHttpUtils.build()//构建myhttputils
                .url("http://52.41.31.68/api")//请求的url
                .addParams(params)
                .setJavaBean(ShareBean.class)
                .onExecuteByPost(new CommCallback<ShareBean>(){//开始执行，并有一个回调（异步的哦---->直接可以更新ui）
                    @Override
                    public void onSucceed(ShareBean shareBean) {//请求成功之后会调用这个方法----显示结果
                        if(shareBean.getState()==0){
                            finish();
                        }else{
                            Toast.makeText(CommentEditActivity.this,shareBean.getMsg(),Toast.LENGTH_SHORT).show();
                        }

                    }
                    @Override
                    public void onFailed(Throwable throwable) {//请求失败的时候会调用这个方法
                        Toast.makeText(CommentEditActivity.this, "Network Error",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
