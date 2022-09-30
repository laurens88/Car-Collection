package com.example.carcollectiondatabase;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.ListFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ShowActivity extends AppCompatActivity {

    EditText brand_input, type_input, plate_input, price_input, power_input, color_input,
            year_input, acceleration_input, topspeed_input;
    Button update_button, delete_button;

    String id, brand, type, plate, price, power, color, year, acceleration, topspeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        brand_input = findViewById(R.id.editBrand2);
        type_input = findViewById(R.id.editType2);
        plate_input = findViewById(R.id.editPlate2);
        price_input = findViewById(R.id.editPrice2);
        power_input = findViewById(R.id.editPower2);
        color_input = findViewById(R.id.editColor2);
        year_input = findViewById(R.id.editYear2);
        acceleration_input = findViewById(R.id.editAcceleration);
        topspeed_input = findViewById(R.id.editTopspeed);

        update_button = findViewById(R.id.update_entry);
        delete_button = findViewById(R.id.delete_entry);
        getAndSetData();

        ActionBar ab = getSupportActionBar();
        if (ab != null){
            ab.setTitle("Update " + brand);
        }


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

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper dbheper = new DatabaseHelper(ShowActivity.this);
                brand = brand_input.getText().toString().trim();
                type = type_input.getText().toString().trim();
                plate = plate_input.getText().toString().trim();
                price = price_input.getText().toString().trim();
                power = power_input.getText().toString().trim();
                color = color_input.getText().toString().trim();
                year = year_input.getText().toString().trim();
                acceleration = acceleration_input.getText().toString().trim();
                topspeed = topspeed_input.getText().toString().trim();


                dbheper.updataData(id, brand, type, plate, price, power, color, year, acceleration,
                        topspeed);

                Intent intent = new Intent(getBaseContext(), ListFragment.class);
                activityResultLauncher.launch(intent);
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });


    }

    void getAndSetData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("brand")
                && getIntent().hasExtra("type") && getIntent().hasExtra("price")
                && getIntent().hasExtra("power") && getIntent().hasExtra("color")
                && getIntent().hasExtra("year") && getIntent().hasExtra("plate")
                && getIntent().hasExtra("acceleration") && getIntent().hasExtra("topspeed")){
            id = getIntent().getStringExtra("id");
            brand = getIntent().getStringExtra("brand");
            type = getIntent().getStringExtra("type");
            price = getIntent().getStringExtra("price");
            power = getIntent().getStringExtra("power");
            color = getIntent().getStringExtra("color");
            year = getIntent().getStringExtra("year");
            plate = getIntent().getStringExtra("plate");
            acceleration = getIntent().getStringExtra("acceleration");
            topspeed = getIntent().getStringExtra("topspeed");

            brand_input.setText(brand);
            type_input.setText(type);
            if (plate.equals("null")){
                plate_input.setText("");
            }else{
                plate_input.setText(plate);}
            if (price.equals("null")){
                price_input.setText("");
            }else{
                price_input.setText(price);}
            if (power.equals("null")){
                power_input.setText("");
            }else{
                power_input.setText(power);}
            if (color.equals("null")){
                color_input.setText("");
            }else{
                color_input.setText(color);}
            if (year.equals("null")){
                year_input.setText("");
            }else{
                year_input.setText(year);}
            if (acceleration.equals("null")){
                acceleration_input.setText("");
            }else{
                acceleration_input.setText(acceleration);
            }
            if (topspeed.equals("null")){
                topspeed_input.setText("");
            }else{
                topspeed_input.setText(topspeed);
            }

            InputFilter[] filterArray = new InputFilter[2];
            filterArray[0] = new InputFilter.AllCaps();
            filterArray[1] = new InputFilter.LengthFilter(6);
            plate_input.setFilters(filterArray);
        }else{
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + brand + "?");
        builder.setMessage("Are you sure you want to delete " + brand + " " + type + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseHelper dbhelper = new DatabaseHelper(ShowActivity.this);
                dbhelper.deleteRow(id);
                Intent intent = new Intent(getBaseContext(), ListFragment.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();

    }
}