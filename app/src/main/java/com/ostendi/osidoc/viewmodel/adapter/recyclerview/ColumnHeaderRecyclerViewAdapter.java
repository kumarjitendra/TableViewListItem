package com.ostendi.osidoc.viewmodel.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.ostendi.osidoc.viewmodel.adapter.ITableAdapter;
import com.ostendi.osidoc.viewmodel.adapter.recyclerview.holder.AbstractViewHolder;

import java.util.List;

/**
 * Created by jitendra on 15/11/2017.
 */

public class ColumnHeaderRecyclerViewAdapter<CH> extends AbstractRecyclerViewAdapter<CH> {
    private static final String LOG_TAG = ColumnHeaderRecyclerViewAdapter.class.getSimpleName();
    private ITableAdapter m_iTableAdapter;

    public ColumnHeaderRecyclerViewAdapter(Context context, List<CH> p_jItemList, ITableAdapter
            p_iTableAdapter) {
        super(context, p_jItemList);
        Log.e("Col_HeadRe_ViewAdapter", String.valueOf(p_jItemList));
        this.m_iTableAdapter = p_iTableAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return m_iTableAdapter.onCreateColumnHeaderViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AbstractViewHolder viewHolder = (AbstractViewHolder) holder;
        Object value = getItem(position);

        m_iTableAdapter.onBindColumnHeaderViewHolder(viewHolder, value, position);
    }

    @Override
    public int getItemViewType(int position) {
        return m_iTableAdapter.getColumnHeaderItemViewType(position);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        AbstractViewHolder viewHolder = (AbstractViewHolder) holder;

        boolean isSelected = m_iTableAdapter.getTableView().getSelectionHandler()
                .isColumnSelected(viewHolder.getAdapterPosition());

        // Change background color of the view considering it's selected state
        m_iTableAdapter.getTableView().getSelectionHandler()
                .changeColumnBackgroundColorBySelectionStatus(viewHolder.getAdapterPosition(),
                        viewHolder);

        // Change selection status
        viewHolder.setSelected(isSelected);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        AbstractViewHolder viewHolder = (AbstractViewHolder) holder;

        viewHolder.setBackgroundColor(m_iTableAdapter.getTableView().getUnSelectedColor());
        viewHolder.setSelected(false);
    }
}
