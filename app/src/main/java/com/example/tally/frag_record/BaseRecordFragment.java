package com.example.tally.frag_record;


import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tally.R;
import com.example.tally.db.AccountBean;
import com.example.tally.db.DBManager;
import com.example.tally.db.TypeBean;
import com.example.tally.utils.KeyBoardUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 记录页面当中的支出模块
 */
public abstract class BaseRecordFragment extends Fragment {

    KeyboardView keyboardView;
    EditText moneyEt;
    ImageView typeIv;
    TextView typeTv, beizhuTv, timeTv;
    GridView typeGv;
    List<TypeBean> typeList;
    TypeBaseAdapter adapter;
    AccountBean accountBean;//将需要插入记账本的数据保存对象形式

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountBean = new AccountBean();
        accountBean.setTypeName("其他");
        accountBean.setsImageId(R.mipmap.ic_qita_fs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_outcome, container, false);
        initView(view);
        //初始化时间
        setInitTime();
        //给GridView填充数据的方法
        loadDataToGV();
        //设置GridView点击事件
        setGVListener();
        return view;
    }

    /*获取当前时间，显示在timeTV上*/
    private void setInitTime() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = format.format(date);
        timeTv.setText(time);
        accountBean.setTime(time);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        accountBean.setYear(year);
        accountBean.setMonth(month);
        accountBean.setDay(day);

    }

    //设置GridView点击事件
    private void setGVListener() {
        typeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.selectPos = position;
                //提示绘制发生了变化
                adapter.notifyDataSetInvalidated();
                //修改上边的文字和图片
                TypeBean typeBean = typeList.get(position);
                String typename = typeBean.getTypename();
                typeTv.setText(typename);
                accountBean.setTypeName(typename);
                int imageId = typeBean.getsImageId();
                typeIv.setImageResource(imageId);
                accountBean.setsImageId(imageId);
            }
        });

    }

    //给GridView填充数据的方法
    public void loadDataToGV() {
        typeList = new ArrayList<>();
        adapter = new TypeBaseAdapter(getContext(), typeList);
        typeGv.setAdapter(adapter);

    }

    private void initView(View view) {
        keyboardView = view.findViewById(R.id.frag_record_keyboard);
        moneyEt = view.findViewById(R.id.frag_record_et_money);
        typeIv = view.findViewById(R.id.frag_record_iv);
        typeGv = view.findViewById(R.id.frag_record_gv);
        typeTv = view.findViewById(R.id.frag_record_tv_type);
        beizhuTv = view.findViewById(R.id.frag_record_tv_beizhu);
        timeTv = view.findViewById(R.id.frag_record_tv_time);
        //显示自定义键盘
        KeyBoardUtils keyBoardUtils = new KeyBoardUtils(keyboardView, moneyEt);
        keyBoardUtils.showKeyboard();
        //设置接口，监听确定按钮被点击了
        keyBoardUtils.setOnEnsureListener(new KeyBoardUtils.OnEnsureListener() {
            @Override
            public void onEnsure() {
                //获取输入钱数
                String moneyStr = moneyEt.getText().toString();
                if (TextUtils.isEmpty(moneyStr) || moneyStr.equals("0")) {
                    getActivity().finish();
                    return;
                }
                float money = Float.parseFloat(moneyStr);
                accountBean.setMoney(money);
                //获取记录的信息，保存在数据库中
                saveAccountToDB();
                //返回上一级页面
                getActivity().finish();
            }
        });
    }

    /*让子类一定要重写这个方法*/
    public abstract void saveAccountToDB();

}
