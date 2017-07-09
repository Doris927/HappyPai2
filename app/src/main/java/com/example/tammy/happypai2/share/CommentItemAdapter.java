package com.example.tammy.happypai2.share;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tammy.happypai2.R;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 7/9/17.
 */

class CommentViewHolder
{
    public ImageView img_icon;
    public TextView tv_username;
    public TextView tv_time;
    public TextView tv_content;
}

public class CommentItemAdapter extends BaseAdapter {
    private LayoutInflater mInflater=null;
    private List<Map<String,Object>> data;
    private Context context;

    public CommentItemAdapter(Context context,List<Map<String,Object>> data){
        this.mInflater=LayoutInflater.from(context);  //这里就是确定你listview在哪一个layout里面展示
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        CommentViewHolder holder=null;
        if(convertView==null)
        {
            holder=new CommentViewHolder();
            convertView=mInflater.inflate(R.layout.comment_item,null); //这里确定你listview里面每一个item的layout
            holder.img_icon = (ImageView)convertView.findViewById(R.id.comment_img_icon); //此处是将内容与控件绑定。
            holder.tv_username = (TextView) convertView.findViewById(R.id.comment_user_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.comment_time);
            holder.tv_content = (TextView)convertView.findViewById(R.id.comment_content);
            convertView.setTag(holder);
        }
        else{
            holder=(CommentViewHolder)convertView.getTag(); //这里是为了提高listview的运行效率
        }

        holder.tv_content.setText((String)data.get(i).get("content"));
        holder.img_icon.setBackgroundResource((Integer)data.get(i).get("img_icon"));
        holder.tv_username.setText((String)data.get(i).get("username"));
        holder.tv_time.setText((String)data.get(i).get("time"));


        return convertView;
    }
}
