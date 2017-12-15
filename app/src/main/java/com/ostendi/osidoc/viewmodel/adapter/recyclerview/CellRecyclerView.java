package com.ostendi.osidoc.viewmodel.adapter.recyclerview;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ostendi.osidoc.viewmodel.HorizontalRecyclerViewListener;
import com.ostendi.osidoc.viewmodel.VerticalRecyclerViewListener;
import com.ostendi.osidoc.viewmodel.adapter.recyclerview.holder.AbstractViewHolder;

public class CellRecyclerView extends RecyclerView {
    private static final String LOG_TAG = CellRecyclerView.class.getSimpleName();

    private int scrolldx = 0;
    private int scrolldY = 0;

    private boolean horizontalScrollListenerRemoved = true;
    private boolean verticalscrollListenerRemoved = true;

    public CellRecyclerView(Context context) {
        super(context);

        this.setHasFixedSize(false);
        this.setNestedScrollingEnabled(false);
        /*this.setItemViewCacheSize(100);
        this.setDrawingCacheEnabled(true);
        this.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);*/
    }

    @Override
    public void onScrolled(int dx, int dy) {
        scrolldx += dx;
        scrolldY += dy;

        super.onScrolled(dx, dy);
    }

    public int getScrolledX() {
        return scrolldx;
    }

    public void clearScrolledX() {
        scrolldx = 0;
    }

    public int getScrolledY() { return scrolldY; }

/** addOnScrollListener :Add a listener that will be notified of any changes in scroll state or position.
Components that add a listener should take care to remove it when finished.**/
    @Override
    public void addOnScrollListener(OnScrollListener listener) {
        if (listener instanceof HorizontalRecyclerViewListener) {
            if (horizontalScrollListenerRemoved) {
                horizontalScrollListenerRemoved = false;
                super.addOnScrollListener(listener);
            } else {
                // Do not let add the listener
                Log.w(LOG_TAG, "horizontalScrollListenerRemoved has been tried to add itself "
                        + "before remove the old one");
            }
        } else if (listener instanceof VerticalRecyclerViewListener) {
            if (verticalscrollListenerRemoved) {
                verticalscrollListenerRemoved = false;
                super.addOnScrollListener(listener);
            } else {
                // Do not let add the listener
                Log.w(LOG_TAG, "verticalscrollListenerRemoved has been tried to add itself " +
                        "before remove the old one");
            }
        } else {
            super.addOnScrollListener(listener);
        }
    }

    @Override
    public void removeOnScrollListener(OnScrollListener listener) {
        if (listener instanceof HorizontalRecyclerViewListener) {
            if (horizontalScrollListenerRemoved) {
                // Do not let remove the listener
                Log.e(LOG_TAG, "HorizontalRecyclerViewListener has been tried to remove " +
                        "itself before add new one");
            } else {
                horizontalScrollListenerRemoved = true;
                super.removeOnScrollListener(listener);
            }
        } else if (listener instanceof VerticalRecyclerViewListener) {
            if (verticalscrollListenerRemoved) {
                // Do not let remove the listener
                Log.e(LOG_TAG, "verticalscrollListenerRemoved has been tried to remove " +
                        "itself before add new one");
            } else {
                verticalscrollListenerRemoved = true;
                super.removeOnScrollListener(listener);
            }
        } else {
            super.removeOnScrollListener(listener);
        }
    }

    public boolean isHorizontalScrollListenerRemoved() {
        return horizontalScrollListenerRemoved;
    }

    public boolean isScrollOthers() {
        return !horizontalScrollListenerRemoved;
    }

    public void setSelected(boolean p_bSelected, @ColorInt int p_nBackgroundColor) {
        for (int i = 0; i < getAdapter().getItemCount(); i++) {
            AbstractViewHolder viewHolder = (AbstractViewHolder) findViewHolderForAdapterPosition
                    (i);
            if (viewHolder != null) {
                // Change background color
                viewHolder.setBackgroundColor(p_nBackgroundColor);

                // Change selection status of the view holder
                viewHolder.setSelected(p_bSelected);
            }
        }
    }

}
