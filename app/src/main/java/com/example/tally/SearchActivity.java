package com.example.tally;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tally.adapter.AccountAdapter;
import com.example.tally.db.AccountBean;
import com.example.tally.db.DBManager;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    ListView searchLv;
    EditText searchEt;
    TextView emptyTv;

    List<AccountBean> mDates;   //数据源
    AccountAdapter adapter;     //适配器对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        mDates = new ArrayList<>();
        adapter = new AccountAdapter(this, mDates);
        searchLv.setAdapter(adapter);
        searchLv.setEmptyView(emptyTv); //设置无数据时显示的控件
    }

    private void initView() {
        searchLv = findViewById(R.id.search_Lv);
        searchEt = findViewById(R.id.search_et);
        emptyTv = findViewById(R.id.search_tv_empty);


    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_iv_back:
                finish();
                break;
            case R.id.search_iv_sh://执行搜索
                String msg = searchEt.getText().toString().trim();
                //判断输入是否为空，如果为空，提示不能搜索
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(SearchActivity.this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    //开始查询
                    List<AccountBean> list = DBManager.getAccountListByRemarkFromAccounttb(msg);
                    mDates.clear();
                    mDates.addAll(list);
                    adapter.notifyDataSetChanged();
                }

                break;

        }
    }
}
