package com.example.tammy.happypai2.share;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tammy.happypai2.R;
import com.example.tammy.happypai2.effect.EditActivity;
import com.example.tammy.happypai2.effect.EffectActivity;

import java.util.ArrayList;
import java.util.List;

public class ShareMainActivity extends AppCompatActivity implements View.OnClickListener{

    //声明ViewPager
    private ViewPager mViewPager;
    //适配器
    private FragmentPagerAdapter mAdapter;
    //装载Fragment的集合
    private List<Fragment> mFragments;

    //四个Tab对应的布局
    private LinearLayout mTabHome;
    private LinearLayout mTabSearch;
    private LinearLayout mTabFriend;
    private LinearLayout mTabProfile;

    //四个Tab对应的ImageButton
    private ImageButton mImgHome;
    private ImageButton mImgSearch;
    private ImageButton mImgFriend;
    private ImageButton mImgProfile;
    private ImageButton mSend;

    private static final int IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_share_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.share_main_toolbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
//        ab.setDisplayShowTitleEnabled(false);

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        initViews();//初始化控件
        initEvents();//初始化事件
        initDatas();//初始化数据
    }

    private void initDatas() {
        mFragments = new ArrayList<>();
        //将四个Fragment加入集合中
        mFragments.add(new ShareHomeFragment());
        mFragments.add(new ShareSearchFragment());
        mFragments.add(new ShareFriendFragment());
        mFragments.add(new ShareProfileFragment());

        //初始化适配器
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {//从集合中获取对应位置的Fragment
                return mFragments.get(position);
            }

            @Override
            public int getCount() {//获取集合中Fragment的总数
                return mFragments.size();
            }

        };
        //不要忘记设置ViewPager的适配器
        mViewPager.setAdapter(mAdapter);
        //设置ViewPager的切换监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            //页面滚动事件
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //页面选中事件
            @Override
            public void onPageSelected(int position) {
                //设置position对应的集合中的Fragment
                mViewPager.setCurrentItem(position);
                resetImgs();
                selectTab(position);
            }

            @Override
            //页面滚动状态改变事件
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initEvents() {
        //设置四个Tab的点击事件
        mTabHome.setOnClickListener(this);
        mTabSearch.setOnClickListener(this);
        mTabFriend.setOnClickListener(this);
        mTabProfile.setOnClickListener(this);
        mSend.setOnClickListener(this);

    }

    //初始化控件
    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mTabHome = (LinearLayout) findViewById(R.id.tab_share_home);
        mTabSearch = (LinearLayout) findViewById(R.id.tab_share_search);
        mTabFriend = (LinearLayout) findViewById(R.id.tab_share_friend);
        mTabProfile = (LinearLayout) findViewById(R.id.tab_share_profile);

        mImgHome = (ImageButton) findViewById(R.id.tab_image_home);
        mImgSearch = (ImageButton) findViewById(R.id.tab_image_search);
        mImgFriend = (ImageButton) findViewById(R.id.tab_image_friend);
        mImgProfile = (ImageButton) findViewById(R.id.tab_image_profile);
        mSend = (ImageButton)findViewById(R.id.button_send);

    }

    @Override
    public void onClick(View v) {
        //先将四个ImageButton置为灰色
        resetImgs();

        //根据点击的Tab切换不同的页面及设置对应的ImageButton为绿色
        switch (v.getId()) {
            case R.id.tab_share_home:
                selectTab(0);
                ShareHomeFragment fgHome = (ShareHomeFragment)mFragments.get(0);
                fgHome.refreshList();
                break;
            case R.id.tab_share_search:
                selectTab(1);
                ShareSearchFragment fgSearch = (ShareSearchFragment)mFragments.get(1);
                fgSearch.refreshList();
                break;
            case R.id.tab_share_friend:
                selectTab(2);
                break;
            case R.id.tab_share_profile:
                selectTab(3);
                break;
            case R.id.button_send:
                Log.v("button_test","button_effect");
                Toast.makeText(this, "choose a photo", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE);
//                startActivity(new Intent(this,ShareEditActivity.class));
                break;
        }
    }

    private void selectTab(int i) {
        //根据点击的Tab设置对应的ImageButton为绿色
        switch (i) {
            case 0:
                mImgHome.setImageResource(R.drawable.button_tab_home_b);
                mImgSearch.setImageResource(R.drawable.button_tab_search_a);
                mImgFriend.setImageResource(R.drawable.button_tab_recent_a);
                mImgProfile.setImageResource(R.drawable.button_tab_profile_a);
                break;
            case 1:
                mImgHome.setImageResource(R.drawable.button_tab_home_a);
                mImgSearch.setImageResource(R.drawable.button_tab_search_b);
                mImgFriend.setImageResource(R.drawable.button_tab_recent_a);
                mImgProfile.setImageResource(R.drawable.button_tab_profile_a);
                break;
            case 2:
                mImgHome.setImageResource(R.drawable.button_tab_home_a);
                mImgSearch.setImageResource(R.drawable.button_tab_search_a);
                mImgFriend.setImageResource(R.drawable.button_tab_recent_b);
                mImgProfile.setImageResource(R.drawable.button_tab_profile_a);
                break;
            case 3:
                mImgHome.setImageResource(R.drawable.button_tab_home_a);
                mImgSearch.setImageResource(R.drawable.button_tab_search_a);
                mImgFriend.setImageResource(R.drawable.button_tab_recent_a);
                mImgProfile.setImageResource(R.drawable.button_tab_profile_b);
                break;
        }
        //设置当前点击的Tab所对应的页面
        mViewPager.setCurrentItem(i);
    }

    //将四个ImageButton设置为灰色
    private void resetImgs() {
        mImgHome.setImageResource(R.drawable.tab_home_a);
        mImgFriend.setImageResource(R.drawable.tab_recent_a);
        mImgSearch.setImageResource(R.drawable.tab_search_a);
        mImgProfile.setImageResource(R.drawable.tab_profile_a);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
//            showImage(imagePath);
            c.close();


            Intent intent=new Intent();
            intent.setClass(this,ShareEditActivity.class);
            intent.putExtra("path", imagePath);
            startActivity(intent);

        }


    }


}
