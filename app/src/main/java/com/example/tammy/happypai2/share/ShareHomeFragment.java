package com.example.tammy.happypai2.share;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.tammy.happypai2.R;
import com.example.tammy.happypai2.bean.PostBean;
import com.hdl.myhttputils.MyHttpUtils;
import com.hdl.myhttputils.bean.CommCallback;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tammy on 17/6/5.
 */

public class ShareHomeFragment extends Fragment {
        private ListView lv;
        private List<Map<String,Object>> data;
        private ShareItemAdapter mAdapter;


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.fragment_share_home, container, false);
                return view;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
                super.onActivityCreated(savedInstanceState);
                lv=(ListView)getView().findViewById(R.id.share_home_lv);
                getdata();
                super.onActivityCreated(savedInstanceState);
        }

        public void refreshList(){
                mAdapter.notifyDataSetChanged();
        }


        private void getdata()
        {
                SharedPreferences sharedPreferences2 = getContext()
                        .getSharedPreferences("user", Context.MODE_PRIVATE);
                String user_id = sharedPreferences2.getString("user_id", "null");

                Map<String, Object> params = new HashMap<>();//构造请求的参数
                params.put("action", "fetch_person_share");
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
                                                        list.add(map);
                                                }

                                                ShareItemAdapter adapter=new ShareItemAdapter(getContext(),list);
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

}
