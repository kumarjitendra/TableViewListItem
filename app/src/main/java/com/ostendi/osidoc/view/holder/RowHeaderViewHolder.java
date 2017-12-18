package com.ostendi.osidoc.view.holder;


/**
 * Created by jitendra on 21/11/2017.
 */

import android.view.View;
import android.widget.RadioButton;

import com.ostendi.osidoc.R;
import com.ostendi.osidoc.view.adapter.recyclerview.holder.AbstractViewHolder;

public class RowHeaderViewHolder extends AbstractViewHolder {
    private RadioButton radioButton;

    public RowHeaderViewHolder(View p_jItemView) {
        super(p_jItemView);
        radioButton = (RadioButton) p_jItemView.findViewById(R.id.radio);
    }
}