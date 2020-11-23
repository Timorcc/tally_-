package com.example.tally;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.tally.adapter.AccountAdapter;
import com.example.tally.db.AccountBean;
import com.example.tally.db.DBManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ListView todayLv;//展示进入收支情况的listview
    List<AccountBean> mDates;
    private AccountAdapter adapter;
    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTime();
        todayLv = findViewById(R.id.main_lv);
        mDates = new ArrayList<>();
        //设置适配器，加载每一行数据到列表中
        adapter = new AccountAdapter(this, mDates);
        todayLv.setAdapter(adapter);

    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

    }

    //
//   当Activity获取焦点时
    @Override
    protected void onResume() {
        super.onResume();
        loadDBDate();
    }

    private void loadDBDate() {
        List<AccountBean> list = DBManager.getAccountListOneDayFormAccounttb(year, month, day);
        mDates.clear();
        mDates.addAll(list);
        adapter.notifyDataSetChanged();

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_iv_search:
                break;
            case R.id.main_btn_edit:
                Intent it1 = new Intent(this, RecordActivity.class);
                startActivity(it1);

                break;
            case R.id.main_btn_more:

                break;
        }
    }
}
