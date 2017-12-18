package com.ostendi.osidoc.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ostendi.osidoc.model.Record;
import com.ostendi.osidoc.model.StoreDefinition;
import com.ostendi.osidoc.model.val.Value;
import com.ostendi.osidoc.server.Store;
import com.ostendi.osidoc.view.holder.CellViewHolder;
import com.ostendi.osidoc.view.holder.ColumnHeaderViewHolder;
import com.ostendi.osidoc.view.holder.RowHeaderViewHolder;
import com.ostendi.osidoc.view.adapter.recyclerview.holder.AbstractViewHolder;

import com.ostendi.osidoc.R;
import com.ostendi.osidoc.viewmodel.Cell;
import com.ostendi.osidoc.viewmodel.ColumnHeader;
import com.ostendi.osidoc.viewmodel.RowHeader;

public class TableViewAdapter extends AbstractTableAdapter<ColumnHeader, RowHeader, Cell> {
    Store store;
    private static final String LOG_TAG = TableViewAdapter.class.getSimpleName();

    public TableViewAdapter(Context context, Store store) {
        super(context);
        this.store=store;
    }

    //It recycles ViewHolder object to inflate from layout
    @Override
    public RecyclerView.ViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.table_view_cell_layout,
                parent, false);

        CellViewHolder cellViewHolder = new CellViewHolder(layout);
        return cellViewHolder;
    }
    @Override
    public void onBindCellViewHolder(AbstractViewHolder holder, Object objValue, int
            xPosition, int yPosition) {
        Record record = (Record) objValue;
        StoreDefinition storeDefinition =store.getDefinition();
        String fieldId = storeDefinition.getFieldId(xPosition);
        Value value = record.getFieldValue(fieldId);

        if (null == holder || !(holder instanceof CellViewHolder) || record == null) {
            return;
        }
        CellViewHolder viewHolder = (CellViewHolder) holder;
        viewHolder.cell_container.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        // viewHolder.cell_container.setTag(value);
        viewHolder.cell_textview.setText(value.toString());
        viewHolder.cell_textview.requestLayout();
        viewHolder.cell_container.requestLayout();
        viewHolder.itemView.requestLayout();
    }

    @Override
    public RecyclerView.ViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout
                .table_view_column_header_layout, parent, false);

        return new ColumnHeaderViewHolder(layout);
    }

    @Override
    public void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object p_jValue, int
            position) {
        ColumnHeader columnHeader = (ColumnHeader) p_jValue;

        if (null == holder || !(holder instanceof ColumnHeaderViewHolder) || columnHeader == null) {
            return;
        }

        ColumnHeaderViewHolder columnHeaderViewHolder = (ColumnHeaderViewHolder) holder;
        columnHeaderViewHolder.column_header_container.getLayoutParams().width = LinearLayout
                .LayoutParams.WRAP_CONTENT;
        columnHeaderViewHolder.column_header_textview.setText(columnHeader.getData());
        columnHeaderViewHolder.column_header_textview.requestLayout();
        columnHeaderViewHolder.column_header_container.requestLayout();
        columnHeaderViewHolder.itemView.requestLayout();

    }

    @Override
    public RecyclerView.ViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout
                .table_view_row_header_layout, parent, false);
        return new RowHeaderViewHolder(layout);
    }

    @Override
    public void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object p_jValue, int
            position) {
        RowHeader rowHeader = (RowHeader) p_jValue;

        if (null == holder || !(holder instanceof RowHeaderViewHolder) || rowHeader == null) {
            return;
        }
        RowHeaderViewHolder rowHeaderViewHolder = (RowHeaderViewHolder) holder;
        rowHeaderViewHolder.isSelected();
    }


    @Override
    public View onCreateCornerView() {
        return LayoutInflater.from(context).inflate(R.layout.table_view_corner_layout, null);
    }

    @Override
    public int getColumnHeaderItemViewType(int position) {
        // TODO:
        return 0;
    }

    @Override
    public int getRowHeaderItemViewType(int position) {
        // TODO:
        return 0;
    }

    @Override
    public int getCellItemViewType(int position) {
        return 0;
    }
}
