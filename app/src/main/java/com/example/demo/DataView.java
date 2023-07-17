package com.example.demo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        DBHelper Helper = new DBHelper(getContext());
        SQLiteDatabase DB = Helper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                DBContract.ExampleColumns.COLUMN_NAME_TITLE
        };

        String selection = BaseColumns._ID + " < ?";
        String[] selectionArgs = { "20" };

        Cursor cursor = DB.query(
                DBContract.ExampleColumns.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null                    // don't sort
        );

        List<String> numbas = new ArrayList<>();
        while (cursor.moveToNext()) {
            String numba = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.ExampleColumns.COLUMN_NAME_TITLE));
            numbas.add(numba);
        }
        cursor.close();
        String[] data = numbas.toArray(new String[0]);

        // set up the RecyclerView
        RecyclerView recyclerView = getView().findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ListViewAdapter adapter = new ListViewAdapter(data);
        //adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

    }
}