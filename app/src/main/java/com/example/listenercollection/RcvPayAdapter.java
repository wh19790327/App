package com.example.listenercollection;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.listenercollection.bean.PayBean;

public class RcvPayAdapter extends BaseQuickAdapter<PayBean, BaseViewHolder> {

    public RcvPayAdapter() {
        super(R.layout.rcv_item_pay);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PayBean item) {
        if(item.getType().equals("微信")){
            helper.setTextColor(R.id.tv_type, Color.parseColor("#00C35A"));
        }else{
            helper.setTextColor(R.id.tv_type, Color.parseColor("#2396E6"));
        }
        helper.setText(R.id.tv_pay,item.getPay());
        helper.setText(R.id.tv_time,item.getTime());
        helper.setText(R.id.tv_type,item.getType());
    }
}
