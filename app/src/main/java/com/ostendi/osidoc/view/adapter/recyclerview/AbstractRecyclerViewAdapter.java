package com.ostendi.osidoc.view.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected List<T> itemList;

    protected Context context;

    public AbstractRecyclerViewAdapter(Context context, List<T> itemList_) {
        this.context = context;

        if (itemList_ != null) {
            itemList =  new ArrayList<>(itemList_);
            this.notifyDataSetChanged();
        } else {
            itemList = new ArrayList<>();
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItems(List<T> p_jItemList) {
        itemList = new ArrayList<>(p_jItemList);
        Log.e("setItems", String.valueOf(itemList));
        this.notifyDataSetChanged();
    }

    public T getItem(int position) {
        if (itemList == null || itemList.isEmpty() || position < 0 || position >=
                itemList.size()) {
            return null;
        }
        return itemList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }
}
