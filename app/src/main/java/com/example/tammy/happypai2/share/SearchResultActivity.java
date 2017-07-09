package com.example.tammy.happypai2.share;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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

public class SearchResultActivity extends AppCompatActivity {
    private ListView lv;
    private ShareItemAdapter mAdapter;
    private String search_kw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.search_result_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Search Results");

        Intent intent = getIntent();
        search_kw = intent.getStringExtra("search_kw");

        lv=(ListView)findViewById(R.id.search_result_lv);
        getdata(search_kw);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void getdata(String search_kw)
    {
        SharedPreferences sharedPreferences2 = getApplicationContext()
                .getSharedPreferences("user", Context.MODE_PRIVATE);
        String user_id = sharedPreferences2.getString("user_id", "null");

        Map<String, Object> params = new HashMap<>();//构造请求的参数
        params.put("action", "search");
        if(search_kw != null && search_kw.length() > 0){
            params.put("search_kw", search_kw);
        }
        params.put("user_id",user_id);
        params.put("post_num", 10);

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
                                map.put("hasfollow",posts.get(i).getFollowee_id() != null);
                                map.put("content", posts.get(i).getState_text());
                                map.put("img_content",posts.get(i).getPicture());
                                map.put("place",posts.get(i).getLocation());
                                map.put("img_compose",R.drawable.button_effect_a);
                                map.put("count_share","123");
                                if(posts.get(i).getComment_num() != null){
                                    map.put("count_comment",posts.get(i).getComment_num());
                                }
                                else{
                                    map.put("count_comment","0");
                                }
                                map.put("count_thumb","123");
                                map.put("state_id", posts.get(i).getState_id());
                                map.put("composition",posts.get(i).getLayout_id());
                                list.add(map);
                            }
                            if(mAdapter == null) {
                                ShareItemAdapter adapter = new ShareItemAdapter(getApplicationContext(), list);
                                lv.setAdapter(adapter);
                                mAdapter = adapter;
                            }
                            else{
                                mAdapter.setData(list);
                            }
                        }else{

                        }

                    }
                    @Override
                    public void onFailed(Throwable throwable) {//请求失败的时候会调用这个方法

                    }
                });
    }
}
