package com.example.npttest.adapter;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.npttest.R;

import java.util.List;

/**
 * Created by liuji on 2017/9/13.
 */

public class PopAdapter extends BaseItemDraggableAdapter<String,BaseViewHolder> {
    public PopAdapter(List<String> data) {
        super(R.layout.popup_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.pop_item_tv,item);
    }
}
