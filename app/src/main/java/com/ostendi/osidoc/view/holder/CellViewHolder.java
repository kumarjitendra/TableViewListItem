package com.ostendi.osidoc.view.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ostendi.osidoc.R;
import com.ostendi.osidoc.model.val.Value;
import com.ostendi.osidoc.view.adapter.recyclerview.holder.AbstractViewHolder;

/**
 * Created by jitendra on 21/11/2017.
 */

public class CellViewHolder extends AbstractViewHolder {

    public final TextView cell_textview;
    public final LinearLayout cell_container;

    public CellViewHolder(View itemView) {
        super(itemView);
        cell_textview = (TextView) itemView.findViewById(R.id.cell_data);
        cell_container = (LinearLayout) itemView.findViewById(R.id.cell_container);
    }
}