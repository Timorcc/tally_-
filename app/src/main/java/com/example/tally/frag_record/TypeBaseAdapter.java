package com.example.tally.frag_record;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tally.R;
import com.example.tally.db.TypeBean;

import java.util.List;

public class TypeBaseAdapter extends BaseAdapter {
    Context context;//所在的Activity
    List<TypeBean> mDates;

    int selectPos = 0;//本次点击位置

    public TypeBaseAdapter(Context context, List<TypeBean> mDates) {
        this.context = context;
        this.mDates = mDates;
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

    //这个适配器不考虑复用，所有item都显示在界面上，不会因为滑动消失，所以没有多余的converView ，所以不复写
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.item_recordfrag_gv, parent, false);
        //查找布局控件
        ImageView iv = convertView.findViewById(R.id.item_recordfrag_iv);
        TextView tv = convertView.findViewById(R.id.item_recordfrag_tv);

        //获取指定位置数据元
        TypeBean typeBean = mDates.get(position);
        tv.setText(typeBean.getTypename());
        //判断当前位置是否为选择位置，是为带颜色的图片，否为不带颜色
        if (selectPos == position) {
            iv.setImageResource(typeBean.getsImageId());
        } else {
            iv.setImageResource(typeBean.getImageId());
        }
        return convertView;
    }
}
