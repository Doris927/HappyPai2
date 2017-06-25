package com.example.tammy.happypai2;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tammy.happypai2.bean.FolderBean;
import com.example.tammy.happypai2.util.ImageLoader;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AlbumActivity extends AppCompatActivity {

    private GridView mGridView;
    private List<String> mImgs;
    private ImageAdapter mImgAdapter;

    private RelativeLayout mBottomLayout;
    private TextView mDirname;
    private TextView mDirCount;

    private File mCurrentDir;
    private int mMaxCount;

    private List<FolderBean> mFolderBeans = new ArrayList<FolderBean>();

    private ProgressDialog mProgressDialog;

    private static final int DATA_LOADED = 0x110;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == DATA_LOADED){
                mProgressDialog.dismiss();
                data2view();
            }
        }
    };

    private void data2view() {
        if (mCurrentDir == null) {
            Toast.makeText(this, "未扫描到任何图片", Toast.LENGTH_SHORT);
            return;
        }

        mImgs = Arrays.asList(mCurrentDir.list());
        mImgAdapter = new ImageAdapter(this,mImgs,mCurrentDir.getAbsolutePath());
        mGridView.setAdapter(mImgAdapter);

        mDirCount.setText(mMaxCount+"");
        mDirname.setText(mCurrentDir.getName());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        initView();
        initData();
        initEvent();
    }

    private void initEvent() {

    }

    /**
     * 利用ContentProvider扫描图片
     */
    private void initData() {
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(this,"当前存储卡不可用",Toast.LENGTH_SHORT);
            return;
        }

        mProgressDialog = ProgressDialog.show(this,null,"正在加载...");

        new Thread(){
            @Override
            public void run() {
                Uri mImgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver cr= AlbumActivity.this.getContentResolver();
                Cursor cursor = cr.query(mImgUri,null,MediaStore.Images.Media.MIME_TYPE + "= ? or "
                        +MediaStore.Images.Media.MIME_TYPE + " = ?",
                        new String[]{"image/jpeg","image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);

                Set<String> mDirPaths = new HashSet<String>();

                while (cursor.moveToNext()){
                    String path = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));
                    File parentFile = new File(path).getParentFile();
                    if (parentFile==null)
                        continue;

                    String dirPath = parentFile.getAbsolutePath();

                    FolderBean folderBean = null;

                    if(mDirPaths.contains(dirPath)){
                        continue;
                    }else {
                        mDirPaths.add(dirPath);
                        folderBean = new FolderBean();
                        folderBean.setDir(dirPath);
                        folderBean.setFirstImgPath(path);
                    }

                    if(parentFile.list()==null){
                        continue;
                    }

                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpeg")
                                    || filename.endsWith(".jpg")
                                    || filename.endsWith("png"))
                                return true;
                            return false;
                        }
                    }).length;

                    folderBean.setCount(picSize);

                    mFolderBeans.add(folderBean);

                    if (picSize > mMaxCount){
                        mMaxCount = picSize;
                        mCurrentDir = parentFile;
                    }
                }

                cursor.close();

                //通知handler扫描图片完成
                mHandler.sendEmptyMessage(DATA_LOADED);

            }
        }.start();


    }


    private void initView() {
        mGridView = (GridView)findViewById(R.id.album_gridview);
        mBottomLayout = (RelativeLayout) findViewById(R.id.album_bottomBtLayout);
        mDirname = (TextView)findViewById(R.id.album_dir_name);
        mDirCount = (TextView)findViewById(R.id.album_dir_count);
    }

    private class ImageAdapter extends BaseAdapter{

        private String mDirPath;
        private List<String> mImgsPath;
        private LayoutInflater mInflater;

        public ImageAdapter(Context context, List<String> mDatas, String dirPath){
            this.mDirPath = dirPath;
            this.mImgsPath = mDatas;
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mImgsPath.size();
        }

        @Override
        public Object getItem(int position) {
            return mImgsPath.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;

            if(convertView == null){
                convertView = mInflater.inflate(R.layout.item_album, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.mImg = (ImageView)convertView.findViewById(R.id.album_item_image);
                viewHolder.mSelect = (ImageButton)convertView.findViewById(R.id.album_item_select);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }

            //重置状态
            viewHolder.mImg.setImageResource(R.drawable.home_icon_ask);
            viewHolder.mSelect.setImageResource(R.drawable.check_a);

            Log.v("img file",mDirPath+"/"+mImgsPath.get(position));
            ImageLoader.getInstance(3, ImageLoader.Type.LIFO)
                    .loadImage(mDirPath+"/"+mImgsPath.get(position),viewHolder.mImg);

            viewHolder.mImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


            return convertView;
        }

        private class ViewHolder{
            ImageView mImg;
            ImageButton mSelect;
        }
    }


}
