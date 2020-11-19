package com.example.tally.frag_record;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.tally.R;
import com.example.tally.db.DBManager;
import com.example.tally.db.TypeBean;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OutcomeFragment extends BaseRecordFragment {


    //重写
    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        //获取数据库中数据
        List<TypeBean> outList = DBManager.getTypeList(0);
        typeList.addAll(outList);
        adapter.notifyDataSetChanged();
        typeTv.setText("其他");
        typeIv.setImageResource(R.mipmap.ic_qita_fs);
    }

    @Override
    public void saveAccountToDB() {

    }
}
