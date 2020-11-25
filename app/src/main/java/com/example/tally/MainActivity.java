package com.example.tally;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tally.adapter.AccountAdapter;
import com.example.tally.db.AccountBean;
import com.example.tally.db.DBManager;
import com.example.tally.utils.BudgetDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ListView todayLv;//展示进入收支情况的listview
    List<AccountBean> mDates;
    ImageView searchIv;
    Button editBtn;
    ImageButton moreBtn;
    private AccountAdapter adapter;
    int year, month, day;
    //头布局相关空间
    View headerView;
    TextView topOutTv, topInTv, topbudgetTv, topConTv;
    ImageView topShowIv;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTime();
        initView();
        preferences = getSharedPreferences("budget", Context.MODE_PRIVATE);

        //添加ListView头布局

        AddLVHeaderView();

        mDates = new ArrayList<>();
        //设置适配器，加载每一行数据到列表中
        adapter = new AccountAdapter(this, mDates);
        todayLv.setAdapter(adapter);

    }

    //初始化自带的view的方法
    private void initView() {
        todayLv = findViewById(R.id.main_lv);
        editBtn = findViewById(R.id.main_btn_edit);
        moreBtn = findViewById(R.id.main_btn_more);
        searchIv = findViewById(R.id.main_iv_search);

        editBtn.setOnClickListener(this);
        moreBtn.setOnClickListener(this);
        searchIv.setOnClickListener(this);
        setLVLongClickListener();
    }

    //设置listview的长按事件
    private void setLVLongClickListener() {
        todayLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    //点击了头布局
                    return false;
                }
                int pos = position - 1;
                AccountBean clickBean = mDates.get(pos);//获取正在被点击的这条信息

                //弹出用户是否删除的对话框
                showDeleteItemDialog(clickBean);

                return false;
            }
        });
    }

    //弹出是否删除某一条记录的对话框
    private void showDeleteItemDialog(final AccountBean clickBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息").setMessage("确定要删除这条记录吗").setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int click_id = clickBean.getId();
                        //执行删除的操作
                        DBManager.deleteItemFromAccounttbById(click_id);
                        mDates.remove(clickBean);//实施刷新
                        adapter.notifyDataSetChanged();//提示适配器而更新数据
                        setTopTvShow();//该表头布局显示的内容
                    }
                });
        builder.create().show();//显示对话框
    }


    //给listview添加头布局
    private void AddLVHeaderView() {
        //将布局转化为view对象
        headerView = getLayoutInflater().inflate(R.layout.item_mainlv_top, null);
        todayLv.addHeaderView(headerView);
        //查找头布局可用控件
        topOutTv = headerView.findViewById(R.id.item_mainlv_top_tv_out);
        topInTv = headerView.findViewById(R.id.item_mainlv_top_tv_in);
        topbudgetTv = headerView.findViewById(R.id.item_mainlv_top_tv_budget);
        topConTv = headerView.findViewById(R.id.item_mainlv_top_tv_day);
        topShowIv = headerView.findViewById(R.id.item_mainlv_iv_hide);

        topbudgetTv.setOnClickListener(this);
        headerView.setOnClickListener(this);
        topShowIv.setOnClickListener(this);

    }

    //获取今日的具体时间
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

    }

    //当Activity获取焦点时
    @Override
    protected void onResume() {
        super.onResume();
        loadDBDate();
        setTopTvShow();
    }

    //设置头布局当中文本内容的显示
    private void setTopTvShow() {
        //获取今日支出和收入总金额，显示在view当中
        float incomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 1);
        float outcomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 0);
        String infoOneDay = "今日支出 ￥" + outcomeOneDay + " 收入 ￥" + incomeOneDay;
        topConTv.setText(infoOneDay);
        //获取本月收入和支出总金额
        float incomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);
        float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
        topOutTv.setText("￥ " + outcomeOneMonth);
        topInTv.setText("￥ " + incomeOneMonth);
        //设置显示剩余预算
        float bmoney = preferences.getFloat("bmoney", 0);//预算
        if (bmoney == 0) {
            topbudgetTv.setText("￥ 0");
        } else {
            float syMoney = bmoney - outcomeOneMonth;
            topbudgetTv.setText("￥" + syMoney);
        }

    }

    private void loadDBDate() {
        List<AccountBean> list = DBManager.getAccountListOneDayFormAccounttb(year, month, day);
        mDates.clear();
        mDates.addAll(list);
        adapter.notifyDataSetChanged();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_iv_search:
                break;
            case R.id.main_btn_edit:
                Intent it1 = new Intent(this, RecordActivity.class);
                startActivity(it1);
                break;
            case R.id.main_btn_more:
                break;
            case R.id.item_mainlv_top_tv_budget:
                showBudgetDialog();
                break;

            case R.id.item_mainlv_iv_hide:
                //切换testview铭文和密文
                toggleShow();
                break;
        }
        if (v == headerView) {
            //头布局被点击了
        }
    }

    //显示运算设置对话框
    private void showBudgetDialog() {
        BudgetDialog dialog = new BudgetDialog(this);
        dialog.show();
        dialog.setDialogSize();

        dialog.setOnEnsureListener(new BudgetDialog.OnEnsureListener() {
            @Override
            public void onEnsure(float money) {
                //预算金额写入到共享参数中，进行存储
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("bmoney", money);
                editor.commit();

                //计算剩余金额
                float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
                float syMoney = money - outcomeOneMonth;//预算剩余 = 预算 - 支出
                topbudgetTv.setText("￥" + syMoney);
            }
        });

    }

    //点击头部眼睛时，明文加密，密文显示
    boolean isShow = true;

    private void toggleShow() {
        //明文=》密文
        if (isShow) {
            PasswordTransformationMethod passwordMethod = PasswordTransformationMethod.getInstance();
            topInTv.setTransformationMethod(passwordMethod);
            topOutTv.setTransformationMethod(passwordMethod);
            topbudgetTv.setTransformationMethod(passwordMethod);
            topShowIv.setImageResource(R.mipmap.ih_hide);
            isShow = false;//设置标志位为隐藏状态
        }
        //密文=》明文
        else {
            HideReturnsTransformationMethod hideMethod = HideReturnsTransformationMethod.getInstance();
            topInTv.setTransformationMethod(hideMethod);
            topOutTv.setTransformationMethod(hideMethod);
            topbudgetTv.setTransformationMethod(hideMethod);
            topShowIv.setImageResource(R.mipmap.ih_show);
            isShow = true;//设置标志位为隐藏状态
        }
    }
}
