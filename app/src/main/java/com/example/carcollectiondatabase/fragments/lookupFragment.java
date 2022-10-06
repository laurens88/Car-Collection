package com.example.carcollectiondatabase.fragments;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.carcollectiondatabase.DisplayActivity;
import com.example.carcollectiondatabase.LoadingDialog;
import com.example.carcollectiondatabase.MainActivity;
import com.example.carcollectiondatabase.ProgressButton;
import com.example.carcollectiondatabase.R;
import com.example.carcollectiondatabase.UpdateActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class lookupFragment extends Fragment {
    private static ProgressButton progressButton;
    Crawler c;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lookup, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        c = new Crawler();

        EditText plate = (EditText) view.findViewById(R.id.editplate);
        InputFilter[] filterArray = new InputFilter[2];
        filterArray[0] = new InputFilter.AllCaps();
        filterArray[1] = new InputFilter.LengthFilter(8);
        plate.setFilters(filterArray);

        view.findViewById(R.id.include);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressButton = new ProgressButton(getContext(), view);

                if (getActivity().getCurrentFocus() != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                }

                try {
                    ArrayList<String> car_data = c.getCarDataFast(String.valueOf(plate.getText()));

                    if (!car_data.isEmpty()) {
                        progressButton.buttonActivated();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ArrayList<String> data = c.getCarData3(String.valueOf(plate.getText()).replaceAll("-", ""));
                                Intent intent = new Intent(getContext(), DisplayActivity.class);
                                intent.putExtra("vehicletype", car_data.get(0));
                                intent.putExtra("brand", data.get(0));
                                intent.putExtra("type", data.get(1));
                                intent.putExtra("edition", data.get(2));
                                intent.putExtra("plate", String.valueOf(plate.getText()));
                                intent.putExtra("price", data.get(7));
                                intent.putExtra("power", data.get(8));
                                intent.putExtra("color", data.get(4));
                                intent.putExtra("year", data.get(3));
                                intent.putExtra("acceleration", data.get(5));
                                intent.putExtra("topspeed", data.get(6));
                                intent.putExtra("rank", data.get(9));
                                plate.setText("");
                                plate.clearFocus();
                                getActivity().startActivityForResult(intent, 1);
                            }
                        }, 1);

                    } else {
                        Toast.makeText(getContext(), "Invalid plate", Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return  view;
    }

    public static void resetProgressButton(){
        progressButton.buttonReset();
    }
}