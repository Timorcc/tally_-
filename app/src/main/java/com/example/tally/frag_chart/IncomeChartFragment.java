package com.example.tally.frag_chart;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.tally.R;
import com.example.tally.adapter.ChartItemAdapter;
import com.example.tally.db.ChartItemBean;
import com.example.tally.db.DBManager;

import java.util.ArrayList;
import java.util.List;

public class IncomeChartFragment extends Fragment {

    ListView chartLv;
    private int year;
    private int month;
    List<ChartItemBean> mDatas;//数据源
    private ChartItemAdapter chartItemAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income_chart, container, false);
        chartLv = view.findViewById(R.id.frag_chart_lv);
        //获取Activity传递的数据
        Bundle bundle = getArguments();
        year = bundle.getInt("year");
        month = bundle.getInt("month");
        //设置数据源
        mDatas = new ArrayList<>();
        //设置适配器
        chartItemAdapter = new ChartItemAdapter(getContext(), mDatas);
        chartLv.setAdapter(chartItemAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDate(year, month, 1);
    }

    public void loadDate(int year, int month, int kind) {

        List<ChartItemBean> list = DBManager.getChartListFromAccounttb(year, month, kind);
        mDatas.clear();
        mDatas.addAll(list);
        startPostponedEnterTransition();
    }
}
