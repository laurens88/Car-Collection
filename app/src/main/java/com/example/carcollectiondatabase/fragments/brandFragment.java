package com.example.carcollectiondatabase.fragments;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.carcollectiondatabase.Crawler;
import com.example.carcollectiondatabase.DatabaseHelper;
import com.example.carcollectiondatabase.R;

import java.util.ArrayList;

public class brandFragment extends Fragment {


    @SuppressLint("Range")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_brand, container, false);

//        DatabaseHelper dbhelper = new DatabaseHelper(getContext());
//        Cursor c = dbhelper.search("VOLVO");
//
//        System.out.println(c.getCount());
//        c.moveToFirst();
//        while(c.moveToNext()){
//            System.out.println(c.getString(c.getColumnIndex("car_type")));
//        }

        return view;
    }

    @SuppressLint("Range")
    public String cursorToString(Cursor cursor){
        String cursorString = "";
        if (cursor.moveToFirst() ){
            String[] columnNames = cursor.getColumnNames();
            for (String name: columnNames)
                cursorString += String.format("%s ][ ", name);
            cursorString += "\n";
            do {
                for (String name: columnNames) {
                    cursorString += String.format("%s ][ ",
                            cursor.getString(cursor.getColumnIndex(name)));
                }
                cursorString += "\n";
            } while (cursor.moveToNext());
        }
        return cursorString;
}}