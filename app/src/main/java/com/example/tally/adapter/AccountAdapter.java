package com.example.tally.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tally.R;
import com.example.tally.db.AccountBean;

import java.util.Calendar;
import java.util.List;

public class AccountAdapter extends BaseAdapter {
    Context context;
    List<AccountBean> mDates;
    LayoutInflater inflater;
    int year, month, day;

    public AccountAdapter(Context context, List<AccountBean> mDates) {
        this.context = context;
        this.mDates = mDates;
        this.inflater = LayoutInflater.from(context);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

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
        ViewHoler holer = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_mainlv, parent, false);
            holer = new ViewHoler(convertView);
            convertView.setTag(holer);
        } else {
            holer = (ViewHoler) convertView.getTag();
        }
        AccountBean bean = mDates.get(position);
        holer.typeIv.setImageResource(bean.getsImageId());
        holer.typeTv.setText(bean.getTypeName());
        holer.beizhuTv.setText(bean.getBeizhu());
        holer.timeTv.setText(bean.getTime());
        holer.moneyTv.setText("￥"+bean.getMoney());

        if (bean.getYear() == year && bean.getMoney() == month && bean.getDay() == day) {
            String time = bean.getTime().split(" ")[1];
            holer.timeTv.setText("今天 " + time);
        } else {
            holer.timeTv.setText(bean.getTime());
        }
        return convertView;
    }

    class ViewHoler {
        ImageView typeIv;
        TextView typeTv, beizhuTv, timeTv, moneyTv;

        public ViewHoler(View view) {
            typeIv = view.findViewById(R.id.item_mainlv_iv);
            typeTv = view.findViewById(R.id.item_mainlv_title);
            beizhuTv = view.findViewById(R.id.item_mainlv_beizhu);
            timeTv = view.findViewById(R.id.item_mainlv_time);
            moneyTv = view.findViewById(R.id.item_mainlv_money);

        }
    }
}
