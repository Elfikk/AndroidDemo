package com.example.demo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DataView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DataView extends Fragment {

    boolean isLoading = false;
    RecyclerView recyclerView;
    ListViewAdapter adapter;
    List<String> numbas;
    DBHelper Helper;
    SQLiteDatabase DB;


    public DataView() {
        // Required empty public constructor
    }

    public static DataView newInstance() {
        DataView fragment = new DataView();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Helper = new DBHelper(getContext());
        DB = Helper.getReadableDatabase();

        numbas = initData(Helper, DB, "0", "10");
        initialiseRecycler();
        initScrollListener();
    }

    public List<String> initData(DBHelper Helper, SQLiteDatabase DB, String minID, String maxID) {
        String[] projection = {
                BaseColumns._ID,
                DBContract.ExampleColumns.COLUMN_NAME_TITLE
        };

        String selection = BaseColumns._ID + " < ? AND " + BaseColumns._ID + " > ?";
        String[] selectionArgs = {maxID, minID};

        Cursor cursor = DB.query(
                DBContract.ExampleColumns.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null                    // don't sort
        );

        numbas = new ArrayList<String>();
        while (cursor.moveToNext()) {
            String numba = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.ExampleColumns.COLUMN_NAME_TITLE));
            numbas.add(numba);
        }
        cursor.close();
        //String[] data = numbas.toArray(new String[0]);

        return numbas;
    }

    public void initialiseRecycler() {
        // set up the RecyclerView
        recyclerView = getView().findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ListViewAdapter(numbas);
        recyclerView.setAdapter(adapter);
    }

    public void getData(String minID, String maxID) {

        String[] projection = {
                BaseColumns._ID,
                DBContract.ExampleColumns.COLUMN_NAME_TITLE
        };

        String selection = BaseColumns._ID + " < ? AND " + BaseColumns._ID + " > ?";
        String[] selectionArgs = {maxID, minID};

        Cursor cursor = DB.query(
                DBContract.ExampleColumns.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null                    // don't sort
        );

        while (cursor.moveToNext()) {
            String numba = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.ExampleColumns.COLUMN_NAME_TITLE));
            numbas.add(numba);
        }
        cursor.close();
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                System.out.println("Scrolling");

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == adapter.getItemCount() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore() {
        numbas.add(null);
        adapter.notifyItemInserted(numbas.size() - 1);
        System.out.println("Progress bar should show up.");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                numbas.remove(numbas.size() - 1);
                int scrollPosition = numbas.size();
                adapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                getData(Integer.toString(currentSize), Integer.toString(nextLimit));

                adapter.notifyItemRangeInserted(scrollPosition, numbas.size());
                //adapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);

    }
}