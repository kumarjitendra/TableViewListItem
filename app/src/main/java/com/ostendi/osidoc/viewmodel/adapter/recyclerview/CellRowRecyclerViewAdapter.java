package com.ostendi.osidoc.viewmodel.adapter.recyclerview;

import android.annotation.TargetApi;
import android.content.Context;
import android.icu.text.AlphabeticIndex;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.ostendi.osidoc.viewmodel.TableViewModel;
import com.ostendi.osidoc.viewmodel.adapter.ITableAdapter;
import com.ostendi.osidoc.viewmodel.adapter.recyclerview.holder.AbstractViewHolder;

import java.util.List;


public class CellRowRecyclerViewAdapter<C> extends AbstractRecyclerViewAdapter<C> {
    private static final String LOG_TAG = CellRowRecyclerViewAdapter.class.getSimpleName();
    private int yPosition;
    private ITableAdapter iTableAdapter;
    private TableViewModel tableviewModel;

    public CellRowRecyclerViewAdapter(Context context, List<C> itemList_, ITableAdapter
            iTableAdapter_, int yPosition) {
        super(context, itemList_);
        this.yPosition = yPosition;
        this.iTableAdapter = iTableAdapter_;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (iTableAdapter != null) {
            AbstractViewHolder viewHolder = (AbstractViewHolder) iTableAdapter.onCreateCellViewHolder(parent, viewType);


            return viewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int p_nXPosition) {
        if (iTableAdapter != null) {
            AbstractViewHolder viewHolder = (AbstractViewHolder) holder;
            Object value = getItem(p_nXPosition);
            Log.e(LOG_TAG+" Object value",value.toString());
            iTableAdapter.onBindCellViewHolder(viewHolder,value,p_nXPosition, yPosition);
            Log.e(LOG_TAG+" position",p_nXPosition +""+yPosition);
        }
    }

    public int getYPosition() {
        return yPosition;
    }

    @Override
    public int getItemViewType(int position) {
        return iTableAdapter.getCellItemViewType(position);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        AbstractViewHolder viewHolder = (AbstractViewHolder) holder;

        boolean isSelected = iTableAdapter.getTableView().getSelectionHandler().isCellSelected
                (holder.getAdapterPosition(), yPosition);

        // Change the background color of the view considering selected row/cell position.
        if (isSelected) {
            viewHolder.setBackgroundColor(iTableAdapter.getTableView().getSelectedColor());
        } else {
            viewHolder.setBackgroundColor(iTableAdapter.getTableView().getUnSelectedColor());
        }

        // Change selection status
        viewHolder.setSelected(isSelected);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        AbstractViewHolder viewHolder = (AbstractViewHolder) holder;

        // Clear selection status of the view holder
        viewHolder.setBackgroundColor(iTableAdapter.getTableView().getUnSelectedColor());
        viewHolder.setSelected(false);
    }


}
