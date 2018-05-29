package com.shallowan.milutv.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.lzy.widget.AlphaIndicator;
import com.sdsmdg.tastytoast.TastyToast;
import com.shallowan.milutv.R;
import com.shallowan.milutv.createroom.CreateLiveActivity;
import com.shallowan.milutv.editprofile.EditProfileFragment;
import com.shallowan.milutv.livelist.LiveListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShallowAn.
 */

public class MainActivity extends AppCompatActivity {

    private long mExitTime;
    private SpaceNavigationView spaceNavigationView;
    private AlphaIndicator alphaIndicator;
    private FrameLayout mContainer;
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findAllViews();
        setTabs();

        //replaceFragment(new LiveListFragment());
//        spaceNavigationView = findViewById(R.id.space);
//        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
//        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.tab_home_unselected));
//        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.tab_profile_unselected));
//
//        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
//            @Override
//            public void onCentreButtonClick() {
//                //跳转到创建直播的页面。
//                Intent intent = new Intent(MainActivity.this, CreateLiveActivity.class);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onItemClick(int itemIndex, String itemName) {
//                if (itemIndex == 0) {
//                    replaceFragment(new LiveListFragment());
//                }
//
//
//                if (itemIndex == 1) {
//                    replaceFragment(new EditProfileFragment());
//                }
//            }
//
//            @Override
//            public void onItemReselected(int itemIndex, String itemName) {
//
//            }
//
//
//        });
    }

    private void setTabs() {
        mTabHost.setup(this, getSupportFragmentManager(), R.id.fragment_container);

        //添加fragment.

        {
            TabHost.TabSpec profileTab = mTabHost.newTabSpec("livelist").setIndicator(getIndicator(R.drawable.tab_livelist));
            mTabHost.addTab(profileTab, LiveListFragment.class, null);
            mTabHost.getTabWidget().setDividerDrawable(null);
        }


        {
            TabHost.TabSpec profileTab = mTabHost.newTabSpec("profile").setIndicator(getIndicator(R.drawable.tab_profile));
            mTabHost.addTab(profileTab, EditProfileFragment.class, null);
            mTabHost.getTabWidget().setDividerDrawable(null);
        }
    }

    private View getIndicator(int resId) {
        View tabView = LayoutInflater.from(this).inflate(R.layout.view_indicator, null);
        ImageView tabImg = tabView.findViewById(R.id.tab_icon);
        tabImg.setImageResource(resId);
        return tabView;
    }

    private void findAllViews() {
        mContainer = findViewById(R.id.fragment_container);
        mTabHost = findViewById(R.id.fragment_tabhost);
    }

//    private void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragment_container, fragment);
//        fragmentTransaction.commit();
//    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            TastyToast.makeText(getApplicationContext(), "再按一次退出", TastyToast.LENGTH_LONG, TastyToast.WARNING);
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
}
