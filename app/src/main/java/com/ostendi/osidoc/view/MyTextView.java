package com.ostendi.osidoc.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by jitendra on 23/11/2017.
 */

public class MyTextView extends android.support.v7.widget.AppCompatTextView {


    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public MyTextView(Context context) {
        super(context);

    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.DEFAULT;

            switch (getTypeface().getStyle()) {
                case Typeface.BOLD:
                    tf = Typeface.createFromAsset(getContext().getAssets(), "Exo/Exo-Bold.ttf");
                    break;

                case Typeface.ITALIC:
                    tf = Typeface.createFromAsset(getContext().getAssets(), "Exo/Exo-Italic.ttf");
                    break;

                case Typeface.BOLD_ITALIC:
                    tf = Typeface.createFromAsset(getContext().getAssets(), "Exo/Exo-BoldItalic.ttf");
                    break;

                default:
                    tf = Typeface.createFromAsset(getContext().getAssets(), "Exo/Exo-Medium.ttf");
                    break;
            }

            setTypeface(tf);
        }
    }}

