package com.example.tally.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tally.R;

import java.util.ArrayList;
import java.util.List;

//历史账单页面，点击日历表，弹出对话框，但中的GridView的适配器
public class CalendarAdapter extends BaseAdapter {
    Context context;
    List<String> mDates;
    public int year;
    public int selPos = -1;

    public void setYear(int year) {
        this.year = year;
        mDates.clear();
        loadDate(year);
        notifyDataSetChanged();
    }

    public CalendarAdapter(Context context, int year) {
        this.context = context;
        this.year = year;
        mDates = new ArrayList<>();
        loadDate(year);
    }

    private void loadDate(int year) {
        for (int i = 1; i < 13; i++) {
            String data = year + "/" + i;
            mDates.add(data);
        }
    }

    @Override
    public int getCount() {
        return mDates.size();
    }

    @Override
    public Object getItem(int position) {
        return mDates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_dialogcal_gv, parent, false);
        TextView tv = convertView.findViewById(R.id.item_dialogcal_gv_tv);
        tv.setText(mDates.get(position));
        tv.setBackgroundResource(R.color.grey_f3f3f3);
        tv.setTextColor(Color.BLACK);
        if (position == selPos) {
            tv.setBackgroundResource(R.color.green_006400);
            tv.setTextColor(Color.WHITE);
        }
        return convertView;
    }
}
