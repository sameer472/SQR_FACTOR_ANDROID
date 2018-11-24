package com.hackerkernel.user.sqrfactor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class Index extends AppCompatActivity {

    private ListView listView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView)findViewById(R.id.indexList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position){

                    case 0:
                        Intent i1 = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i1);
                        break;

                    case 1:
                        Intent i2 = new Intent(getApplicationContext(), LoginScreen.class);
                        startActivity(i2);
                        finish();
                        break;

                    case 2:
                        Intent i3 = new Intent(getApplicationContext(), HomeScreen.class);
                        startActivity(i3);
                        break;

                    case 3:
                        Intent i4 = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(i4);
                        break;

                    case 4:
                        Intent i5 = new Intent(getApplicationContext(), RedActivity.class);
                        startActivity(i5);
                        break;

                    case 5:
                        Intent i6 = new Intent(getApplicationContext(), DesignActivity.class);
                        startActivity(i6);
                        break;

                    case 6:
                        Intent i7 = new Intent(getApplicationContext(), ArticleActivity.class);
                        startActivity(i7);
                        break;




                }

            }
        });

    }
}
