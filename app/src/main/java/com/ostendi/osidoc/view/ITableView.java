package com.ostendi.osidoc.view;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.ostendi.osidoc.view.adapter.recyclerview.CellRecyclerView;
import com.ostendi.osidoc.view.handler.SelectionHandler;
import com.ostendi.osidoc.view.layoutmanager.CellLayoutManager;
import com.ostendi.osidoc.view.layoutmanager.ColumnHeaderLayoutManager;
import com.ostendi.osidoc.view.listener.HorizontalRecyclerViewListener;
import com.ostendi.osidoc.view.listener.ITableViewListener;
import com.ostendi.osidoc.view.listener.VerticalRecyclerViewListener;

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
