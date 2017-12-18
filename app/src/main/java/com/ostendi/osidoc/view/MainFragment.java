package com.ostendi.osidoc.view;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.ostendi.osidoc.model.Record;
import com.ostendi.osidoc.server.Store;
import com.ostendi.osidoc.viewmodel.TableViewModel;
import com.ostendi.osidoc.viewmodel.TableView;
import com.ostendi.osidoc.viewmodel.adapter.AbstractTableAdapter;
import com.ostendi.osidoc.viewmodel.Cell;
import com.ostendi.osidoc.viewmodel.ColumnHeader;
import com.ostendi.osidoc.viewmodel.RowHeader;
import com.ostendi.osidoc.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainFragment extends Fragment {

    public static final int ROW_SIZE = 500;

    private List<RowHeader> rowHeaderList;
    private List<ColumnHeader> columnHeaderList;
    private List<Record> listRecord;
    private AbstractTableAdapter abstractTableAdapter;
    private TableView tableView;
    private Application application;

    private Store store = new Store("abc", 50);
    private LiveData<List<Record>> recordListLivedata;

    TableViewModel tableViewModel = new TableViewModel(getContext(),
            store, application);

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        observeLiveData();
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        RelativeLayout fragment_container = (RelativeLayout) view.findViewById(R.id
                .fragment_container);
        tableViewModel.
                // Create Store view
                tableView = createTableView();
        fragment_container.addView(tableView);
        initTableView();
        return view;
    }

    private TableView createTableView() {
        tableView = new TableView(getContext());
       /* Adapter -> Pulls the items in the data model and presents items on the screen.
       i.e.(Content View) via the view Holder. */

        // Set adapter
        abstractTableAdapter = new TableViewAdapter(getContext());
        tableView.setAdapter(abstractTableAdapter);

        // Set layout params
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams
                .MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        tableView.setLayoutParams(layoutParams);

        // Set StoreView listener
        tableView.setTableViewListener(new TableViewListener(tableView) {
        });
        return tableView;
    }

    private void initData() {
        rowHeaderList = new ArrayList<>();
        columnHeaderList = new ArrayList<>();
        listRecord = new ArrayList<>();
    }

    private void initTableView() {
        tableViewModel.loadNextRows();
       List<ColumnHeader> columnHeaders = tableViewModel.getColumnHeaders();
        columnHeaderList.addAll(columnHeaders);
    }

    private void observeLiveData() {
        // Observe TableViewModel
        tableViewModel.getStore().observe(this, new Observer<List<Record>>() {
            @Override
            public void onChanged(@Nullable List<Record> recordList) {
                abstractTableAdapter.setAllItems(columnHeaderList,Collections.emptyList(),recordList);
            }

        });
    }


}

