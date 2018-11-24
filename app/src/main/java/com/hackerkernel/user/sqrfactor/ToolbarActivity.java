package com.hackerkernel.user.sqrfactor;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class ToolbarActivity extends AppCompatActivity {

    Toolbar toolbar;
    //TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);

        /*tabLayout = (TabLayout)findViewById(R.id.tabs);

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.trophy_filled));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.msg));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.notification));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_search_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_menu_black_24dp));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){

                    case 0:
                        tab.setIcon(R.drawable.trophy_filled);
                        break;

                    case 1:
                        tab.setIcon(R.drawable.envelope_filled);
                        Intent i1 = new Intent(getApplicationContext(), MessagesActivity.class);
                        startActivity(i1);
                        break;

                    case 2:
                        tab.setIcon(R.drawable.notification_filled);
                        Intent i2 = new Intent(getApplicationContext(), MessagesActivity.class);
                        startActivity(i2);
                        break;

                    case 3:
                        Intent i3 = new Intent(getApplicationContext(), MessagesActivity.class);
                        startActivity(i3);
                        break;

                    case 4:
                        Intent i4 = new Intent(getApplicationContext(), MessagesActivity.class);
                        startActivity(i4);
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                switch (tab.getPosition()){

                    case 0:
                        tab.setIcon(R.drawable.trophy);
                        break;

                    case 1:
                        tab.setIcon(R.drawable.msg);
                        break;

                    case 2:
                        tab.setIcon(R.drawable.notification);
                        break;

                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {



            }
        });*/

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    /*@Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        getMenuInflater().inflate(R.menu.options, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView)search.getActionView();

        return super.onCreateOptionsMenu(menu);

    }

    int flag = 0;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.profile:
                if (flag==0){

                    getMenuInflater().inflate(R.menu.drawer_options, item.getSubMenu());
                    flag = 1;

                    Menu submenu = item.getSubMenu();
                    submenu.findItem(R.id.avatar).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if (item.getItemId()==R.id.avatar){

                                Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                                startActivity(i);

                            }
                            return true;

                        }
                    });

                }
            break;
        }
        return true;
    }*/
}
