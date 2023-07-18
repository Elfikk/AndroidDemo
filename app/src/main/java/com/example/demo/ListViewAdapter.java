package com.example.demo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;

import java.util.List;

public class ListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<String> localDataSet;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ListViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ListViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.list_text);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView
     */
    public ListViewAdapter(List<String> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_item, viewGroup, false);
            return new ListViewHolder(view);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_loading, viewGroup, false);
            return new LoadingViewHolder(view);
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        if (viewHolder instanceof ListViewHolder) {
            setListItemText((ListViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }
    }

    public void setListItemText(ListViewHolder viewHolder, final int position) {
        viewHolder.getTextView().setText(localDataSet.get(position));
    }

    public void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed
        ;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet == null ? 0 : localDataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        return localDataSet.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

}
