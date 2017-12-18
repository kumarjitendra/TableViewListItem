package com.ostendi.osidoc.view.layoutmanager;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.ostendi.osidoc.view.ITableView;
import com.ostendi.osidoc.view.adapter.recyclerview.CellRecyclerView;
import com.ostendi.osidoc.view.adapter.recyclerview.holder.AbstractViewHolder;
import com.ostendi.osidoc.view.listener.HorizontalRecyclerViewListener;
import com.ostendi.osidoc.view.util.TableViewUtils;

/**
 * Created by jitendra on 17/11/2017.
 */

public class CellLayoutManager extends LinearLayoutManager {
    private static final String LOG_TAG = CellLayoutManager.class.getSimpleName();
    private static final int IGNORE_LEFT = -99999;

    private ColumnHeaderLayoutManager columnHeaderLayoutManager;
    private LinearLayoutManager m_jRowHeaderLayoutManager;

    private CellRecyclerView m_iRowHeaderRecyclerView;
    private CellRecyclerView cellRecyclerView;

    private HorizontalRecyclerViewListener horizontalListener;
    private ITableView iTableView;

    private int m_nLastDy = 0;
    private boolean needSetLeft = false;
    private boolean m_bNeedFit = false;

    public CellLayoutManager(Context context, ITableView iTableView) {
        super(context);
        this.cellRecyclerView = iTableView.getCellRecyclerView();
        this.columnHeaderLayoutManager = iTableView.getColumnHeaderLayoutManager();
        //  this.m_jRowHeaderLayoutManager = iTableView.getRowHeaderLayoutManager();
        // this.m_iRowHeaderRecyclerView = iTableView.getRowHeaderRecyclerView();
        this.iTableView = iTableView;

        initialize();
    }


    private void initialize() {
        this.setOrientation(VERTICAL);
        //this.setMeasurementCacheEnabled(false);
        // Add new one
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);

        // initialize the instances
        if (cellRecyclerView == null) {
            cellRecyclerView = iTableView.getCellRecyclerView();
        }

