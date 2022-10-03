package com.example.carcollectiondatabase.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.StrictMode;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.commons.lang3.StringUtils;

import com.example.carcollectiondatabase.Crawler;
import com.example.carcollectiondatabase.DatabaseHelper;
import com.example.carcollectiondatabase.LoadingDialog;
import com.example.carcollectiondatabase.MainActivity;
import com.example.carcollectiondatabase.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class lookupFragment extends Fragment {
    private Button button;
    Crawler c;
    DatabaseHelper dbhelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lookup, container, false);

        button = view.findViewById(R.id.lookupbutton);

        c = new Crawler();
        dbhelper = new DatabaseHelper(getContext());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        TextView data_brand = (TextView) view.findViewById(R.id.brand);
        TextView data_type = (TextView) view.findViewById(R.id.type);
        TextView edition = (TextView) view.findViewById(R.id.edition);
        TextView price = (TextView) view.findViewById(R.id.price);
        TextView horsepower = (TextView) view.findViewById(R.id.horsepower);
        TextView year = (TextView) view.findViewById(R.id.year);
        TextView color = (TextView) view.findViewById(R.id.color);
        TextView acceleration = (TextView) view.findViewById(R.id.acceleration);
        TextView topspeed = (TextView) view.findViewById(R.id.topspeed);
        TextView ranking = (TextView) view.findViewById(R.id.ranking);
        EditText plate = (EditText) view.findViewById(R.id.editplate);
        InputFilter[] filterArray = new InputFilter[2];
        filterArray[0] = new InputFilter.AllCaps();
        filterArray[1] = new InputFilter.LengthFilter(6);
        plate.setFilters(filterArray);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity().getCurrentFocus() != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                }


                try {
                    ArrayList<String> car_data = c.getCarDataFast(String.valueOf(plate.getText()));

                    if (!car_data.isEmpty()) {


                        ArrayList<String> data = c.getCarData3(String.valueOf(plate.getText()));
                        if (!data.isEmpty()) {
                            data_brand.setText(data.get(0));
                            if (car_data.get(0).equals("car")) {
                                data_type.setText(data.get(1));
                                edition.setText(data.get(2));
                            } else {
                                data_type.setText(data.get(2));
                            }
                            //hide edition textview?
                            price.setText("Price: " + data.get(7));
                            year.setText("Year: " + data.get(3));
                            color.setText("Color: " + data.get(4));
                            acceleration.setText("0-100: " + data.get(5));
                            topspeed.setText("Top speed: " + data.get(6));
                            horsepower.setText("Power: " + data.get(8));
                            ranking.setText("Rank: "+ data.get(9) +" vehicles faster (NL)");

                        }
                    } else {
                        Toast.makeText(getContext(), "Invalid plate", Toast.LENGTH_SHORT).show();
                        data_brand.setText("Brand");
                        data_type.setText("Type");
                        edition.setText("");
                        price.setText("Price: ");
                        horsepower.setText("Power: ");
                        year.setText("Year: ");
                        color.setText("Color: ");
                        acceleration.setText("0-100: ");
                        topspeed.setText("Top speed: ");
                        ranking.setText("Ranking: ");
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

        return  view;
    }

}