package com.ostendi.osidoc.server;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.ostendi.osidoc.model.Page;
import com.ostendi.osidoc.model.Record;
import com.ostendi.osidoc.model.StoreDefinition;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.databinding.adapters.NumberPickerBindingAdapter.setValue;

public class Store extends LiveData<List<Record>> {

    private FakeHttpServer fakeServer = new FakeHttpServer(500);

    private String storeId;
    private int pageSize;

    private StoreDefinition storeDefinition;

    private boolean isLoadMoreRecords = true;
    private  List<Record> loadedRecords = new ArrayList<>();

    public Store(String storeId, int pageSize) {
        this.storeId = storeId;
        this.pageSize = pageSize;
        this.storeDefinition = ServerDataDeserializer.deserializeStoreDefinition(
                fakeServer.fetchStoreDefinition(storeId)
        );
    }

    public Record getRecordAt(int recordNumber) {
        return loadedRecords.get(recordNumber);
    }

    public boolean loadNextPage() {
        if (!isLoadMoreRecords) {
            return false;
        }
        /*  start thread to retrieve page from fakeServer
                once got result from server, set the value
                */
        new AsyncTask<URL, Integer, Page>() {

            @Override
            protected Page doInBackground(URL... urls) {
               try {
                    Thread.currentThread();
                    Thread.sleep(50);
                } catch (InterruptedException e) {

                }
                Page page = ServerDataDeserializer.deserializePage(
                        storeDefinition,
                        fakeServer.fetchPage(storeId, getLoadedRecordCount(), pageSize)
                );
                return page;
            }

            protected void onProgressUpdate(Integer... progress) {}

            protected void onPostExecute(Page page) {
                addNewRecords(page);
            }
        }.execute();

        return true;
    }
    private void addNewRecords(Page page) {
        isLoadMoreRecords = page.hasMore();
        if (isLoadMoreRecords) {
            List<Record> newRecords = page.getRecords();
            loadedRecords.addAll(newRecords);
            setValue(loadedRecords);
        }
    }

    public StoreDefinition getDefinition() {
        return storeDefinition;
    }

    public int getLoadedRecordCount() {
        return loadedRecords.size();
    }

}

