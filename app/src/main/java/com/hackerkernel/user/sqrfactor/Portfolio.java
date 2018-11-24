package com.hackerkernel.user.sqrfactor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class Portfolio extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<PortfolioClass> portfolioArrayList=new ArrayList<>();


    public Portfolio() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_portfolio, container, false);

        recyclerView=view.findViewById(R.id.recyclerView_portfolio);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        PortfolioAdapter portfolioAdapter = new PortfolioAdapter(portfolioArrayList,getActivity());

        recyclerView.setAdapter(portfolioAdapter);
        return view;

    }

}
