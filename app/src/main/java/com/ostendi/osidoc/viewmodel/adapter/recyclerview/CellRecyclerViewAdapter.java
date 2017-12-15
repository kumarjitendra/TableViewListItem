package com.ostendi.osidoc.viewmodel.adapter.recyclerview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.ostendi.osidoc.viewmodel.HorizontalRecyclerViewListener;
import com.ostendi.osidoc.viewmodel.ITableView;
import com.ostendi.tableview.R;
import com.ostendi.osidoc.viewmodel.adapter.ITableAdapter;
import com.ostendi.osidoc.viewmodel.adapter.recyclerview.holder.AbstractViewHolder;
import com.ostendi.osidoc.viewmodel.handler.SelectionHandler;
import com.ostendi.osidoc.viewmodel.layoutmanager.ColumnLayoutManager;


import java.util.ArrayList;
import java.util.List;

public class CellRecyclerViewAdapter<C> extends AbstractRecyclerViewAdapter<C> {

    private List<RecyclerView.Adapter> adapterList;

    private ITableAdapter iTableAdapter;

    private final DividerItemDecoration cellItemDecoration;
    private HorizontalRecyclerViewListener horizontalRecyclerViewListener;

    // This is for testing purpose
    private int recyclerViewId = 0;

    public CellRecyclerViewAdapter(Context context, List<C> itemList_, ITableAdapter
            iTableAdapter_) {
        super(context, itemList_);
        Log.e("Cell_RecyView_Adapter", String.valueOf(itemList_)); // It has null value
        this.iTableAdapter = iTableAdapter_;

        // Initialize the array
        adapterList = new ArrayList<>();

        // Create Item decoration
        cellItemDecoration = createCellItemDecoration();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get TableView
        ITableView iTableView = iTableAdapter.getTableView();

        // Create a RecyclerView as a Row of the CellRecyclerView
        final CellRecyclerView jRecyclerView = new CellRecyclerView(context);

        // Add divider
        jRecyclerView.addItemDecoration(cellItemDecoration);


        if (iTableView != null) {
            // set touch horizontalRecyclerViewListener to scroll synchronously
            if (horizontalRecyclerViewListener == null) {
                horizontalRecyclerViewListener = iTableView.getHorizontalRecyclerViewListener();
            }

            jRecyclerView.addOnItemTouchListener(horizontalRecyclerViewListener);

            if (iTableView.getTableViewListener() != null) {
                // Add Item click listener for cell views
                jRecyclerView.addOnItemTouchListener(new CellRecyclerViewItemClickListener
                        (jRecyclerView, iTableView));
            }

            // Set the Column layout manager that helps the fit width of the cell and column header
            // and it also helps to locate the scroll position of the horizontal recyclerView
            // which is row recyclerView
            ColumnLayoutManager layoutManager = new ColumnLayoutManager(context, iTableView,
                    jRecyclerView);
            jRecyclerView.setLayoutManager(layoutManager);

            // This is for testing purpose to find out which recyclerView is displayed.
            jRecyclerView.setId(recyclerViewId);

            recyclerViewId++;
        }

        return new CellRowViewHolder(jRecyclerView);
    }
//Called by RecyclerView to display the data at the specified position.
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int yPosition) {
        if (!(holder instanceof CellRowViewHolder)) {
            return;
        }

        CellRowViewHolder viewHolder = (CellRowViewHolder) holder;
        // Set adapter to the RecyclerView
        List<C> listItem =  itemList;
        Log.e("listItem_cellReVA",listItem.toString());

        CellRowRecyclerViewAdapter viewAdapter = new CellRowRecyclerViewAdapter(context,
                listItem, iTableAdapter, yPosition);

        viewHolder.cellRecyclerView.setAdapter(viewAdapter);

        // Add the adapter to the list
        adapterList.add(viewAdapter);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        // The below code helps to display a new attached recyclerView on exact scrolled position.
        CellRowViewHolder viewHolder = (CellRowViewHolder) holder;
        ((ColumnLayoutManager) viewHolder.cellRecyclerView.getLayoutManager())
                .scrollToPositionWithOffset(horizontalRecyclerViewListener.getScrollPosition(),
                        horizontalRecyclerViewListener.getScrollPositionOffset());


        SelectionHandler selectionHandler = iTableAdapter.getTableView().getSelectionHandler();

        int nXPosition = selectionHandler.getSelectedColumnPosition();
        if (nXPosition != SelectionHandler.UNSELECTED_POSITION && selectionHandler
                .getSelectedRowPosition() == SelectionHandler.UNSELECTED_POSITION) {
            AbstractViewHolder cellViewHolder = (AbstractViewHolder) ((CellRowViewHolder) holder)
                    .cellRecyclerView.findViewHolderForAdapterPosition(nXPosition);

            if (cellViewHolder != null) {
                cellViewHolder.setBackgroundColor(iTableAdapter.getTableView().getSelectedColor
                        ());
                cellViewHolder.setSelected(true);

            }
        }

    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        // Clear selection status of the view holder
        ((CellRowViewHolder) holder).cellRecyclerView.setSelected(true, iTableAdapter
                .getTableView().getUnSelectedColor());
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        CellRowViewHolder viewHolder = (CellRowViewHolder) holder;
        // ScrolledX should be cleared at that time. Because we need to prepare each
        // recyclerView at onViewAttachedToWindow process.
        viewHolder.cellRecyclerView.clearScrolledX();
    }

    static class CellRowViewHolder extends RecyclerView.ViewHolder {
        final CellRecyclerView cellRecyclerView;

        CellRowViewHolder(View itemView) {
            super(itemView);
            cellRecyclerView = (CellRecyclerView) itemView;
        }
    }

    private DividerItemDecoration createCellItemDecoration() {
        Drawable mDivider = ContextCompat.getDrawable(context, R.drawable.cell_line_divider);

        DividerItemDecoration jItemDecoration = new DividerItemDecoration(context,
                DividerItemDecoration.HORIZONTAL);
        jItemDecoration.setDrawable(mDivider);
        return jItemDecoration;
    }


    public void notifyCellDataSetChanged() {
        if (adapterList != null) {
            if (adapterList.isEmpty()) {
                notifyDataSetChanged();
            } else {
                for (RecyclerView.Adapter adapter : adapterList) {
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}
