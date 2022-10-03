package com.example.carcollectiondatabase;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {

    EditText brand_input, type_input, plate_input;
    Button add_entry;

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
        filterArray[1] = new InputFilter.LengthFilter(6);
        plate_input.setFilters(filterArray);
        add_entry = findViewById(R.id.add_entry);
        add_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
//                        Intent intent = new Intent(getBaseContext(), ListFragment.class);
//                        startActivity(intent);
                        Intent intent = new Intent(getBaseContext(), ListFragment.class);
                        activityResultLauncher.launch(intent);
                        brand_input.setText("");
                        type_input.setText("");
                    }
                }
                else{
                    Crawler c = new Crawler();
                    if (dbhelper.getPlates().contains(String.valueOf(plate_input.getText()))) {
                        Toast.makeText(getBaseContext(), "Car already added", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            ArrayList<String> car_data = c.getCarDataFast(plate);
                            if (car_data.isEmpty()) {
                                Toast.makeText(getBaseContext(), "Invalid plate", Toast.LENGTH_SHORT).show();
                            } else {
                                dbhelper.addCarPlate(plate, car_data.get(0));
                                Intent intent = new Intent(getBaseContext(), ListFragment.class);
                                startActivity(intent);
//                            activityResultLauncher.launch(intent);


                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

    }
}