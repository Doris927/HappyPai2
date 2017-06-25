package com.example.tammy.happypai2;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

public class ShareSearchFragment extends Fragment {
    private ViewPager mViewPaper;
    private List<ImageView> images;
    private List<View> dots;
    private int currentItem;
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
        data=getdata();
        ShareItemAdapter adapter=new ShareItemAdapter(getContext(),data);
        lv.setAdapter(adapter);
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

    /**
     * 获得数据
     * @return
     */
    private List<Map<String,Object>> getdata()
    {
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        for(int i=0;i<10;i++)
        {
            Map<String,Object> map=new HashMap<String, Object>();
            map.put("img_icon", R.drawable.icon_user);
            map.put("username", "Tammy");
            map.put("time","2017/06/10 12:00");
            map.put("hasfollow",false);
            map.put("content", "contentcontentcontentcontentcontentcontent");
            map.put("img_content",R.drawable.advertise_img4);
            map.put("place","立命館大学");
            map.put("img_compose",R.drawable.button_effect_a);
            map.put("count_share","123");
            map.put("count_comment","123");
            map.put("count_thumb","123");
            list.add(map);
        }
        return list;
    }


}
