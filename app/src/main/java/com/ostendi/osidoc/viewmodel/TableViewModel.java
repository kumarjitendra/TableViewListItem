package com.ostendi.osidoc.viewmodel;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import android.content.Context;
import android.util.Log;

import com.ostendi.osidoc.model.FieldDefinition;
import com.ostendi.osidoc.model.Record;
import com.ostendi.osidoc.model.val.Value;
import com.ostendi.osidoc.server.Store;

import java.util.ArrayList;
import java.util.List;

import static com.ostendi.osidoc.view.MainFragment.ROW_SIZE;

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

  /**  public Cell getRowCells(int rowNumber, int cellNumber) {
        List<Cell> cells = new ArrayList<>();
        Record record = store.getRecordAt(rowNumber);

        String fieldId = store.getDefinition().getFieldId(cellNumber);

        Value fieldValue = record.getFieldValue(fieldId);
        return new Cell(fieldId, fieldValue.toString());
    } **/

     public List<Cell> getRowCells(int rowNumber ) {
         List<Cell> cellList = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            Record record =  store.getRecordAt(rowNumber);
            for (FieldDefinition fieldDefinition : store.getDefinition().getFieldDefinitions()) {
                String fieldId = fieldDefinition.getId();
                cellList.add(new Cell(
                        fieldId,
                        record.getFieldValue(fieldId).toString()
                ));
            }

        } return cellList;}


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


