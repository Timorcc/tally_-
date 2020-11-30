package com.example.tally;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tally.adapter.CharVPAdapter;
import com.example.tally.db.DBManager;
import com.example.tally.frag_chart.IncomeChartFragment;
import com.example.tally.frag_chart.OutcomeChartFragment;
import com.example.tally.utils.CalendarDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthChartActivity extends AppCompatActivity {

    Button inBtn, outBtn;
    TextView dateTv, inTv, ouTv;
    ViewPager chartVp;
    int year, month;
    int selctPos = -1, selctMonth = -1;
    List<Fragment> charFragList;
    private IncomeChartFragment incomeChartFragment;
    private OutcomeChartFragment outcomeChartFragment;
    private CharVPAdapter charVPAdapter;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_chart);
        initView();
        initTime();
        initStatistics(year, month);
        initFrag();

    }

    private void initFrag() {
        charFragList = new ArrayList<>();
        //添加Fragment的对象
        incomeChartFragment = new IncomeChartFragment();
        outcomeChartFragment = new OutcomeChartFragment();
        //添加数据到Fragment中
        Bundle bundle = new Bundle();
        bundle.putInt("year", year);
        bundle.putInt("month", month);
        incomeChartFragment.setArguments(bundle);
        outcomeChartFragment.setArguments(bundle);
        //将Fragment添加到数据源中
        charFragList.add(outcomeChartFragment);
        charFragList.add(incomeChartFragment);
        //使用适配器
        charVPAdapter = new CharVPAdapter(getSupportFragmentManager(), charFragList);
        chartVp.setAdapter(charVPAdapter);
        //将fragment加载到activity中


    }

    //初始化某年某月的收支情况
    private void initStatistics(int year, int month) {
        float inMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);
        float outMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
        int outcountItemOneMonth = DBManager.getCountItemOneMonth(year, month, 0);
        int incountItemOneMonth = DBManager.getCountItemOneMonth(year, month, 1);
        dateTv.setText(year + "年" + month + "月账单");
        inTv.setText("共" + incountItemOneMonth + "笔收入，￥" + inMoneyOneMonth);
        ouTv.setText("共" + outcountItemOneMonth + "笔支出，￥" + outMoneyOneMonth);

    }

    //初始化时间
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;


    }


    //初始化控件
    private void initView() {
        inBtn = findViewById(R.id.char_btn_in);
        outBtn = findViewById(R.id.char_btn_out);
        dateTv = findViewById(R.id.chart_tv_date);
        inTv = findViewById(R.id.chart_tv_in);
        ouTv = findViewById(R.id.chart_tv_out);
        chartVp = findViewById(R.id.chart_vp);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chart_iv_back:
                finish();
                break;
            case R.id.chart_iv_rili:
                showCalendarDialog();
                break;
            case R.id.char_btn_out:
                setButtonStyle(0);
                chartVp.setCurrentItem(0);
                break;
            case R.id.char_btn_in:
                setButtonStyle(1);
                chartVp.setCurrentItem(1);
                break;
        }
    }

    private void showCalendarDialog() {
        CalendarDialog dialog = new CalendarDialog(this, selctPos, selctMonth);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
            @Override
            public void onRefresh(int selPos, int year, int month) {
                MonthChartActivity.this.selctPos = selctPos;
                MonthChartActivity.this.selctMonth = month;
                initStatistics(year, month);
            }
        });
    }

    //    设置按钮样式改变
    public void setButtonStyle(int kind) {
        if (kind == 0) {
            outBtn.setBackgroundResource(R.drawable.main_recordbtn_bg);
            outBtn.setTextColor(Color.WHITE);
            inBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
            inBtn.setTextColor(Color.BLACK);
        } else if (kind == 1) {
            inBtn.setBackgroundResource(R.drawable.main_recordbtn_bg);
            inBtn.setTextColor(Color.WHITE);
            outBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
            outBtn.setTextColor(Color.BLACK);
        }
    }
}
