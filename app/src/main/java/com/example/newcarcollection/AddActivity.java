package com.example.newcarcollection;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.laurens.newcarcollection.R;

import java.io.IOException;
import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {

    EditText brand_input, type_input, plate_input;
    Button add_entry;
    View add;
    ProgressAddButton addButton;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == AddActivity.RESULT_OK){
                        Intent data = result.getData();
                        recreate();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        brand_input = findViewById(R.id.editBrand);
        type_input = findViewById(R.id.editType);
        plate_input = findViewById(R.id.editPlate);
        InputFilter[] filterArray = new InputFilter[2];
        filterArray[0] = new InputFilter.AllCaps();
        filterArray[1] = new InputFilter.LengthFilter(8);
        plate_input.setFilters(filterArray);

        add = findViewById(R.id.add_button2);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getCurrentFocus() != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(getBaseContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }

                addButton = new ProgressAddButton(getBaseContext(), view);
                DatabaseHelper dbhelper = new DatabaseHelper(AddActivity.this);
                String plate = plate_input.getText().toString();
                String brand = brand_input.getText().toString();
                String type = type_input.getText().toString();
                if(plate.equals("")) {
                    if(brand.equals("") || type.equals("")){
                        Toast.makeText(getBaseContext(), "Missing fields", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        dbhelper.addCarManually(brand_input.getText().toString().trim(), type_input.getText().toString().trim());
                        MainActivity.fromUpdate = true;
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        activityResultLauncher.launch(intent);
                        brand_input.setText("");
                        type_input.setText("");
                    }
                }
                else{
                    Crawler c = new Crawler();
                    if (dbhelper.getPlates().contains(String.valueOf(plate_input.getText()).replaceAll("-",""))) {
                        Toast.makeText(getBaseContext(), "Vehicle already added", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            ArrayList<String> car_data = c.getCarDataFast(plate);
                            if (car_data.isEmpty()) {
                                Toast.makeText(getBaseContext(), "Invalid plate", Toast.LENGTH_SHORT).show();
                            } else {
                                addButton.buttonActivated();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            dbhelper.addCarPlate(plate, car_data.get(0));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        MainActivity.fromUpdate = true;
                                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                        startActivity(intent);
                                    }
                                }, 1);

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

//        add_entry = findViewById(R.id.add_entry);
//        add_entry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DatabaseHelper dbhelper = new DatabaseHelper(AddActivity.this);
//                String plate = plate_input.getText().toString();
//                String brand = brand_input.getText().toString();
//                String type = type_input.getText().toString();
//                if(plate.equals("")) {
//                    if(brand.equals("") || type.equals("")){
//                        Toast.makeText(getBaseContext(), "Missing fields", Toast.LENGTH_SHORT).show();
//                    }
//                    else {
//                        dbhelper.addCarManually(brand_input.getText().toString().trim(), type_input.getText().toString().trim());
//                        MainActivity.fromUpdate = true;
//                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
//                        activityResultLauncher.launch(intent);
//                        brand_input.setText("");
//                        type_input.setText("");
//                    }
//                }
//                else{
//                    Crawler c = new Crawler();
//                    if (dbhelper.getPlates().contains(String.valueOf(plate_input.getText()).replaceAll("-",""))) {
//                        Toast.makeText(getBaseContext(), "Vehicle already added", Toast.LENGTH_SHORT).show();
//                    } else {
//                        try {
//                            ArrayList<String> car_data = c.getCarDataFast(plate);
//                            if (car_data.isEmpty()) {
//                                Toast.makeText(getBaseContext(), "Invalid plate", Toast.LENGTH_SHORT).show();
//                            } else {
//                                dbhelper.addCarPlate(plate, car_data.get(0));
//                                MainActivity.fromUpdate = true;
//                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
//                                startActivity(intent);
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        });

    }
}