package com.nathan.app.weblinklist.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nathan.app.weblinklist.R;
import com.nathan.app.weblinklist.databinding.RowLinkBinding;
import com.nathan.app.weblinklist.model.WebLink;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.LinkViewHolder> {

    private SelectionTracker<Long> tracker;

    private List<WebLink> list = new ArrayList<>();

    private boolean ascending = true;

    LinkAdapter() {
        this.setHasStableIds(true);
    }

    @NonNull
    @Override
    public LinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowLinkBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_link, parent, false);
        return new LinkViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LinkViewHolder holder, int position) {
        WebLink webLink = list.get(position);
        holder.binding.setWebLink(webLink);
        boolean selected = tracker.isSelected(webLink.getCreated());
        holder.binding.setIsSelected(selected);
        holder.binding.getRoot().setOnClickListener(v -> {
            if (!selected) {
                tracker.select(webLink.getCreated());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getCreated();
//        return position;
    }

    int add(WebLink webLink) {
        List<WebLink> newList = new ArrayList<>(list);
        newList.add(webLink);
        Collections.sort(newList);
        if (!ascending) {
            Collections.reverse(newList);
        }
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new WebLinkDiffCallback(this.list, newList));
        result.dispatchUpdatesTo(this);
        this.list = newList;
        return this.list.indexOf(webLink);
    }

    void deleteSelected() {
        List<WebLink> newList = new ArrayList<>();
        for (WebLink webLink : this.list) {
            if (!tracker.getSelection().contains(webLink.getCreated())) {
                newList.add(webLink);
            }
        }
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new WebLinkDiffCallback(this.list, newList));
        result.dispatchUpdatesTo(this);
        this.list = newList;
        tracker.clearSelection();
    }

    boolean getAscending() {
        return this.ascending;
    }

    void setAscending(boolean ascending) {
        List<WebLink> newList = new ArrayList<>(list);
        this.ascending = ascending;
        Collections.reverse(newList);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new WebLinkDiffCallback(this.list, newList));
        result.dispatchUpdatesTo(this);
        this.list = newList;
    }

    void setTracker(SelectionTracker<Long> tracker) {
        this.tracker = tracker;
    }

    static class LinkViewHolder extends RecyclerView.ViewHolder {

        RowLinkBinding binding;

        LinkViewHolder(@NonNull RowLinkBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        ItemDetailsLookup.ItemDetails<Long> getItemDetails() {
            return new ItemDetailsLookup.ItemDetails<Long>() {
                @Override
                public int getPosition() {
                    return getAdapterPosition();
                }

                @Override
                public Long getSelectionKey() {
                    return getItemId();
                }
            };
        }
    }

    static class WebLinkDiffCallback extends DiffUtil.Callback {

        List<WebLink> oldList;
        List<WebLink> newList;

        WebLinkDiffCallback(List<WebLink> oldList, List<WebLink> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getCreated() == newList.get(newItemPosition).getCreated();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }
    }

}
