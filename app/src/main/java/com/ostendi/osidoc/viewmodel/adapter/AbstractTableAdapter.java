package com.ostendi.osidoc.viewmodel.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.ostendi.osidoc.viewmodel.ITableView;
import com.ostendi.osidoc.viewmodel.TableView;
import com.ostendi.osidoc.viewmodel.adapter.recyclerview.CellRecyclerViewAdapter;
import com.ostendi.osidoc.viewmodel.adapter.recyclerview.ColumnHeaderRecyclerViewAdapter;
import com.ostendi.osidoc.viewmodel.adapter.recyclerview.RowHeaderRecyclerViewAdapter;

import java.util.List;

public abstract class AbstractTableAdapter<CH, RH, C> implements ITableAdapter {

    private int rowHeaderWidth;
    private int columnHeaderWidth;

    protected Context context;
    private ColumnHeaderRecyclerViewAdapter columnHeaderRecyclerViewAdapter;
    private RowHeaderRecyclerViewAdapter rowHeaderRecyclerViewAdapter;
    private CellRecyclerViewAdapter cellRecyclerViewAdapter;
    private View cornerView;

    protected List<CH> columnHeaderItems;
    protected List<RH> rowHeaderItems;
    protected List<C> cellItems;

    private ITableView tableView;

    public AbstractTableAdapter(Context context1) {
        context = context1;
        initialize();
    }

    private void initialize() {
        // Create Column header RecyclerView Adapter
        columnHeaderRecyclerViewAdapter = new ColumnHeaderRecyclerViewAdapter(context,
                columnHeaderItems, this);

        // Create Row Header RecyclerView Adapter
        rowHeaderRecyclerViewAdapter = new RowHeaderRecyclerViewAdapter(context,
                rowHeaderItems, this);

        // Create Cell RecyclerView Adapter
        cellRecyclerViewAdapter = new CellRecyclerViewAdapter(context, cellItems, this);
    }

    public void setColumnHeaderItems(List<CH> p_jColumnHeaderItems) {
        if (p_jColumnHeaderItems == null) {
            return;
        }

        columnHeaderItems = p_jColumnHeaderItems;

        // Set the items to the adapter
        columnHeaderRecyclerViewAdapter.setItems(columnHeaderItems);
    }

    public void setRowHeaderItems(List<RH> p_jRowHeaderItems) {
        if (p_jRowHeaderItems == null) {
            return;
        }

        rowHeaderItems = p_jRowHeaderItems;

        // Set the items to the adapter
        rowHeaderRecyclerViewAdapter.setItems(rowHeaderItems);
    }

    public void setCellItems(List<C> p_jCellItems) {
        if (p_jCellItems == null) {
            return;
        }

        cellItems = p_jCellItems;

        // Set the items to the adapter
        cellRecyclerViewAdapter.setItems(cellItems);
    }

    public void setAllItems(List<CH> p_jColumnHeaderItems, List<RH> p_jRowHeaderItems,
                            List<C> p_jCellItems) {
        // Set all items
        setColumnHeaderItems(p_jColumnHeaderItems);
        setRowHeaderItems(p_jRowHeaderItems);
        setCellItems(p_jCellItems);

        // Control corner view
        if ((p_jColumnHeaderItems != null && !p_jColumnHeaderItems.isEmpty()) &&
                (p_jRowHeaderItems != null && !p_jRowHeaderItems.isEmpty()) && (p_jCellItems !=
                null && !p_jCellItems.isEmpty()) && tableView != null && cornerView == null) {

            // Create corner view
            cornerView = onCreateCornerView();
            tableView.addView(cornerView, new FrameLayout.LayoutParams(rowHeaderWidth,
                    columnHeaderWidth));
        } else if (cornerView != null) {

            // Change corner view visibility
            if (p_jRowHeaderItems != null && !p_jRowHeaderItems.isEmpty()) {
                cornerView.setVisibility(View.GONE);
            } else {
                cornerView.setVisibility(View.VISIBLE);
            }
        }
    }

    public ColumnHeaderRecyclerViewAdapter getColumnHeaderRecyclerViewAdapter() {
        return columnHeaderRecyclerViewAdapter;
    }

    public RowHeaderRecyclerViewAdapter getRowHeaderRecyclerViewAdapter() {
        return rowHeaderRecyclerViewAdapter;
    }

    public CellRecyclerViewAdapter getCellRecyclerViewAdapter() {
        return cellRecyclerViewAdapter;
    }

    public void setRowHeaderWidth(int p_nRowHeaderWidth) {
        this.rowHeaderWidth = p_nRowHeaderWidth;
    }

    public void setColumnHeaderHeight(int p_nColumnHeaderHeight) {
        this.columnHeaderWidth = p_nColumnHeaderHeight;
    }

    public CH getColumnHeaderItem(int p_nPosition) {
        if ((columnHeaderItems == null || columnHeaderItems.isEmpty()) || p_nPosition < 0
                || p_nPosition >= columnHeaderItems.size()) {
            return null;
        }
        return columnHeaderItems.get(p_nPosition);
    }

    public RH getRowHeaderItem(int p_nPosition) {
        if ((rowHeaderItems == null || rowHeaderItems.isEmpty()) || p_nPosition < 0 ||
                p_nPosition >= rowHeaderItems.size()) {
            return null;
        }
        return rowHeaderItems.get(p_nPosition);
    }

    /**
     * public C getCellItem(int p_nXPosition, int p_nYPosition) {
     * if ((cellItems == null || cellItems.isEmpty()) || p_nXPosition < 0 || p_nYPosition
     * >= cellItems.size() || cellItems.get(p_nYPosition) == null || p_nYPosition
     * < 0 || p_nXPosition >= cellItems.get(p_nYPosition).size()) {
     * return null;
     * }
     * <p>
     * return cellItems.get(p_nYPosition).get(p_nXPosition);
     * }
     **/
    public final void notifyDataSetChanged() {
        columnHeaderRecyclerViewAdapter.notifyDataSetChanged();
        rowHeaderRecyclerViewAdapter.notifyDataSetChanged();
        cellRecyclerViewAdapter.notifyCellDataSetChanged();
    }

    public void setTableView(TableView p_iTableView) {
        tableView = p_iTableView;
    }

    @Override
    public ITableView getTableView() {
        return tableView;
    }

}
