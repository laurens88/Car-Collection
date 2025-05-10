package com.example.newcarcollection.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newcarcollection.DatabaseHelper;
import com.laurens.newcarcollection.R;

import java.math.BigDecimal;
import java.util.ArrayList;

public class profileFragment extends Fragment {

    DatabaseHelper dbhelper = new DatabaseHelper(getContext());
    ProgressBar colorful;
    ProgressBar million;
    ProgressBar completionist;
    TextView fastest, fave_brand, oldest, color, completion, millionaire, millionaireTitle;

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
        millionaireTitle = view.findViewById(R.id.MillionaireTitle);
        colorful = view.findViewById(R.id.colorProgress);
        million = view.findViewById(R.id.MillionaireProgress);
        completionist = view.findViewById(R.id.completionistProgress);


        DatabaseHelper dbhelper = new DatabaseHelper(getContext());

        String fastestEntry = dbhelper.searchFastest();
        System.out.println(fastestEntry);
        if (fastestEntry != null) {
            fastest.setText(fastestEntry);
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


//        int totalValue = dbhelper.getTotalValue(dbhelper.getPrices());
//        if (totalValue<=1000000){
//            million.setMax(1000000);
//            million.setProgress(Math.min(totalValue, 1000000));
//            millionaire.setText(Math.min(totalValue, 1000000) / 10000 + "%1");
//        }else {
//            million.setMax(5000000);
//            million.setProgress(Math.min(totalValue, 5000000));
//            millionaire.setText(Math.min(totalValue, 5000000) / 50000 + "%5");
//        }

        completionist.setMax(55);
        int brands = Integer.parseInt(dbhelper.getDistinctBrands().get(0));
        completionist.setProgress(Math.min(brands, 55));
        completion.setText(brands+"/"+55);

        return view;
    }

    @Override
    public void onResume() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }

        DatabaseHelper dbhelper = new DatabaseHelper(getContext());
        int colors = Integer.parseInt(dbhelper.getDistinctColors().get(0));
        colorful.setProgress(Math.min(colors, 13));
        color.setText(colors+"/"+13);

        int totalValue = dbhelper.getTotalValue(dbhelper.getPrices());
        double total = totalValue;
        if (totalValue <= 1000000) {
            million.setMax(1000000);
            million.setProgress(Math.min(totalValue, 1000000));

            double val = Math.min(total, 1000000) / 1000000;
            millionaire.setText(new BigDecimal(val).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "M/1M");

            millionaireTitle.setText("Millionaire motorist");

        } else {
            million.setMax(5000000);
            million.setProgress(Math.min(totalValue, 5000000));
            double val5 = Math.min(total, 5000000) / 1000000;
            millionaire.setText(new BigDecimal(val5).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "M/5M");
            millionaireTitle.setText("Millionaire motorist 2");
        }


        int brands = Integer.parseInt(dbhelper.getDistinctBrands().get(0));
        completionist.setProgress(Math.min(brands, 55));
        completion.setText(brands+"/"+55);
        super.onResume();
    }

}