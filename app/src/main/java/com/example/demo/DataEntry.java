package com.example.demo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class DataEntry extends Fragment {

    DBHelper Helper;
    SQLiteDatabase DB;

    public DataEntry() {
        // Required empty public constructor
    }

    public static DataEntry newInstance() {
        DataEntry fragment = new DataEntry();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View RootView = inflater.inflate(R.layout.fragment_data_entry, container, false);
        return RootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Cant getContext() ? Seems to be an AppCompatActivity thing.
        // Context context = MainActivity.this;
        DBHelper Helper = new DBHelper(getContext());
        SQLiteDatabase DB = Helper.getWritableDatabase();

        Button ButtonSubmit = (Button) getView().findViewById(R.id.button_submit);
        EditText TextEditor = (EditText) getView().findViewById(R.id.text_editor);

        //String snackbar = getResources().getString(R.string.mystring);
        // Can pass any View i.e the Button and the Snackbar.make() method figures out the ViewGroup.
        Snackbar ConfirmMessage = Snackbar.make(ButtonSubmit, R.string.snackbar_text, BaseTransientBottomBar.LENGTH_LONG);

        ButtonSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View root_view) {
                String InputText = TextEditor.getText().toString();
                String DefaultText = getString(R.string.snackbar_text);
                String OutputText = String.join(" ", DefaultText, InputText);

                Integer inputNumber = Integer.parseInt(InputText);
                System.out.println(InputText);
                System.out.println(inputNumber);
                if (inputNumber == null) {
                    ConfirmMessage.setText("Invalid Input");
                    ConfirmMessage.show();
                } else {
                    ContentValues values = new ContentValues();
                    values.put(DBContract.ExampleColumns.COLUMN_NAME_TITLE, inputNumber);
                    long db_id = DB.insert(DBContract.ExampleColumns.TABLE_NAME, null, values);
                    System.out.println(db_id);
                    ConfirmMessage.setText(OutputText);
                    ConfirmMessage.show();
                    TextEditor.setText("");
                }
            }

            //ConfirmMessage.setText(OutputText);
            //ConfirmMessage.show();
            //TextEditor.setText("");
        });
    }
}