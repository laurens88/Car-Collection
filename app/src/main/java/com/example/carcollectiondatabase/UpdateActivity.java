package com.example.carcollectiondatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;

public class UpdateActivity extends AppCompatActivity {

    EditText brand_input, type_input, edition_input, plate_input, price_input, power_input, color_input,
            year_input, acceleration_input, topspeed_input, rank_input;
    Button update_button, cancel_button;

    String id, brand, type, edition, plate, price, power, color, year, acceleration, topspeed, rank;

    ActionBar ab;

    ArrayList<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        brand_input = findViewById(R.id.editBrand2);
        brand_input.setFocusable(false);
        type_input = findViewById(R.id.editType2);
        type_input.setFocusable(false);
        edition_input = findViewById(R.id.editEdition);
        edition_input.setFocusable(false);
        plate_input = findViewById(R.id.editPlate2);
        plate_input.setFocusable(false);
        price_input = findViewById(R.id.editPrice2);
        price_input.setFocusable(false);
        power_input = findViewById(R.id.editPower2);
        power_input.setFocusable(false);
        color_input = findViewById(R.id.editColor2);
        color_input.setFocusable(false);
        year_input = findViewById(R.id.editYear2);
        year_input.setFocusable(false);
        acceleration_input = findViewById(R.id.editAcceleration);
        acceleration_input.setFocusable(false);
        topspeed_input = findViewById(R.id.editTopspeed);
        topspeed_input.setFocusable(false);
        rank_input = findViewById(R.id.editRanking);
        rank_input.setFocusable(false);

        update_button = findViewById(R.id.update_entry);
        cancel_button = findViewById(R.id.cancel_button);

        update_button.setVisibility(View.GONE);
        cancel_button.setVisibility(View.GONE);

        getAndSetData();

        ab = getSupportActionBar();
        if (ab != null){
            ab.setTitle("");
        }

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper dbheper = new DatabaseHelper(UpdateActivity.this);
                brand = brand_input.getText().toString().trim();
                type = type_input.getText().toString().trim();
                edition = edition_input.getText().toString().trim();
                plate = plate_input.getText().toString().trim().replaceAll("-","");
                price = price_input.getText().toString().trim();
                power = power_input.getText().toString().trim();
                color = color_input.getText().toString().trim();
                year = year_input.getText().toString().trim();
                acceleration = acceleration_input.getText().toString().trim();
                topspeed = topspeed_input.getText().toString().trim();
                rank = rank_input.getText().toString().trim();

