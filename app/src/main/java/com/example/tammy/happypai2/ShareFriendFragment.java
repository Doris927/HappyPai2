package com.example.tammy.happypai2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tammy on 17/6/5.
 */

public class ShareFriendFragment extends Fragment {


    private Button btnTab1, btnTab2;// TAB Button
    private TabHost tabhost;
    private ArrayList<HashMap<String,Object>> following_listitems; //Following 图片文字信息
    private ArrayList<HashMap<String,Object>> mine_listitems; //Mine 图片文字信息
    private SimpleAdapter Following_Adapter;//Following 适配器
    private SimpleAdapter Mine_Adapter;// Mine 适配器
    private Button bt_follow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share_friend, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*Tab Zone*/
        tabhost = (TabHost)getView().findViewById(R.id.tabhost);
        tabhost.setup();
        tabhost.addTab(tabhost.newTabSpec("tab01").setIndicator("Following").setContent(R.id.listview_Following));
        tabhost.addTab(tabhost.newTabSpec("tab02").setIndicator("Mine").setContent(R.id.listview_Mine));
        /*listview_Following Zone*/
        ListView list_following = (ListView)getView().findViewById(R.id.listview_Following);//Following 界面绑定
        followingListview();
        //生成适配器
        SimpleAdapter mSchedule_Following = new SimpleAdapter(getContext(),
                following_listitems,
                R.layout.following_listview,
                //new String[]{"following_face_image", "follower_name","follower_action","mine_picture"},
                new String[]{ "follower_name","follower_action"},
                //new int[]{R.id.following_face_image, R.id.follower_name,R.id.follower_action,R.id.mine_picture}
                new int[]{ R.id.follower_name,R.id.follower_action});
        list_following.setAdapter(mSchedule_Following);

        /*listview_Mine Zone*/
        ListView list_mine = (ListView)getView().findViewById(R.id.listview_Mine);// Mine 界面绑定
        mineListview();
        //生成适配器
        SimpleAdapter mSchedule_Mine = new SimpleAdapter(getContext(),
                mine_listitems,
                R.layout.mine_listview,
                //new String[]{"following_face_image", "follower_name","follower_action","mine_picture"},
                new String[]{ "follower_name","follower_action"},
                //new int[]{R.id.following_face_image, R.id.follower_name,R.id.follower_action,R.id.mine_picture}
                new int[]{ R.id.follower_name,R.id.follower_action});
        list_mine.setAdapter(mSchedule_Mine);
    }

    private void mineListview() {
        mine_listitems = new ArrayList<HashMap<String, Object>>();
        for(int i = 0; i< 30; i++){
            HashMap<String,Object> map_Mine = new HashMap<String, Object>();
            map_Mine.put("mine_face_image",R.id.mine_face_image);
            map_Mine.put("follower_name","hahaha");
            map_Mine.put("follower_action","followed you");
            map_Mine.put("mine_followButton",R.id.mine_followButton);
            mine_listitems.add(map_Mine);
        }
    }

    //生成Following适配器内容
    private void followingListview() {
        following_listitems = new ArrayList<HashMap<String, Object>>();
        for(int i = 0; i< 30; i++){
            HashMap<String,Object> map_Following = new HashMap<String, Object>();
            map_Following.put("following_face_image",R.id.following_face_image);
            map_Following.put("follower_name","hahaha");
            map_Following.put("follower_action","liked your picture");
            map_Following.put("mine_picture",R.id.mine_picture);
            following_listitems.add(map_Following);
        }
    }

}