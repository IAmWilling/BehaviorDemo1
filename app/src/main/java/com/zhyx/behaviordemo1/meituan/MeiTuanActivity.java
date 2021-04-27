package com.zhyx.behaviordemo1.meituan;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.zhyx.behaviordemo1.R;

import java.util.ArrayList;
import java.util.List;

public class MeiTuanActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    MTScrollLayout mtScrollLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.setStatusBarTransparent(this,false,false);
        setContentView(R.layout.activity_main_2);
        tabLayout = findViewById(R.id.tablayout);
        viewPager2 = findViewById(R.id.viewpager2);
        mtScrollLayout = findViewById(R.id.mt_scrolllayout);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new DianCaiFragment());
        fragments.add(new PingLunragment());
        fragments.add(new ShangJiaFragment());
        viewPager2.setAdapter(new ViewPager2Adapter(this,fragments));
        initListener();

    }
    public boolean canSlide(){
        return mtScrollLayout.isCanChildSlide();
    }


    private void initListener(){
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                tabLayout.setScrollPosition(position,positionOffset,true);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition(),true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition(),true);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition(),true);
            }
        });
    }
}