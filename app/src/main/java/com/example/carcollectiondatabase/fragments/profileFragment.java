package com.example.carcollectiondatabase.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carcollectiondatabase.DatabaseHelper;
import com.example.carcollectiondatabase.R;

import java.util.ArrayList;

public class profileFragment extends Fragment {

    DatabaseHelper dbhelper = new DatabaseHelper(getContext());
    ProgressBar colorful;
    ProgressBar million;
    TextView millionTitle;

    @SuppressLint("Range")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView fastest = view.findViewById(R.id.fastest);
        TextView fave_brand = view.findViewById(R.id.favBrand);
        TextView oldest = view.findViewById(R.id.oldest);
        TextView millionTitle = view.findViewById(R.id.MillionaireTitle);
        colorful = view.findViewById(R.id.colorProgress);
        million = view.findViewById(R.id.MillionaireProgress);

        DatabaseHelper dbhelper = new DatabaseHelper(getContext());

        ArrayList<String> fastestEntry = dbhelper.searchFastest();
        if (fastestEntry.get(1) != null && fastestEntry.get(2) != null) {
            fastest.setText(fastestEntry.get(1) + " " + fastestEntry.get(2));
        }else{
            Toast.makeText(getContext(), "Add some cars first", Toast.LENGTH_SHORT).show();
        }

        ArrayList<String> mostFreqBrand = dbhelper.searchMostFrequentBrand();
        if (!mostFreqBrand.isEmpty()) {
            fave_brand.setText(mostFreqBrand.get(1));
        }

        ArrayList<String> oldestVehicle = dbhelper.getOldest();
        if (oldestVehicle.get(0) != null){
            oldest.setText(oldestVehicle.get(0)+" "+oldestVehicle.get(1)+" from "+oldestVehicle.get(2));
        }

        colorful.setMax(13);
        colorful.setProgress(Integer.parseInt(dbhelper.getDistinctColors().get(0)));

        million.setMax(1000000);
        million.setProgress(Math.min(dbhelper.getTotalValue(dbhelper.getPrices()), 1000000));



        return view;
    }

    @Override
    public void onResume() {
        DatabaseHelper dbhelper = new DatabaseHelper(getContext());
        colorful.setProgress(Integer.parseInt(dbhelper.getDistinctColors().get(0)));
        million.setProgress(Math.min(dbhelper.getTotalValue(dbhelper.getPrices()), 1000000));
        super.onResume();
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