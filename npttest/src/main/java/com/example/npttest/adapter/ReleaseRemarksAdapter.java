package com.example.npttest.adapter;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.npttest.R;

import java.util.List;

/**
 * Created by liuji on 2017/9/12.
 */

public class ReleaseRemarksAdapter extends BaseItemDraggableAdapter<String ,BaseViewHolder> {

    public ReleaseRemarksAdapter(List<String> data) {
        super(R.layout.release_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.releaseItem_tv,item);
        helper.addOnClickListener(R.id.releaseItem_iv);
    }

}
