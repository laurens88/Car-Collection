package com.example.newcarcollection.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.newcarcollection.AddActivity;
import com.example.newcarcollection.CustomAdapter;
import com.example.newcarcollection.DatabaseHelper;
import com.laurens.newcarcollection.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class listFragment extends Fragment {

    private final ActivityResultLauncher<Intent> exportLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        exportDatabaseToUri(uri);
                    }
                }
            });

    private final ActivityResultLauncher<Intent> importLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        importDatabaseFromUri(uri);
                    }
                }
            });


    FloatingActionButton add_button;
    RecyclerView recyclerView;

    DatabaseHelper dbhelper;
    ArrayList<String> car_id, brand, type, edition, plate, count, price, power,
            color, year, acceleration, topspeed, rank, note;
    ArrayList<String> car_id2, brand2, type2, edition2, plate2, count2, price2, power2,
            color2, year2, acceleration2, topspeed2, rank2, note2;

    CustomAdapter customAdapter;

    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        add_button = view.findViewById(R.id.add_button);
        dbhelper = new DatabaseHelper(getContext());
        dbhelper.addColumn("car_note");
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                startActivity(intent);
            }
        });

        car_id = new ArrayList<>();
        brand = new ArrayList<>();
        type = new ArrayList<>();
        edition = new ArrayList<>();
        plate = new ArrayList<>();
        count = new ArrayList<>();
        price = new ArrayList<>();
        power = new ArrayList<>();
        color = new ArrayList<>();
        year = new ArrayList<>();
        acceleration = new ArrayList<>();
        topspeed = new ArrayList<>();
        rank = new ArrayList<>();
        note = new ArrayList<>();

        car_id2 = new ArrayList<>();
        brand2 = new ArrayList<>();
        type2 = new ArrayList<>();
        edition2 = new ArrayList<>();
        plate2 = new ArrayList<>();
        count2 = new ArrayList<>();
        price2 = new ArrayList<>();
        power2 = new ArrayList<>();
        color2 = new ArrayList<>();
        year2 = new ArrayList<>();
        acceleration2 = new ArrayList<>();
        topspeed2 = new ArrayList<>();
        rank2 = new ArrayList<>();
        note2 = new ArrayList<>();

        storeDataInArrays();

        customAdapter = new CustomAdapter(getActivity(), getContext(), car_id, brand, type, edition, plate,
                count, price, power, color, year, acceleration, topspeed, rank, note);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onResume() {
        storeSearchData("", false);
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        inflater.inflate(R.menu.list_fragment_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.item_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                storeSearchData(s, true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                storeSearchData(s, false);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_import) {
            handleImport();
            return true;
        } else if (id == R.id.action_export) {
            handleExport();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void handleExport() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/octet-stream");
        intent.putExtra(Intent.EXTRA_TITLE, "CarDB_Export.db");
        exportLauncher.launch(intent);
    }

    private void handleImport() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        importLauncher.launch(intent);
    }

    private void exportDatabaseToUri(Uri uri) {
        try {
            File dbFile = getContext().getDatabasePath("CarDB.db");
            InputStream inputStream = new FileInputStream(dbFile);
            OutputStream outputStream = getContext().getContentResolver().openOutputStream(uri);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();
            Toast.makeText(getContext(), "Database exported", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Export failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void importDatabaseFromUri(Uri uri) {
        try {
            File dbFile = getContext().getDatabasePath("CarDB.db");

            InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
            OutputStream outputStream = new FileOutputStream(dbFile, false);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();
            Toast.makeText(getContext(), "Database imported", Toast.LENGTH_SHORT).show();

            // Force reload of list
            storeDataInArrays();
            customAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Import failed", Toast.LENGTH_SHORT).show();
        }
    }



    void storeDataInArrays(){
        Cursor cursor = dbhelper.readAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(getContext(), "No cars in collection", Toast.LENGTH_SHORT).show();
        }else{
            int car_count = 1;
                while (cursor.moveToNext()) {
                    car_id.add(cursor.getString(0));
                    brand.add(cursor.getString(1));
                    type.add(cursor.getString(2));
                    edition.add(cursor.getString(3));
                    price.add(cursor.getString(4));
                    power.add(cursor.getString(5));
                    color.add(cursor.getString(6));
                    year.add(cursor.getString(7));
                    plate.add(cursor.getString(8));
                    count.add(String.valueOf(car_count));
                    acceleration.add(cursor.getString(10));
                    topspeed.add(cursor.getString(11));
                    rank.add(cursor.getString(12));
                    note.add(cursor.getString(13));
                    car_count += 1;
                }
        }
    }

    void storeSearchData(String searchTerm, Boolean submit){
        if (searchTerm.equals("")) {
            customAdapter = new CustomAdapter(getActivity(), getContext(), car_id, brand, type, edition, plate,
                    count, price, power, color, year, acceleration, topspeed, rank, note);
            recyclerView.setAdapter(customAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }else{
            Cursor cursor = dbhelper.search(searchTerm);
            Cursor cursorType = dbhelper.searchType(searchTerm);
            Cursor cursors[] = new Cursor[]{cursor, cursorType};
            MergeCursor mergeCursor = new MergeCursor(cursors);
        if(mergeCursor.getCount() == 0){
            car_id2.clear();
            brand2.clear();
            type2.clear();
            edition2.clear();
            price2.clear();
            power2.clear();
            color2.clear();
            year2.clear();
            plate2.clear();
            count2.clear();
            acceleration2.clear();
            topspeed2.clear();
            rank2.clear();
            note2.clear();
            if (submit){
            Toast.makeText(getContext(), searchTerm+" not found", Toast.LENGTH_SHORT).show();
            }
        }else {
                int car_count = 1;
                car_id2.clear();
                brand2.clear();
                type2.clear();
                edition2.clear();
                price2.clear();
                power2.clear();
                color2.clear();
                year2.clear();
                plate2.clear();
                count2.clear();
                acceleration2.clear();
                topspeed2.clear();
                rank2.clear();
                note2.clear();
                while (mergeCursor.moveToNext()) {
                    car_id2.add(mergeCursor.getString(0));
                    brand2.add(mergeCursor.getString(1));
                    type2.add(mergeCursor.getString(2));
                    edition2.add(mergeCursor.getString(3));
                    price2.add(mergeCursor.getString(4));
                    power2.add(mergeCursor.getString(5));
                    color2.add(mergeCursor.getString(6));
                    year2.add(mergeCursor.getString(7));
                    plate2.add(mergeCursor.getString(8));
                    count2.add(String.valueOf(car_count));
                    acceleration2.add(mergeCursor.getString(10));
                    topspeed2.add(mergeCursor.getString(11));
                    rank2.add(mergeCursor.getString(12));
                    note2.add(mergeCursor.getString(13));
                    car_count += 1;
                }
            }
            customAdapter = new CustomAdapter(getActivity(), getContext(), car_id2, brand2, type2,
                    edition2, plate2,
                    count2, price2, power2, color2, year2, acceleration2, topspeed2, rank2, note2);
            recyclerView.setAdapter(customAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }}
    }