package com.example.tally;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tally.adapter.AccountAdapter;
import com.example.tally.db.AccountBean;
import com.example.tally.db.DBManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    ListView historyLv;
    TextView timeTv;
    List<AccountBean> mDates;
    AccountAdapter adapter;
    int year, month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initView();
        initTime();
        loadDate();
    }

    //获取指定年份月份收支情况的列表
    private void loadDate() {
        List<AccountBean> list = DBManager.getAccountListOneMonthFormAccounttb(year, month);
        mDates.clear();
        mDates.addAll(list);
        adapter.notifyDataSetChanged();
    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        timeTv.setText(year + "年" + month + "月");
    }

    private void initView() {
        historyLv = findViewById(R.id.history_lv);
        timeTv = findViewById(R.id.history_tv_time);
        //设置适配器
        mDates = new ArrayList<>();
        adapter = new AccountAdapter(this, mDates);
        historyLv.setAdapter(adapter);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.history_iv_back:
                finish();
                break;
            case R.id.history_rili:
                break;
        }
    }
}
