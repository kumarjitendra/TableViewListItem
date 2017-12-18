package com.ostendi.osidoc.view.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ostendi.osidoc.view.adapter.ITableAdapter;
import com.ostendi.osidoc.view.adapter.recyclerview.holder.AbstractViewHolder;

import java.util.List;

/**
 * Created by jitendra on 15/11/2017.
 */

public class RowHeaderRecyclerViewAdapter<RH> extends AbstractRecyclerViewAdapter<RH> {

    private ITableAdapter m_iTableAdapter;

    public RowHeaderRecyclerViewAdapter(Context context, List<RH> p_jItemList, ITableAdapter
            p_iTableAdapter) {
        super(context, p_jItemList);
        this.m_iTableAdapter = p_iTableAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return m_iTableAdapter.onCreateRowHeaderViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AbstractViewHolder viewHolder = (AbstractViewHolder) holder;
        Object value = getItem(position);

        m_iTableAdapter.onBindRowHeaderViewHolder(viewHolder, value, position);
    }

    @Override
    public int getItemViewType(int position) {
        return m_iTableAdapter.getRowHeaderItemViewType(position);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        AbstractViewHolder viewHolder = (AbstractViewHolder) holder;

        boolean isSelected = m_iTableAdapter.getTableView().getSelectionHandler().isRowSelected
                (holder.getAdapterPosition());

        // Change background color of the view considering it's selected state
        m_iTableAdapter.getTableView().getSelectionHandler()
                .changeRowBackgroundColorBySelectionStatus(holder.getAdapterPosition(), viewHolder);

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
