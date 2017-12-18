package com.ostendi.osidoc.viewmodel;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import android.content.Context;
import android.util.Log;

import com.ostendi.osidoc.model.FieldDefinition;
import com.ostendi.osidoc.model.Record;
import com.ostendi.osidoc.server.Store;
import com.ostendi.osidoc.view.TableView;

import java.util.ArrayList;
import java.util.List;
/*
 * Manage the data for view(UI Controller(Ex:fragment...))
 * but never access the view hierarchy or hold a reference back to fragment
 * Table View model must not know anything about android except android.arch*
*/

public class TableViewModel extends AndroidViewModel {

    private Store store;
    private Context context;
    public TableView tableView;

    public TableViewModel(Context context, Store store, Application application) {
        super(application);
        this.context = context;
        this.store = store;
    }

    public List<ColumnHeader> getColumnHeaders() {
        List<ColumnHeader> columnHeaders = new ArrayList<>();
        for (FieldDefinition fieldDefinition : store.getDefinition().getFieldDefinitions()) {
            columnHeaders.add(new ColumnHeader(fieldDefinition.getId(), fieldDefinition.getLabel()));
        }
        return columnHeaders;
    }
    public boolean loadNextRows() {
        return store.loadNextPage();
    }

    /**
     * Expose the LiveData Record  so the Fragment can observe it.
     */
    public LiveData<List<Record>> getStore() {
        Log.e("liveRecord", String.valueOf(store));
       return store;
   }

}


