package com.hackerkernel.user.sqrfactor.Fragments;


import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hackerkernel.user.sqrfactor.R;
import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {
    TabLayout mTabLayout;
    ViewPager mPager;
    private MySharedPreferences mSp;


    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        mSp = MySharedPreferences.getInstance(getActivity());
        mTabLayout = view.findViewById(R.id.competition_tablayout);
        mPager = view.findViewById(R.id.competition_pager);

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getFragmentManager());
        mPager.setAdapter(adapter);

        mTabLayout.setupWithViewPager(mPager);
        mTabLayout.setSelected(true);
        mTabLayout.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.sqr)));

        return view;

    }
    private class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){

                case 0:
                    return new CurrentCompetition();

                case 1:
                    return new PastCompetition();

            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){

                case 0:
                    return "Current Competition";

                case 1:
                    return "Past Competition";
            }
            return null;
        }
    }

}
