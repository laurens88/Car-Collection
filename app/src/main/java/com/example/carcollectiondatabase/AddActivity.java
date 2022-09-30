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
        String plates = "8ZPT48XX456G74HSX1XD306LKH726V73JNZ1RH441FXK271PPS734PRJ452JRL963N02HJL447ZPP2V896FHV080BRRD735SN791BJSR561JJ258HNXG271S63PFZ5P389SV17ZRF7G417ZSXB662D60PBV1PJ389PXT566K28PGVKR822GNN061ZXP098VZJ739LNV636ZBJ748HGZJ753RN628BGL916NLP519RHK124GBN395VT9ZNK49ZF729FG675GKK243XSG233HRTD550ZL776SZ41SFRFTH471TPT276LV272SBN036GHXT345TTX817NV960GRH487RGRK309JH589SVPT390PKG030LG343VSP973JNVJK61NK178JKPT578BXZ085NH629BVXX041ZXS504FRX788RN741KKPX411JSB038HG174ZLTT345VNJ341JG635HNJ432FDL477FGHH061LG164TNH721BKVLS61GHH936ZV301DKTH190FPG734F";
        System.out.println(plates.length());
        DatabaseHelper dbhelper = new DatabaseHelper(AddActivity.this);
        for (int i = 0; i<plates.length(); i=i+6 ){
            String plate = plates.substring(i, i+6);
            if (plate.equals("G417ZS")){
                dbhelper.addCarManually("New Holland", "T7230");
                dbhelper.addCarManually("New Holland", "T650");
            }
            try {
                dbhelper.addCarPlate(plate, "car");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}