        if (horizontalListener == null) {
            horizontalListener = iTableView.getHorizontalRecyclerViewListener();
        }
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State
            state) {
        /**    if (m_iRowHeaderRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE &&
         !m_iRowHeaderRecyclerView.isScrollOthers()) {
         // CellRecyclerViews should be scrolled after the RowHeaderRecyclerView.
         // Because it is one of the main compared criterion to make each columns fit.
         m_iRowHeaderRecyclerView.scrollBy(0, dy);
         }
         **/
        int nScroll = super.scrollVerticallyBy(dy, recycler, state);

        // It is important to determine right position to fit all columns which are the same y pos.
        m_nLastDy = dy;
        return nScroll;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            // It is important to set it 0 to be able to know which direction is being scrolled
            m_nLastDy = 0;
        }
    }


    /**
     * This method helps to fit all columns which are displayed on screen.
     * Especially it will be called when TableView is scrolled on vertically.
     */
    public void fitWidthSize(boolean p_nScrollingUp) {
        int nLeft = columnHeaderLayoutManager.getFirstItemLeft();
        for (int i = columnHeaderLayoutManager.findFirstVisibleItemPosition(); i <
                columnHeaderLayoutManager.findLastVisibleItemPosition() + 1; i++) {
            nLeft = fitSize(i, nLeft, p_nScrollingUp);
        }

        needSetLeft = false;
    }

    /**
     * This method helps to fit a column. it will be called when TableView is scrolled on
     * horizontally.
     */
    public void fitWidthSize(int position, boolean scrollingLeft) {
        fitSize(position, IGNORE_LEFT, false);

        if (needSetLeft & scrollingLeft) {
            // Works just like invoke later of swing utils.
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    fitWidthSize2();
                }
            });
        }
    }


    private int fitSize(int p_nPosition, int p_nLeft, boolean p_nScrollingUp) {
        int nCellRight = -1;

        int nColumnCacheWidth = columnHeaderLayoutManager.getCacheWidth(p_nPosition);
        View column = columnHeaderLayoutManager.findViewByPosition(p_nPosition);

        if (column != null) {
            // Determine default right
            nCellRight = column.getLeft() + nColumnCacheWidth + 1;

            if (p_nScrollingUp) {
                // Loop reverse order
                for (int i = findLastVisibleItemPosition(); i >= findFirstVisibleItemPosition();
                     i--) {
                    nCellRight = fit(p_nPosition, i, p_nLeft, nCellRight, nColumnCacheWidth);
                }
            } else {
                // Loop for all rows which are visible.
                for (int j = findFirstVisibleItemPosition(); j < findLastVisibleItemPosition() +
                        1; j++) {
                    nCellRight = fit(p_nPosition, j, p_nLeft, nCellRight, nColumnCacheWidth);
                }
            }
        }
        return nCellRight;
    }

    private int fit(int p_nXPosition, int p_nYPosition, int p_nLeft, int p_nRight, int
            p_nColumnCachedWidth) {
        CellRecyclerView child = (CellRecyclerView) findViewByPosition(p_nYPosition);

        if (child != null) {
            ColumnLayoutManager childLayoutManager = (ColumnLayoutManager) child.getLayoutManager();

            int nCellCacheWidth = childLayoutManager.getCacheWidth(p_nXPosition);
            View cell = childLayoutManager.findViewByPosition(p_nXPosition);

            // Control whether the cell needs to be fitted by column header or not.
            if (cell != null) {

                if (nCellCacheWidth != p_nColumnCachedWidth || needSetLeft) {

                    // This is just for setting width value
                    if (nCellCacheWidth != p_nColumnCachedWidth) {
                        nCellCacheWidth = p_nColumnCachedWidth;
                        TableViewUtils.setWidth(cell, nCellCacheWidth);
                        childLayoutManager.setCacheWidth(p_nXPosition, nCellCacheWidth);
                    }

                    // Even if the cached values are same, the left & right value wouldn't change.
                    // m_bNeedSetLeftValue & the below lines for it.
                    if (p_nLeft != IGNORE_LEFT && cell.getLeft() != p_nLeft) {

                        // Calculate scroll distance
                        int nScrollX = Math.max(cell.getLeft(), p_nLeft) - Math.min(cell.getLeft
                                (), p_nLeft);

                        cell.setLeft(p_nLeft);

                        // It shouldn't be scroll horizontally and the problem is gotten just for
                        // first visible item.
                        if (horizontalListener.getScrollPositionOffset() > 0 && p_nXPosition
                                == childLayoutManager.findFirstVisibleItemPosition() &&
                                cellRecyclerView.getScrollState() != RecyclerView
                                        .SCROLL_STATE_IDLE) { //


                            horizontalListener.setScrollPositionOffset(horizontalListener
                                    .getScrollPositionOffset() + nScrollX);
                            childLayoutManager.scrollToPositionWithOffset(horizontalListener
                                    .getScrollPosition(), horizontalListener
                                    .getScrollPositionOffset());
                        }
                    }

                    if (cell.getWidth() != nCellCacheWidth) {
                        if (p_nLeft != IGNORE_LEFT) {
                            // TODO: + 1 is for decoration item. It should be gotten from a
                            // generic method  of layoutManager
                            // Set right
                            p_nRight = cell.getLeft() + nCellCacheWidth + 1;
                            cell.setRight(p_nRight);

                            childLayoutManager.layoutDecoratedWithMargins(cell, cell.getLeft(),
                                    cell.getTop(), cell.getRight(), cell.getBottom());
                        }

                        needSetLeft = true;
                    }
                }
            }
        } else {
            Log.e(LOG_TAG, " x: " + p_nXPosition + " y: " + p_nYPosition + " child is null. " +
                    "Because first visible item position is  " + findFirstVisibleItemPosition());
        }
        return p_nRight;
    }

    /**
     * Alternative method of fitWidthSize().
     * The main difference is this method works after main thread draw the ui components.
     */
    private void fitWidthSize2() {
        for (int i = columnHeaderLayoutManager.findFirstVisibleItemPosition(); i <
                columnHeaderLayoutManager.findLastVisibleItemPosition() + 1; i++) {
            fitSize2(i);
        }

        needSetLeft = false;
    }

    private void fitSize2(int p_nPosition) {
        int nColumnCacheWidth = columnHeaderLayoutManager.getCacheWidth(p_nPosition);
        View column = columnHeaderLayoutManager.findViewByPosition(p_nPosition);

        if (column != null) {
            // Loop for all rows which are visible.
            for (int j = findFirstVisibleItemPosition(); j < findLastVisibleItemPosition() + 1;
                 j++) {
                fit2(p_nPosition, j, nColumnCacheWidth, column);
            }
        }
    }

    private void fit2(int p_nXPosition, int p_nYPosition, int p_nColumnCachedWidth, View
            p_jColumn) {
        CellRecyclerView child = (CellRecyclerView) findViewByPosition(p_nYPosition);
        if (child != null) {
            ColumnLayoutManager childLayoutManager = (ColumnLayoutManager) child.getLayoutManager();

            int nCellCacheWidth = childLayoutManager.getCacheWidth(p_nXPosition);
            View cell = childLayoutManager.findViewByPosition(p_nXPosition);

            // Control whether the cell needs to be fitted by column header or not.
            if (cell != null) {

                if (nCellCacheWidth != p_nColumnCachedWidth || needSetLeft) {

                    // This is just for setting width value
                    if (nCellCacheWidth != p_nColumnCachedWidth) {
                        nCellCacheWidth = p_nColumnCachedWidth;
                        TableViewUtils.setWidth(cell, nCellCacheWidth);
                        childLayoutManager.setCacheWidth(p_nXPosition, nCellCacheWidth);
                    }

                    // The left & right values of Column header can be considered. Because this
                    // method will be worked
                    // after drawing process of main thread.
                    if (p_jColumn.getLeft() != cell.getLeft() || p_jColumn.getRight() != cell
                            .getRight()) {
                        // TODO: + 1 is for decoration item. It should be gotten from a generic
                        // method  of layoutManager
                        // Set right & left values
                        cell.setLeft(p_jColumn.getLeft());
                        cell.setRight(p_jColumn.getRight() + 1);
                        childLayoutManager.layoutDecoratedWithMargins(cell, cell.getLeft(), cell
                                .getTop(), cell.getRight(), cell.getBottom());

                        needSetLeft = true;
                    }
                }
            }
        }
    }


    public boolean shouldFitColumns(int p_nYPosition) {

        // Scrolling horizontally
        if (cellRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
            CellRecyclerView cellRecyclerView = (CellRecyclerView) findViewByPosition(p_nYPosition);
            if (!cellRecyclerView.isScrollOthers()) {

                int nLastVisiblePosition = findLastVisibleItemPosition();
                CellRecyclerView lastCellRecyclerView = (CellRecyclerView) findViewByPosition
                        (nLastVisiblePosition);

                if (lastCellRecyclerView != null) {
                    if (p_nYPosition == nLastVisiblePosition) {
                        return true;
                    } else if (lastCellRecyclerView.isScrollOthers() && p_nYPosition ==
                            nLastVisiblePosition - 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    @Override
    public void measureChildWithMargins(View child, int widthUsed, int heightUsed) {
        super.measureChildWithMargins(child, widthUsed, heightUsed);

        int nPosition = getPosition(child);

        ColumnLayoutManager childLayoutManager = (ColumnLayoutManager) ((CellRecyclerView) child)
                .getLayoutManager();

        // the below codes should be worked when it is scrolling vertically
        if (cellRecyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
            if (childLayoutManager.isNeedFit()) {

                // Scrolling up
                if (m_nLastDy < 0) {
                    Log.e(LOG_TAG, nPosition + " fitWidthSize all vertically up");
                    fitWidthSize(true);
                } else {
                    // Scrolling down
                    Log.e(LOG_TAG, nPosition + " fitWidthSize all vertically down");
                    fitWidthSize(false);
                }
                // all columns have been fitted.
                childLayoutManager.clearNeedFit();
            }

            // That means,populating for the first time like fetching all data to display.
            // It shouldn't be worked when it is scrolling horizontally ."getLastDx() == 0"
            // control for it.
        } else if (cellRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE &&
                childLayoutManager.getLastDx() == 0) {

            if (childLayoutManager.isNeedFit()) {
                m_bNeedFit = true;

                // all columns have been fitted.
                childLayoutManager.clearNeedFit();
            }

            if (m_bNeedFit) {
                // for the first time to populate adapter
                /**      if (m_jRowHeaderLayoutManager.findLastVisibleItemPosition() == nPosition) {

                 // The below line helps to change left & right value of the each column
                 // header views
                 // without using requestLayout().
                 columnHeaderLayoutManager.customRequestLayout();

                 fitWidthSize(false);
                 Log.e(LOG_TAG, nPosition + " fitWidthSize populating data for the first time");

                 m_bNeedFit = false;
                 }**/
            }
        }
    }


    public AbstractViewHolder[] getVisibleCellViewsByRowPosition(int p_nYPosition) {
        CellRecyclerView cellRowRecyclerView = (CellRecyclerView) iTableView
                .getCellLayoutManager().findViewByPosition(p_nYPosition);
        if (cellRowRecyclerView != null) {
            return ((ColumnLayoutManager) cellRowRecyclerView.getLayoutManager())
                    .getVisibleViewHolders();
        }
        return null;
    }

    public AbstractViewHolder[] getVisibleCellViewsByColumnPosition(int p_nXPosition) {
        int nVisibleChildCount = findLastVisibleItemPosition() - findFirstVisibleItemPosition() + 1;
        int nIndex = 0;
        AbstractViewHolder[] viewHolders = new AbstractViewHolder[nVisibleChildCount];
        for (int i = findFirstVisibleItemPosition(); i < findLastVisibleItemPosition() + 1; i++) {
            CellRecyclerView cellRowRecyclerView = (CellRecyclerView) findViewByPosition(i);

            AbstractViewHolder holder = (AbstractViewHolder) cellRowRecyclerView
                    .findViewHolderForAdapterPosition(p_nXPosition);

            viewHolders[nIndex] = holder;

            nIndex++;
        }
        return viewHolders;
    }

    public AbstractViewHolder getCellViewHolder(int p_nXPosition, int p_nYPosition) {
        CellRecyclerView cellRowRecyclerView = (CellRecyclerView) findViewByPosition(p_nYPosition);

        if (cellRowRecyclerView != null) {
            return (AbstractViewHolder) cellRowRecyclerView.findViewHolderForAdapterPosition
                    (p_nXPosition);
        }
        return null;
    }

}
