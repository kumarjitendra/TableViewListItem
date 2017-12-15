package com.ostendi.osidoc.viewmodel;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.ostendi.osidoc.viewmodel.adapter.recyclerview.CellRecyclerView;
import com.ostendi.osidoc.viewmodel.handler.SelectionHandler;
import com.ostendi.osidoc.viewmodel.layoutmanager.CellLayoutManager;
import com.ostendi.osidoc.viewmodel.layoutmanager.ColumnHeaderLayoutManager;

public interface ITableView {

    void addView(View child, ViewGroup.LayoutParams params);

    CellRecyclerView getCellRecyclerView();

    CellRecyclerView getColumnHeaderRecyclerView();

    CellRecyclerView getRowHeaderRecyclerView();

    ColumnHeaderLayoutManager getColumnHeaderLayoutManager();

    CellLayoutManager getCellLayoutManager();

    LinearLayoutManager getRowHeaderLayoutManager();

    HorizontalRecyclerViewListener getHorizontalRecyclerViewListener();

    VerticalRecyclerViewListener getVerticalRecyclerViewListener();

    ITableViewListener getTableViewListener();

    SelectionHandler getSelectionHandler();

    int getShadowColor();

    int getSelectedColor();

    int getUnSelectedColor();
}