                Crawler c = new Crawler();
                try {
                    data = c.getCarDataFast(plate);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(data.isEmpty()){
                    Toast.makeText(getBaseContext(), "Invalid plate", Toast.LENGTH_SHORT).show();
                }else{
                if (dbheper.getPlates().contains(plate.replaceAll("-",""))){
                    Toast.makeText(getBaseContext(),"Vehicle already added", Toast.LENGTH_SHORT).show();
                }else{

                dbheper.updataData(id, brand, type, edition, plate, price, power, color, year, acceleration,
                topspeed, rank);

                Toast.makeText(getBaseContext(),"Updated successfully", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getBaseContext(), ListFragment.class);
                startActivity(intent);
            }}}
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UpdateActivity.this.getCurrentFocus() != null) {
                    InputMethodManager imm = (InputMethodManager) UpdateActivity.this.getSystemService(UpdateActivity.this.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(UpdateActivity.this.getCurrentFocus().getWindowToken(), 0);
                }

                update_button.setVisibility(View.GONE);
                cancel_button.setVisibility(View.GONE);
                brand_input.setFocusableInTouchMode(false);
                brand_input.clearFocus();
                brand_input.setText(brand);
                type_input.setFocusableInTouchMode(false);
                type_input.clearFocus();
                type_input.setText(type);
                edition_input.setFocusableInTouchMode(false);
                edition_input.clearFocus();
                if (edition.equals("null")){
                    edition_input.setText("");
                }else {
                    edition_input.setText(edition);
                }
                price_input.setFocusableInTouchMode(false);
                price_input.clearFocus();
                price_input.setText(price);
                power_input.setFocusableInTouchMode(false);
                power_input.clearFocus();
                power_input.setText(power);
                color_input.setFocusableInTouchMode(false);
                color_input.clearFocus();
                if (color.equals("Unknown")){
                    color_input.setText("");
                }else{
                color_input.setText(color);
                }
                year_input.setFocusableInTouchMode(false);
                year_input.clearFocus();
                year_input.setText(year);
                acceleration_input.setFocusableInTouchMode(false);
                acceleration_input.clearFocus();
                if (acceleration.equals("Unknown")){
                    acceleration_input.setText("");
                }else{
                    acceleration_input.setText(acceleration);
                }
                topspeed_input.setFocusableInTouchMode(false);
                topspeed_input.clearFocus();
                if (topspeed.equals("Unknown")){
                    topspeed_input.setText("");
                }else{
                    topspeed_input.setText(topspeed);
                }
                rank_input.setFocusableInTouchMode(false);
                rank_input.clearFocus();
                rank_input.setText(rank);
                plate_input.setFocusableInTouchMode(false);
                plate_input.clearFocus();
                plate_input.setText(plate);
                ab.setTitle("");
            }
        });

    }

    void getAndSetData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("brand")
                && getIntent().hasExtra("type") && getIntent().hasExtra("edition") && getIntent().hasExtra("price")
                && getIntent().hasExtra("power") && getIntent().hasExtra("color")
                && getIntent().hasExtra("year") && getIntent().hasExtra("plate")
                && getIntent().hasExtra("acceleration") && getIntent().hasExtra("topspeed")
                && getIntent().hasExtra("rank")){
            id = getIntent().getStringExtra("id");
            brand = getIntent().getStringExtra("brand");
            type = getIntent().getStringExtra("type");
            edition = getIntent().getStringExtra("edition");
            price = getIntent().getStringExtra("price");
            power = getIntent().getStringExtra("power");
            color = getIntent().getStringExtra("color");
            year = getIntent().getStringExtra("year");
            plate = getIntent().getStringExtra("plate").replaceAll("-","");
            acceleration = getIntent().getStringExtra("acceleration");
            topspeed = getIntent().getStringExtra("topspeed");
            rank = getIntent().getStringExtra("rank");

            brand_input.setText(brand);
            type_input.setText(type);
            if (edition.equals("null")){
                edition_input.setText("");
            }else{
                edition_input.setText(edition);
            }

            if (plate.equals("null")){
                plate_input.setText("");
            }else{
            plate_input.setText(formatPlate(plate));}
            if (price.equals("null")){
                price_input.setText("");
            }else{
                price_input.setText(price);}
            if (power.equals("null")){
                power_input.setText("");
            }else{
                power_input.setText(power);}
            if (color.equals("null") || color.equals("Unknown")){
                color_input.setText("");
            }else{
                color_input.setText(color);}
            if (year.equals("null")){
                year_input.setText("");
            }else{
                year_input.setText(year);}
            if (acceleration.equals("null") || acceleration.equals("Unknown")){
                acceleration_input.setText("");
            }else{
                acceleration_input.setText(acceleration);
            }
            if (topspeed.equals("null") || topspeed.equals("Unknown")){
                topspeed_input.setText("");
            }else{
                topspeed_input.setText(topspeed);
            }
            rank_input.setText(formatRank(rank));

            InputFilter[] filterArray = new InputFilter[2];
            filterArray[0] = new InputFilter.AllCaps();
            filterArray[1] = new InputFilter.LengthFilter(8);
            plate_input.setFilters(filterArray);
        }else{
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }

    String formatPlate(String plate){
        String formattedPlate = "";
        plate = plate.replaceAll("-","");
        System.out.println(plate);
        int dash_count = 0;
        for (int i = 0; i<plate.length()-1 && dash_count <= 2;i++){

            char c1 = plate.charAt(i);
            char c2 = plate.charAt(i+1);

            formattedPlate += c1;

            if (Character.isDigit(c1) ^ Character.isDigit(c2)){
                formattedPlate += "-";
                dash_count += 1;
            }

            if (i==plate.length()-2){
                formattedPlate += c2;
            }

        }

        if (dash_count == 1){
            formattedPlate = formattedPlate.substring(0, 5) + "-" + formattedPlate.substring(5);
        }

        return formattedPlate;
    }

    String formatRank(String rank){
        NumberFormat format = NumberFormat.getInstance();
        format.setGroupingUsed(true);

        return String.valueOf(format.format(Integer.parseInt(rank))).replaceAll(",",".");
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + brand + "?");
        builder.setMessage("Are you sure you want to delete " + brand + " " + type + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseHelper dbhelper = new DatabaseHelper(UpdateActivity.this);
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

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_entry){
            confirmDialog();
        }
        if (item.getItemId() == R.id.edit_entry){
            if (update_button.getVisibility() == View.GONE) {
                update_button.setVisibility(View.VISIBLE);
                cancel_button.setVisibility(View.VISIBLE);
                brand_input.setFocusableInTouchMode(true);
                type_input.setFocusableInTouchMode(true);
                edition_input.setFocusableInTouchMode(true);
                price_input.setFocusableInTouchMode(true);
                power_input.setFocusableInTouchMode(true);
                color_input.setFocusableInTouchMode(true);
                year_input.setFocusableInTouchMode(true);
                acceleration_input.setFocusableInTouchMode(true);
                topspeed_input.setFocusableInTouchMode(true);
                rank_input.setFocusableInTouchMode(true);
                plate_input.setFocusableInTouchMode(true);
                ab.setTitle("Update " + brand);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}