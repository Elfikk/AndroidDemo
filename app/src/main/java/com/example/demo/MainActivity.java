package com.example.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Cant getContext() ? Seems to be an AppCompatActivity thing.
        Context context = MainActivity.this;
        DBHelper Helper = new DBHelper(context);
        SQLiteDatabase DB = Helper.getWritableDatabase();

        // View RootView = findViewById(android.R.id.content).getRootView();

        Button ButtonSubmit = (Button) findViewById(R.id.button_submit);
        EditText TextEditor = (EditText) findViewById(R.id.text_editor);

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
                    DB.insert(DBContract.ExampleColumns.TABLE_NAME, null, values);
                    ConfirmMessage.setText(OutputText);
                    ConfirmMessage.show();
                    TextEditor.setText("");
                }

                //ConfirmMessage.setText(OutputText);
                //ConfirmMessage.show();
                //TextEditor.setText("");

            }
        });
    }




}