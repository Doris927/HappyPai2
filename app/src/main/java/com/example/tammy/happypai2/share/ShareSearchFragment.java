package com.example.tammy.happypai2.share;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tammy.happypai2.bean.PostBean;
import com.example.tammy.happypai2.bean.ShareBean;
import com.example.tammy.happypai2.util.NoScrollListView;
import com.example.tammy.happypai2.R;
import com.hdl.myhttputils.MyHttpUtils;
import com.hdl.myhttputils.bean.CommCallback;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created by tammy on 17/6/5.
 */

public class ShareSearchFragment extends Fragment implements View.OnClickListener {
    private ViewPager mViewPaper;
    private List<ImageView> images;
    private List<View> dots;
    private int currentItem;
    private ShareItemAdapter mAdapter;
    private Button mSearchBtn;
    private EditText mSearchBar;
    //记录上一次点的位置
    private int oldPosition = 0;
    //存放图片的id
    private int[] imageIds = new int[]{
            R.drawable.advertise_img1,
            R.drawable.advertise_img2,
            R.drawable.advertise_img3,
            R.drawable.advertise_img4,
            R.drawable.advertise_img5
    };

    //存放图片的标题
    private String[]  titles = new String[]{
            "test1",
            "test2",
            "test3",
            "test4",
            "test5"
    };

    private TextView title;
    private ViewPagerAdapter adapter;
    private ScheduledExecutorService scheduledExecutorService;


    private NoScrollListView lv;
    private List<Map<String,Object>> data;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share_search, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewPaper = (ViewPager)getView().findViewById(R.id.vp);
        mSearchBtn = (Button)getView().findViewById(R.id.search_btn);
        mSearchBar = (EditText)getView().findViewById(R.id.edittext);
        mSearchBtn.setOnClickListener(this);
        mAdapter = null;

        //显示的图片
        images = new ArrayList<ImageView>();
        for(int i = 0; i < imageIds.length; i++){
            ImageView imageView = new ImageView(getContext());
            imageView.setBackgroundResource(imageIds[i]);
            images.add(imageView);
        }
        //显示的小点
        dots = new ArrayList<View>();
        dots.add(getView().findViewById(R.id.dot_0));
        dots.add(getView().findViewById(R.id.dot_1));
        dots.add(getView().findViewById(R.id.dot_2));
        dots.add(getView().findViewById(R.id.dot_3));
        dots.add(getView().findViewById(R.id.dot_4));

        title = (TextView)getView().findViewById(R.id.title);
        title.setText(titles[0]);

        adapter = new ViewPagerAdapter();
        mViewPaper.setAdapter(adapter);

        mViewPaper.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageSelected(int position) {
                title.setText(titles[position]);
                dots.get(position).setBackgroundResource(R.drawable.dot_focus);
                dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);

                oldPosition = position;
                currentItem = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        lv=(NoScrollListView) getView().findViewById(R.id.share_search_lv);
        getdata("fetch_share", "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.search_btn:
                Intent intent = new Intent(getContext(), SearchResultActivity.class);
                intent.putExtra("search_kw", mSearchBar.getText().toString());
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 自定义Adapter
     * @author liuyazhuang
     *
     */
    private class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            // TODO Auto-generated method stub
//          super.destroyItem(container, position, object);
//          view.removeView(view.getChildAt(position));
//          view.removeViewAt(position);
            view.removeView(images.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            // TODO Auto-generated method stub
            view.addView(images.get(position));
            return images.get(position);
        }

    }

    /**
     * 利用线程池定时执行动画轮播
     */
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(
                new ViewPageTask(),
                2,
                2,
                TimeUnit.SECONDS);
    }


    /**
     * 图片轮播任务
     * @author liuyazhuang
     *
     */
    private class ViewPageTask implements Runnable{

        @Override
        public void run() {
            currentItem = (currentItem + 1) % imageIds.length;
            mHandler.sendEmptyMessage(0);
        }
    }

    /**
     * 接收子线程传递过来的数据
     */
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            mViewPaper.setCurrentItem(currentItem);
        };
    };
    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if(scheduledExecutorService != null){
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;
        }
    }

    public void refreshList(){
        mAdapter.notifyDataSetChanged();
    }

    public Drawable LoadImageFromWebOperations(String url) {
        Log.v("draw", "get image url " + url);
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            Log.v("draw", "get image success");
            return d;
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("draw", "get image fail");
            return null;
        }
    }

    /**
     * 获得数据
     * @return
     */
    public void getdata(String action, String search_kw)
    {
        SharedPreferences sharedPreferences2 = getContext()
                .getSharedPreferences("user", Context.MODE_PRIVATE);
        String user_id = sharedPreferences2.getString("user_id", "null");

        Map<String, Object> params = new HashMap<>();//构造请求的参数
        params.put("action", action);
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
                                ShareItemAdapter adapter = new ShareItemAdapter(getContext(), list);
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
