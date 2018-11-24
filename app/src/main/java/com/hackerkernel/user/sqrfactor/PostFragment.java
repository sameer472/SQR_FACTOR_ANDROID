package com.hackerkernel.user.sqrfactor;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

public class PostFragment extends Fragment {

    TabLayout tabLayout1;
    ImageView btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmen

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_post, container, false);

        btn = (ImageView)view.findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pop = new PopupMenu(getContext(), v);
                pop.getMenuInflater().inflate(R.menu.home_menu, pop.getMenu());
                pop.show();

                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){

                            case R.id.comp:
                                //Intent i = new Intent(getContext(), LaunchCompetition.class);
                                //startActivity(i);
                                return true;

                        }
                        return true;
                    }
                });

            }
        });

        tabLayout1 = (TabLayout) view.findViewById(R.id.tabs2);

        tabLayout1.addTab(tabLayout1.newTab().setIcon(R.drawable.status)
                .setText("Status"));
        tabLayout1.addTab(tabLayout1.newTab().setIcon(R.drawable.design)
                .setText("Design"));
        tabLayout1.addTab(tabLayout1.newTab().setIcon(R.drawable.article)
                .setText("Article"));

        tabLayout1.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {

                    case 1:
                        Intent i2 = new Intent(getContext(), DesignActivity.class);
                        startActivity(i2);
                        break;

                    case 2:
                        Intent i3 = new Intent(getContext(), ArticleActivity.class);
                        startActivity(i3);
                        break;

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

}
