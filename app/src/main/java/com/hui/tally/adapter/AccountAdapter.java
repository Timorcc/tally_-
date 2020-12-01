package com.hui.tally.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hui.tally.R;
import com.hui.tally.db.AccountBean;

import java.util.Calendar;
import java.util.List;


/**
 * @author  lga
 * 创建日期： 2020/12/1 17:36
 * 描述：
 * 总结一下用ViewHolder优化BaseAdapter的整体步骤：
 *
 * >1 创建bean对象，用于封装数据；
 *
 * >2 在构造方法中初始化的数据List；
 *
 * >3 创建ViewHolder类，创建布局映射关系；
 *
 * >4 判断convertView，为空则创建，并设置tag，不为空则通过tag取出ViewHolder；
 *
 * >5 给ViewHolder的控件设置数据。
 *
 */
public class AccountAdapter extends BaseAdapter {
    Context context;
    List<AccountBean> mDatas;
    LayoutInflater inflater;
    int year, month, day;

    public AccountAdapter(Context context, List<AccountBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        inflater = LayoutInflater.from(context);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        //如果view未被实例化过，缓存池中没有对应的缓存
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_mainlv, parent, false);
            holder = new ViewHolder(convertView);
            //通过setTag将convertView与viewHolder关联
            convertView.setTag(holder);
        } else {
            //如果缓存池中有对应的view缓存，则直接通过getTag取出viewHolder
            holder = (ViewHolder) convertView.getTag();
        }
        AccountBean bean = mDatas.get(position);
        holder.typeIv.setImageResource(bean.getsImageId());
        holder.typeTv.setText(bean.getTypename());
        holder.beizhuTv.setText(bean.getBeizhu());
        holder.moneyTv.setText("￥ " + bean.getMoney());
        if (bean.getYear() == year && bean.getMonth() == month && bean.getDay() == day) {
            String time = bean.getTime().split(" ")[1];
            holder.timeTv.setText("今天 " + time);
        } else {
            holder.timeTv.setText(bean.getTime());
        }
        return convertView;
    }

    // ViewHolder用于缓存控件，属性分别对应item布局文件的控件。创建布局映射关系
    class ViewHolder {
        ImageView typeIv;
        TextView typeTv, beizhuTv, timeTv, moneyTv;

        public ViewHolder(View view) {
            typeIv = view.findViewById(R.id.item_mainlv_iv);
            typeTv = view.findViewById(R.id.item_mainlv_tv_title);
            timeTv = view.findViewById(R.id.item_mainlv_tv_time);
            beizhuTv = view.findViewById(R.id.item_mainlv_tv_beizhu);
            moneyTv = view.findViewById(R.id.item_mainlv_tv_money);

        }
    }
}
