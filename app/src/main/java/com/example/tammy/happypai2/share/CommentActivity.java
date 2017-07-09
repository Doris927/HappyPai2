package com.example.tammy.happypai2.share;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

import com.example.tammy.happypai2.R;
import com.example.tammy.happypai2.bean.PostBean;
import com.hdl.myhttputils.MyHttpUtils;
import com.hdl.myhttputils.bean.CommCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView lv;
    private CommentItemAdapter mAdapter;
    private String state_id;
    private Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_comment);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.comment_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Comments");

        addBtn = (Button)findViewById(R.id.comment_addBtn);
        addBtn.setOnClickListener(this);
        Intent intent = getIntent();
        state_id = intent.getStringExtra("state_id");
        Log.v("intent", state_id + "");

        lv=(ListView)findViewById(R.id.comment_lv);
        getdata();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    void getdata(){
        Map<String, Object> params = new HashMap<>();//构造请求的参数
        params.put("action", "comment");
        params.put("user_id",state_id);

        MyHttpUtils.build()//构建myhttputils
                .url("http://52.41.31.68/api")//请求的url
                .addParams(params)
                .setJavaBean(PostBean.class)
                .onExecuteByPost(new CommCallback<PostBean>(){//开始执行，并有一个回调（异步的哦---->直接可以更新ui）
                    @Override
                    public void onSucceed(PostBean postBean) {//请求成功之后会调用这个方法----显示结果
                        //Toast.makeText(RegisterActivity.this,registerBean.getUser_id()+"",Toast.LENGTH_SHORT).show();
                        if(postBean.getState()==0){
                            List<PostBean.SharesBean> posts = postBean.getShares();
                            List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
                            for(int i = 0;i < posts.size();i ++){
                                Map<String,Object> map=new HashMap<String, Object>();
                                map.put("user_id", posts.get(i).getUser_id());
                                map.put("img_icon", R.drawable.icon_user);
                                map.put("username", posts.get(i).getUser_name());
                                map.put("time",posts.get(i).getTime_stamp());
                                map.put("content", posts.get(i).getContent());
                                list.add(map);
                            }

                            CommentItemAdapter adapter=new CommentItemAdapter(getApplicationContext(),list);
                            mAdapter = adapter;
                            lv.setAdapter(adapter);
                        }else{

                        }

                    }
                    @Override
                    public void onFailed(Throwable throwable) {//请求失败的时候会调用这个方法

                    }
                });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.comment_addBtn:
                Intent intent = new Intent(CommentActivity.this,CommentEditActivity.class);
                intent.putExtra("state_id", state_id);
                startActivity(intent);
                break;
            default:break;
        }
    }
}
