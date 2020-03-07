package com.nathan.app.weblinklist.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class WebLinkKeyProvider extends ItemKeyProvider<Long> {

    private RecyclerView recyclerView;

    WebLinkKeyProvider(RecyclerView recyclerView) {
        super(ItemKeyProvider.SCOPE_MAPPED);
        this.recyclerView = recyclerView;
    }

    @Nullable
    @Override
    public Long getKey(int position) {
        return Objects.requireNonNull(recyclerView.getAdapter()).getItemId(position);
    }

    @Override
    public int getPosition(@NonNull Long key) {
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForItemId(key);
        if (viewHolder != null) {
            return viewHolder.getLayoutPosition();
        } else {
            return RecyclerView.NO_POSITION;
        }
    }
}
