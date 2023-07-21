package com.example.demo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.chaquo.python.PyException;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataPlot extends Fragment implements AdapterView.OnItemSelectedListener {

    String xSelection = "_id"; // getActivity().getString(R.string.id);
    String ySelection = "_id"; //getActivity().getString(R.string.number);

    Spinner xSpinner;
    Spinner ySpinner;

    String selectedSpinner;

    DBHelper Helper;
    SQLiteDatabase DB;

    ArrayList<Integer> xData;
    ArrayList<Integer> yData;

    public DataPlot() {
        // Required empty public constructor
    }

    public static DataPlot newInstance() {
        DataPlot fragment = new DataPlot();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

//        System.out.println("1");
//        System.out.println(view);
//        System.out.println("2");

        if (Objects.equals(selectedSpinner, "x")) {
            xSelection = (String) parent.getItemAtPosition(pos);
        } else if (Objects.equals(selectedSpinner, "y")) {
            ySelection = (String) parent.getItemAtPosition(pos);
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageView plotImage = getView().findViewById(R.id.plot_view);

        Button ButtonPlot = (Button) getView().findViewById(R.id.plot_button);

        Snackbar confirmMessage = Snackbar.make(ButtonPlot, R.string.snackbar_text, BaseTransientBottomBar.LENGTH_LONG);

        Helper = new DBHelper(getContext());
        DB = Helper.getReadableDatabase();

        xSpinner = (Spinner) getView().findViewById(R.id.x_axis_spinner);
        ySpinner = (Spinner) getView().findViewById(R.id.y_axis_spinner);

        xSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                selectedSpinner = "x";
                return false;
            }
        });

        ySpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                selectedSpinner = "y";
                return false;
            }
        });

        spinnerAdapterSetup();

        ButtonPlot.setOnClickListener(new View.OnClickListener() {
            public void onClick(View root_view) {
                String spinnerStatus = "x " + xSelection + " y " + ySelection;
                confirmMessage.setText(spinnerStatus);
                confirmMessage.show();

                if (!Python.isStarted()) {
                    Python.start(new AndroidPlatform(getContext()));
                }

                Python py = Python.getInstance();
                PyObject module = py.getModule("plot_example");

                try {
                    String dbpath = getContext().getDatabasePath(DBContract.ExampleColumns.TABLE_NAME).toString()
                            + DBContract.ExampleColumns.TABLE_NAME;

                    getData();

                    byte[] bytes = module.callAttr("plot", xData.toArray(), yData.toArray(),
                                    xSelection, ySelection).toJava(byte[].class);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    ((ImageView) getView().findViewById(R.id.plot_view)).setImageBitmap(bitmap);
                } catch (PyException e) {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data_plot, container, false);
    }

    private void spinnerAdapterSetup() {
        ArrayAdapter<CharSequence> xAdapter = ArrayAdapter.createFromResource(getContext()   ,
                R.array.column_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        xAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        xSpinner.setAdapter(xAdapter);
        xSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> yAdapter = ArrayAdapter.createFromResource(getContext()   ,
                R.array.column_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        yAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        ySpinner.setAdapter(yAdapter);
        ySpinner.setOnItemSelectedListener(this);
    }

    public void getData() {



        String[] projection = {
                xSelection,
                ySelection
        };

        Cursor cursor = DB.query(
                DBContract.ExampleColumns.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,                   // The columns for the WHERE clause
                null,                   // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null                    // don't sort
        );

        xData = new ArrayList<Integer>();
        yData = new ArrayList<Integer>();

        while (cursor.moveToNext()) {
            Integer xRow = cursor.getInt(cursor.getColumnIndexOrThrow(xSelection));
            xData.add(xRow);

            Integer yRow = cursor.getInt(cursor.getColumnIndexOrThrow(ySelection));
            yData.add(yRow);
        }
        cursor.close();

    }

}