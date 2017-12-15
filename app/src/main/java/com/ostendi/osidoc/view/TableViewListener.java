package com.ostendi.osidoc.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.ostendi.osidoc.viewmodel.ITableViewListener;
import com.ostendi.osidoc.viewmodel.TableView;
/**
 * Created by jitendra on 21/11/2017.
 */

public class TableViewListener implements ITableViewListener {

    private Toast m_jToast;
    private Context m_jContext;

    public TableViewListener(TableView p_jTableView) {
        this.m_jContext = p_jTableView.getContext();

    }

    private void showToast(String p_strMessage) {
        if (m_jToast == null) {
            m_jToast = Toast.makeText(m_jContext, "", Toast.LENGTH_SHORT);
        }

        m_jToast.setText(p_strMessage);
        m_jToast.show();
    }

    @Override
    public void onCellClicked(@NonNull RecyclerView.ViewHolder p_jCellView, int p_nXPosition, int
            p_nYPosition) {
        showToast("Cell " + p_nXPosition + " " + p_nYPosition + " has been selected.");
    }


    @Override
    public void onColumnHeaderClicked(@NonNull RecyclerView.ViewHolder p_jColumnHeaderView, int
            p_nXPosition) {
        showToast("Column " + p_nXPosition + " has been selected.");
    }

    @Override
    public void onRowHeaderClicked(@NonNull RecyclerView.ViewHolder p_jRowHeaderView, int
            p_nYPosition) {
        showToast("Record  " + p_nYPosition + " has been selected.");
    }

}
