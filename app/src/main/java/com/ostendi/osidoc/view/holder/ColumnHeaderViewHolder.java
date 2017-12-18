package com.ostendi.osidoc.view.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ostendi.osidoc.R;
import com.ostendi.osidoc.view.adapter.recyclerview.holder.AbstractViewHolder;

/**
 * Created by jitendra on 21/11/2017.
 */
public class ColumnHeaderViewHolder extends AbstractViewHolder {
    public final LinearLayout column_header_container;
    public final TextView column_header_textview;

    public ColumnHeaderViewHolder(View itemView) {
        super(itemView);
        column_header_textview = (TextView) itemView.findViewById(R.id.column_header_textView);
        column_header_container = (LinearLayout) itemView.findViewById(R.id
                .column_header_container);
    }

}
