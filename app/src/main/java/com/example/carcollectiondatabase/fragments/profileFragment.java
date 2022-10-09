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
    ProgressBar completionist;
    TextView fastest, fave_brand, oldest, color, completion, millionaire;

    @SuppressLint("Range")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        fastest = view.findViewById(R.id.fastest);
        fave_brand = view.findViewById(R.id.favBrand);
        oldest = view.findViewById(R.id.oldest);
        color = view.findViewById(R.id.colorProgressText);
        completion = view.findViewById(R.id.textView14);
        millionaire = view.findViewById(R.id.millionProgressText);
        colorful = view.findViewById(R.id.colorProgress);
        million = view.findViewById(R.id.MillionaireProgress);
        completionist = view.findViewById(R.id.completionistProgress);


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
        int colors = Integer.parseInt(dbhelper.getDistinctColors().get(0));
        colorful.setProgress(Math.min(colors, colorful.getMax()));
        color.setText(colors+"/"+13);

        million.setMax(1000000);
        int totalValue = dbhelper.getTotalValue(dbhelper.getPrices());
        million.setProgress(Math.min(totalValue, 1000000));
        millionaire.setText(Math.min(totalValue,1000000)/10000+"%");

        completionist.setMax(40);
        int brands = Integer.parseInt(dbhelper.getDistinctBrands().get(0));
        completionist.setProgress(Math.min(brands, 40));
        completion.setText(brands+"/"+40);

        return view;
    }

    @Override
    public void onResume() {
        DatabaseHelper dbhelper = new DatabaseHelper(getContext());
        int colors = Integer.parseInt(dbhelper.getDistinctColors().get(0));
        colorful.setProgress(Math.min(colors, 13));
        color.setText(colors+"/"+13);
        int totalValue = dbhelper.getTotalValue(dbhelper.getPrices());
        million.setProgress(Math.min(totalValue, 1000000));
        millionaire.setText(Math.min(totalValue,1000000)/10000+"%");
        int brands = Integer.parseInt(dbhelper.getDistinctBrands().get(0));
        completionist.setProgress(Math.min(brands, 40));
        completion.setText(brands+"/"+40);
        super.onResume();
    }

}