package com.ostendi.osidoc.viewmodel.layoutmanager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.SparseArray;
import android.view.View;

import com.ostendi.osidoc.viewmodel.util.TableViewUtils;

public class ColumnHeaderLayoutManager extends LinearLayoutManager {
    //SparseArrays map integers to Objects.
    private SparseArray<Integer> widthList;

    public ColumnHeaderLayoutManager(Context context) {
        super(context);
        widthList = new SparseArray<>();

        this.setOrientation(ColumnHeaderLayoutManager.HORIZONTAL);
    }
//measureChildWithMargins :Measure a child view using standard measurement policy,
// taking the padding of the parent RecyclerView, any added item decorations and the child margins into account.
    @Override
    public void measureChildWithMargins(View child, int widthUsed, int heightUsed) {
        super.measureChildWithMargins(child, widthUsed, heightUsed);
        measureChild(child, widthUsed, heightUsed);
    }

    @Override
    public void measureChild(View child, int widthUsed, int heightUsed) {
        int position = getPosition(child);
        int cacheWidth = getCacheWidth(position);

        // If the width value of the cell has already calculated, then set the value
        if (cacheWidth != -1) {
            TableViewUtils.setWidth(child, cacheWidth);
        } else {
            super.measureChild(child, widthUsed, heightUsed);
        }
    }


    public void setCacheWidth(int position, int width) {
        widthList.put(position, width);
    }

    public int getCacheWidth(int position) {
        Integer cachewidth = widthList.get(position);
        if (cachewidth != null) {
            return widthList.get(position);
        }
        return -1;
    }

    public int getFirstItemLeft() {
        View firstColumnHeader = findViewByPosition(findFirstVisibleItemPosition());
        return firstColumnHeader.getLeft();
    }

    /**
     * Helps to recalculate the width value of the cell that is located in given position.
     */
    public void removeCachedWidth(int p_nPosition) {
        widthList.remove(p_nPosition);
    }


    public void customRequestLayout() {
        int left = getFirstItemLeft();
        int right;
        for (int i = findFirstVisibleItemPosition(); i < findLastVisibleItemPosition() + 1; i++) {
            right = left + getCacheWidth(i);

            View columnHeader = findViewByPosition(i);
            columnHeader.setLeft(left);
            columnHeader.setRight(right);

            layoutDecoratedWithMargins(columnHeader, columnHeader.getLeft(),
                    columnHeader.getTop(), columnHeader.getRight(), columnHeader.getBottom());

            left = right + 1;
        }
    }
}
