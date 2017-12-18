package com.ostendi.osidoc.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.widget.FrameLayout;


import com.ostendi.osidoc.view.adapter.AbstractTableAdapter;
import com.ostendi.osidoc.view.adapter.recyclerview.CellRecyclerView;
import com.ostendi.osidoc.view.adapter.recyclerview.holder.AbstractViewHolder;
import com.ostendi.osidoc.view.handler.SelectionHandler;
import com.ostendi.osidoc.view.layoutmanager.CellLayoutManager;
import com.ostendi.osidoc.view.layoutmanager.ColumnHeaderLayoutManager;
import com.ostendi.osidoc.view.listener.ColumnHeaderRecyclerViewItemClickListener;
import com.ostendi.osidoc.view.listener.HorizontalRecyclerViewListener;
import com.ostendi.osidoc.view.listener.ITableViewListener;
import com.ostendi.osidoc.view.listener.RowHeaderRecyclerViewItemClickListener;
import com.ostendi.osidoc.view.listener.VerticalRecyclerViewListener;

public class TableView extends FrameLayout implements ITableView {

    protected CellRecyclerView m_jCellRecyclerView;
    protected CellRecyclerView m_jColumnHeaderRecyclerView;
    protected CellRecyclerView m_jRowHeaderRecyclerView;

    protected AbstractTableAdapter m_iTableAdapter;
    private ITableViewListener m_iTableViewListener;

    private VerticalRecyclerViewListener m_jVerticalRecyclerListener;
    private HorizontalRecyclerViewListener m_jHorizontalRecyclerViewListener;

    private ColumnHeaderRecyclerViewItemClickListener m_jColumnHeaderRecyclerViewItemClickListener;
    private RowHeaderRecyclerViewItemClickListener m_jRowHeaderRecyclerViewItemClickListener;

    private ColumnHeaderLayoutManager m_iColumnHeaderLayoutManager;
    private LinearLayoutManager m_jRowHeaderLayoutManager;
    private CellLayoutManager m_iCellLayoutManager;

    private SelectionHandler m_iSelectionHandler;

    private int m_nRowHeaderWidth;
    private int m_nColumnHeaderHeight;

    private int m_nSelectedColor;
    private int m_nUnSelectedColor;
    private int m_nShadowColor;

    public TableView(@NonNull Context context) {
        super(context);
        initialize();
    }

    public TableView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public TableView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int
            defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        // initialize default dimensions
        m_nRowHeaderWidth = (int) getResources().getDimension(com.ostendi.tableview.R.dimen.default_row_header_width);
        m_nColumnHeaderHeight = (int) getResources().getDimension(com.ostendi.tableview.R.dimen
                .default_column_header_height);

        // initialize default colors
        m_nSelectedColor = ContextCompat.getColor(getContext(), com.ostendi.tableview.R.color
                .table_view_selected_background_color);
        m_nUnSelectedColor = ContextCompat.getColor(getContext(), com.ostendi.tableview.R.color.default_background_color);
        m_nShadowColor = ContextCompat.getColor(getContext(), com.ostendi.tableview.R.color
                .table_view_shadow_background_color);

        // Create Views
        m_jColumnHeaderRecyclerView = createColumnHeaderRecyclerView();
        m_jRowHeaderRecyclerView = createRowHeaderRecyclerView();
        m_jCellRecyclerView = createCellRecyclerView();

        // Add Views
        addView(m_jColumnHeaderRecyclerView);
        addView(m_jRowHeaderRecyclerView);
        addView(m_jCellRecyclerView);

        // Create Selection Handler
        m_iSelectionHandler = new SelectionHandler(this);

