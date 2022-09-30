package com.example.carcollectiondatabase.fragments;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.carcollectiondatabase.AddActivity;
import com.example.carcollectiondatabase.CustomAdapter;
import com.example.carcollectiondatabase.DatabaseHelper;
import com.example.carcollectiondatabase.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class listFragment extends Fragment {

    FloatingActionButton add_button;
    RecyclerView recyclerView;

    DatabaseHelper dbhelper;
    ArrayList<String> car_id, brand, type, plate, count, price, power, color, year, acceleration, topspeed;

    CustomAdapter customAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        add_button = view.findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                startActivity(intent);
            }
        });

        dbhelper = new DatabaseHelper(getContext());
        car_id = new ArrayList<>();
        brand = new ArrayList<>();
        type = new ArrayList<>();
        plate = new ArrayList<>();
        count = new ArrayList<>();
        price = new ArrayList<>();
        power = new ArrayList<>();
        color = new ArrayList<>();
        year = new ArrayList<>();
        acceleration = new ArrayList<>();
        topspeed = new ArrayList<>();

        storeDataInArrays();

        customAdapter = new CustomAdapter(getActivity(), getContext(), car_id, brand, type, plate,
                count, price, power, color, year, acceleration, topspeed);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    void storeDataInArrays(){
        Cursor cursor = dbhelper.readAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(getContext(), "No cars in collection", Toast.LENGTH_SHORT).show();
        }else{
            int car_count = 1;
            while (cursor.moveToNext()){
                car_id.add(cursor.getString(0));
                brand.add(cursor.getString(1));
                type.add(cursor.getString(2));
                price.add(cursor.getString(3));
                power.add(cursor.getString(4));
                color.add(cursor.getString(5));
                year.add(cursor.getString(6));
                plate.add(cursor.getString(7));
                count.add(String.valueOf(car_count));
                acceleration.add(cursor.getString(9));
                topspeed.add(cursor.getString(10));
                car_count += 1;
            }
        }
    }


}