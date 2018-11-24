package com.hackerkernel.user.sqrfactor;


import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TabHost;

import static com.hackerkernel.user.sqrfactor.R.color.white;
import static com.hackerkernel.user.sqrfactor.R.color.yellow;


public class PostActivity extends TabActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;




    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setTitle("Post");
        toolbar.setTitleTextColor(R.color.White);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TabHost tabHost = getTabHost();

        // Tab for Photos
        TabHost.TabSpec photospec = tabHost.newTabSpec("Status");
        // setting Title and Icon for the Tab
        photospec.setIndicator("Status", getResources().getDrawable(R.drawable.status));
        Intent intent = new Intent(this, StatusPostActivity.class);
        intent.putExtra("FromText","FormText");
        photospec.setContent(intent);

        // Tab for Songs
        TabHost.TabSpec songspec = tabHost.newTabSpec("Design");
        songspec.setIndicator("Design", getResources().getDrawable(R.drawable.design));
        Intent intent1 = new Intent(this, DesignActivity.class);
        intent.putExtra("FromText","FormText");
        songspec.setContent(intent1);

        // Tab for Videos
        TabHost.TabSpec videospec = tabHost.newTabSpec("Article");
        videospec.setIndicator("Article", getResources().getDrawable(R.drawable.article));
        Intent intent2 = new Intent(this, ArticleActivity.class);
        intent.putExtra("FromText","FormText");
        videospec.setContent(intent2);

        // Adding all TabSpec to TabHost
        tabHost.addTab(photospec); // Adding photos tab
        tabHost.addTab(songspec); // Adding songs tab
        tabHost.addTab(videospec); // Adding videos tab
    }

//
//        viewPager = (ViewPager) findViewById(R.id.post_viewPager);
//        setupViewPager(viewPager);
//
//        tabLayout = (TabLayout) findViewById(R.id.post_tabs);
//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @SuppressLint("ResourceAsColor")
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//        tabLayout.setupWithViewPager(viewPager);
//        setupTabIcons();
//
//    }
//    private void setupTabIcons() {
//        tabLayout.getTabAt(0).setIcon(R.drawable.status);
//        tabLayout.getTabAt(1).setIcon(R.drawable.design);
//        tabLayout.getTabAt(2).setIcon(R.drawable.article);
//    }
//    private void setupViewPager(ViewPager viewPager) {
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(new StatusPostFragment(),"Status");
//        adapter.addFragment(new DesignFragment(), "Design");
//        adapter.addFragment(new ArticleFragment(), "Article");
//        viewPager.setAdapter(adapter);
//    }
//
//    class ViewPagerAdapter extends FragmentPagerAdapter {
//        private final List<Fragment> mFragmentList = new ArrayList<>();
//        private final List<String> mFragmentTitleList = new ArrayList<>();
//
//        public ViewPagerAdapter(FragmentManager manager) {
//            super(manager);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return mFragmentList.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return mFragmentList.size();
//        }
//
//        public void addFragment(Fragment fragment, String title) {
//            mFragmentList.add(fragment);
//            mFragmentTitleList.add(title);
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
//        }
//    }

    @Override
        public void finish () {
            super.finish();
            overridePendingTransition(R.anim.slide_from_top, R.anim.slide_in_top);

        }

}
