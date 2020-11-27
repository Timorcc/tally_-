package com.example.tally;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tally.adapter.AccountAdapter;
import com.example.tally.db.AccountBean;
import com.example.tally.db.DBManager;
import com.example.tally.utils.CalendarDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    ListView historyLv;
    TextView timeTv;
    List<AccountBean> mDates;
    AccountAdapter adapter;
    int year, month;
    int dialogSelPos = -1;
    int dialogSelMonth = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initView();
        initTime();
        loadDate(year, month);
        setLvClickListener();
    }

    //设置list view每一个item的长安事件
    private void setLvClickListener() {
        historyLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AccountBean accountBean = mDates.get(position);
                deleteItem(accountBean);
                return false;
            }
        });
    }

    private void deleteItem(final AccountBean accountBean) {
        final int id = accountBean.getId();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息").setMessage("确定要删除吗？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBManager.deleteItemFromAccounttbById(id);
                        mDates.remove(accountBean);//实时刷新
                        adapter.notifyDataSetChanged();
                    }
                });
        builder.create().show();

    }

    //获取指定年份月份收支情况的列表
    private void loadDate(int year, int month) {
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
                final CalendarDialog dialog = new CalendarDialog(this, dialogSelPos, dialogSelMonth);
                dialog.show();
                dialog.setDialogSize();
                dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
                    @Override
                    public void onRefresh(int selPos, int year, int month) {
                        timeTv.setText(year + "年" + month + "月");
                        loadDate(year, month);
                        dialogSelPos = selPos;
                        dialogSelMonth = month;
                    }
                });
                break;
        }
    }
}