        initializeListeners();
    }

    protected void initializeListeners() {
        // It handles Vertical scroll listener
        m_jVerticalRecyclerListener = new VerticalRecyclerViewListener(this);

        // Set this listener both of Cell RecyclerView and RowHeader RecyclerView
        m_jRowHeaderRecyclerView.addOnItemTouchListener(m_jVerticalRecyclerListener);
        m_jCellRecyclerView.addOnItemTouchListener(m_jVerticalRecyclerListener);

        // It handles Horizontal scroll listener
        m_jHorizontalRecyclerViewListener = new HorizontalRecyclerViewListener(this);
        // Set scroll listener to be able to scroll all rows synchrony.
        m_jColumnHeaderRecyclerView.addOnItemTouchListener(m_jHorizontalRecyclerViewListener);

    }

    protected CellRecyclerView createColumnHeaderRecyclerView() {
        CellRecyclerView recyclerView = new CellRecyclerView(getContext());

        // Set layout manager
        recyclerView.setLayoutManager(getColumnHeaderLayoutManager());

        // Set layout params
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                m_nColumnHeaderHeight);
        layoutParams.leftMargin = m_nRowHeaderWidth;
        recyclerView.setLayoutParams(layoutParams);

        // Add vertical item decoration to display column line
        recyclerView.addItemDecoration(createItemDecoration(DividerItemDecoration.HORIZONTAL));
        return recyclerView;
    }

    protected CellRecyclerView createRowHeaderRecyclerView() {
        CellRecyclerView recyclerView = new CellRecyclerView(getContext());

        // Set layout manager
        recyclerView.setLayoutManager(getRowHeaderLayoutManager());

        // Set layout params
        LayoutParams layoutParams = new LayoutParams(m_nRowHeaderWidth, LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = m_nColumnHeaderHeight;
        recyclerView.setLayoutParams(layoutParams);

        // Add vertical item decoration to display row line
        recyclerView.addItemDecoration(createItemDecoration(DividerItemDecoration.VERTICAL));

        return recyclerView;
    }

    protected CellRecyclerView createCellRecyclerView() {
        CellRecyclerView recyclerView = new CellRecyclerView(getContext());

        // Disable multitouch
        recyclerView.setMotionEventSplittingEnabled(false);

        // Set layout manager
        recyclerView.setLayoutManager(getCellLayoutManager());

        // Set layout params
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams
                .WRAP_CONTENT);
        layoutParams.leftMargin = m_nRowHeaderWidth;
        layoutParams.topMargin = m_nColumnHeaderHeight;
        recyclerView.setLayoutParams(layoutParams);

        // Add vertical item decoration to display row line on center recycler view
        recyclerView.addItemDecoration(createItemDecoration(DividerItemDecoration.VERTICAL));
        return recyclerView;
    }

    private DividerItemDecoration createItemDecoration(int orientation) {
        Drawable mDivider = ContextCompat.getDrawable(getContext(), com.ostendi.tableview.R.drawable.cell_line_divider);

        DividerItemDecoration jItemDecoration = new DividerItemDecoration(getContext(),
                orientation);
        jItemDecoration.setDrawable(mDivider);
        return jItemDecoration;
    }


    public void setAdapter(AbstractTableAdapter p_iTableAdapter) {
        if (p_iTableAdapter != null) {
            this.m_iTableAdapter = p_iTableAdapter;
            this.m_iTableAdapter.setRowHeaderWidth(m_nRowHeaderWidth);
            this.m_iTableAdapter.setColumnHeaderHeight(m_nColumnHeaderHeight);
            this.m_iTableAdapter.setTableView(this);

            // set adapters
            if (m_jColumnHeaderRecyclerView != null) {
                m_jColumnHeaderRecyclerView.setAdapter(m_iTableAdapter
                        .getColumnHeaderRecyclerViewAdapter());
            }
            if (m_jRowHeaderRecyclerView != null) {
                m_jRowHeaderRecyclerView.setAdapter(m_iTableAdapter
                        .getRowHeaderRecyclerViewAdapter());
            }
            if (m_jCellRecyclerView != null) {
                m_jCellRecyclerView.setAdapter(m_iTableAdapter.getCellRecyclerViewAdapter());
            }
        }
    }

    @Override
    public CellRecyclerView getCellRecyclerView() {
        return m_jCellRecyclerView;
    }

    @Override
    public CellRecyclerView getColumnHeaderRecyclerView() {
        return m_jColumnHeaderRecyclerView;
    }

    @Override
    public CellRecyclerView getRowHeaderRecyclerView() {
        return m_jRowHeaderRecyclerView;
    }

    @Override
    public ColumnHeaderLayoutManager getColumnHeaderLayoutManager() {
        if (m_iColumnHeaderLayoutManager == null) {
            m_iColumnHeaderLayoutManager = new ColumnHeaderLayoutManager(getContext());
        }
        return m_iColumnHeaderLayoutManager;
    }

    @Override
    public CellLayoutManager getCellLayoutManager() {
        if (m_iCellLayoutManager == null) {
            m_iCellLayoutManager = new CellLayoutManager(getContext(), this);
        }
        return m_iCellLayoutManager;
    }

    @Override
    public LinearLayoutManager getRowHeaderLayoutManager() {
        if (m_jRowHeaderLayoutManager == null) {
            m_jRowHeaderLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager
                    .VERTICAL, false);
        }
        return m_jRowHeaderLayoutManager;
    }

    @Override
    public HorizontalRecyclerViewListener getHorizontalRecyclerViewListener() {
        return m_jHorizontalRecyclerViewListener;
    }

    @Override
    public VerticalRecyclerViewListener getVerticalRecyclerViewListener() {
        return m_jVerticalRecyclerListener;
    }

    @Override
    public ITableViewListener getTableViewListener() {
        return m_iTableViewListener;
    }


    public void setTableViewListener(ITableViewListener p_jTableViewListener) {
        this.m_iTableViewListener = p_jTableViewListener;

        // Remove old ones
        if (m_jColumnHeaderRecyclerViewItemClickListener != null &&
                m_jRowHeaderRecyclerViewItemClickListener != null) {

            m_jColumnHeaderRecyclerView.removeOnItemTouchListener
                    (m_jColumnHeaderRecyclerViewItemClickListener);

            m_jRowHeaderRecyclerView.removeOnItemTouchListener
                    (m_jRowHeaderRecyclerViewItemClickListener);
        }

        // Create item click listeners
        m_jColumnHeaderRecyclerViewItemClickListener = new
                ColumnHeaderRecyclerViewItemClickListener(m_jColumnHeaderRecyclerView, this);

        m_jRowHeaderRecyclerViewItemClickListener = new RowHeaderRecyclerViewItemClickListener
                (m_jRowHeaderRecyclerView, this);

        // Add item click listeners for both column header & row header recyclerView
        m_jColumnHeaderRecyclerView.addOnItemTouchListener
                (m_jColumnHeaderRecyclerViewItemClickListener);
        m_jRowHeaderRecyclerView.addOnItemTouchListener(m_jRowHeaderRecyclerViewItemClickListener);
    }


    /**
     * Returns the index of the selected row, -1 if no row is selected.
     */
    public int getSelectedRow() {
        return m_iSelectionHandler.getSelectedRowPosition();
    }

    public void setSelectedRow(int p_nYPosition) {
        // Find the row header view holder which is located on y position.
        AbstractViewHolder jRowViewHolder = (AbstractViewHolder) getRowHeaderRecyclerView()
                .findViewHolderForAdapterPosition(p_nYPosition);


        m_iSelectionHandler.setSelectedRowPosition(jRowViewHolder, p_nYPosition);
    }

    /**
     * Returns the index of the selected column, -1 if no column is selected.
     */
    public int getSelectedColumn() {
        return m_iSelectionHandler.getSelectedColumnPosition();
    }

    public void setSelectedColumn(int p_nXPosition) {
        // Find the column view holder which is located on x position.
        AbstractViewHolder jColumnViewHolder = (AbstractViewHolder) getColumnHeaderRecyclerView()
                .findViewHolderForAdapterPosition(p_nXPosition);

        m_iSelectionHandler.setSelectedColumnPosition(jColumnViewHolder, p_nXPosition);
    }

    public void setSelectedCell(int p_nXPosition, int p_nYPosition) {
        // Find the cell view holder which is located on x,y position.
        AbstractViewHolder jCellViewHolder = getCellLayoutManager().getCellViewHolder
                (p_nXPosition, p_nYPosition);

        m_iSelectionHandler.setSelectedCellPositions(jCellViewHolder, p_nXPosition, p_nYPosition);
    }

    @Override
    public SelectionHandler getSelectionHandler() {
        return m_iSelectionHandler;
    }

    /**
     * This method helps to change default selected color programmatically.
     *
     * @param p_nSelectedColor It must be Color int.
     */
    public void setSelectedColor(@ColorInt int p_nSelectedColor) {
        this.m_nSelectedColor = p_nSelectedColor;
    }

    @Override
    public @ColorInt
    int getSelectedColor() {
        return m_nSelectedColor;
    }

    /**
     * This method helps to change default unselected color programmatically.
     *
     * @param p_nUnSelectedColor It must be Color int.
     */
    public void setUnSelectedColor(@ColorInt int p_nUnSelectedColor) {
        this.m_nUnSelectedColor = p_nUnSelectedColor;
    }

    @Override
    public @ColorInt
    int getUnSelectedColor() {
        return m_nUnSelectedColor;
    }

    public void setShadowColor(@ColorInt int p_nShadowColor) {
        this.m_nShadowColor = p_nShadowColor;
    }

    @Override
    public @ColorInt
    int getShadowColor() {
        return m_nShadowColor;
    }
}
