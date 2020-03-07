package com.nathan.app.weblinklist.ui;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

public class WebLinkDetailsLookup extends ItemDetailsLookup<Long> {

    private RecyclerView recyclerView;

    WebLinkDetailsLookup(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Nullable
    @Override
    public ItemDetails<Long> getItemDetails(@NonNull MotionEvent e) {
        View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (view != null) {
            LinkAdapter.LinkViewHolder viewHolder = (LinkAdapter.LinkViewHolder) recyclerView.getChildViewHolder(view);
            if (viewHolder != null) {
                return viewHolder.getItemDetails();
            } else {
                return null;
            }
        }
        return null;
    }
}
