package com.example.tammy.happypai2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
                data=getdata();
                ShareItemAdapter adapter=new ShareItemAdapter(getContext(),data);
                lv.setAdapter(adapter);

                super.onActivityCreated(savedInstanceState);
        }

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
