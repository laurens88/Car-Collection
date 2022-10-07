package com.example.carcollectiondatabase.fragments;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.carcollectiondatabase.DatabaseHelper;
import com.example.carcollectiondatabase.R;

import java.util.ArrayList;

public class profileFragment extends Fragment {


    @SuppressLint("Range")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView fastest = view.findViewById(R.id.fastest);
        TextView fave_brand = view.findViewById(R.id.favBrand);

        DatabaseHelper dbhelper = new DatabaseHelper(getContext());

        ArrayList<String> fastestEntry = dbhelper.searchFastest();
        fastest.setText(fastestEntry.get(1)+" "+fastestEntry.get(2));

        ArrayList<String> mostFreqBrand = dbhelper.searchMostFrequentBrand();
        fave_brand.setText(mostFreqBrand.get(1));

        System.out.println(dbhelper.getDistinctColors());
        System.out.println(dbhelper.getTotalValue(dbhelper.getPrices()));



        return view;
    }

    @SuppressLint("Range")
    public ArrayList<String> cursorToArrayList(Cursor cursor){
        ArrayList<String> cursorList = new ArrayList<>();
        if (cursor.moveToFirst() ){
            String[] columnNames = cursor.getColumnNames();
            do {
                for (String name: columnNames) {
                    cursorList.add(cursor.getString(cursor.getColumnIndex(name)));
                }
            } while (cursor.moveToNext());
        }
        return cursorList;
    }

}