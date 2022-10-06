package com.example.carcollectiondatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carcollectiondatabase.fragments.lookupFragment;

public class DisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lookupFragment.resetProgressButton();
        setContentView(R.layout.activity_display);

        TextView data_brand = (TextView) findViewById(R.id.brandD);
        TextView data_type = (TextView) findViewById(R.id.typeD);
        TextView edition = (TextView) findViewById(R.id.editionD);
        TextView price = (TextView) findViewById(R.id.priceD);
        TextView horsepower = (TextView) findViewById(R.id.horsepowerD);
        TextView year = (TextView) findViewById(R.id.yearD);
        TextView color = (TextView) findViewById(R.id.colorD);
        TextView acceleration = (TextView) findViewById(R.id.accelerationD);
        TextView topspeed = (TextView) findViewById(R.id.topspeedD);
        TextView ranking = (TextView) findViewById(R.id.rankingD);
        EditText plate = (EditText) findViewById(R.id.editplateD);
        Button addButton = (Button) findViewById(R.id.savebutton);

        String vehicle = getIntent().getStringExtra("vehicletype");
        String brand = getIntent().getStringExtra("brand");
        String type = getIntent().getStringExtra("type");
        String editionString = getIntent().getStringExtra("edition");
        String priceString = getIntent().getStringExtra("price");
        String power = getIntent().getStringExtra("power");
        String colorString = getIntent().getStringExtra("color");
        String yearString = getIntent().getStringExtra("year");
        String plateString = getIntent().getStringExtra("plate");
        String accelerationString = getIntent().getStringExtra("acceleration");
        String topspeedString = getIntent().getStringExtra("topspeed");
        String rankString = getIntent().getStringExtra("rank");

        data_brand.setText(brand);
        if (vehicle.equals("car")) {
            data_type.setText(type);
            edition.setText(editionString);
        } else {
            data_type.setText(editionString);
        }

        price.setText("Price: " + priceString);
        year.setText("Year: " + yearString);
        color.setText("Color: " + colorString);
        acceleration.setText("0-100: " + accelerationString);
        topspeed.setText("Top speed: " + topspeedString);
        horsepower.setText("Power: " + power);
        ranking.setText("Rank: "+ rankString +" vehicles faster (NL)");
        plate.setText(formatPlate(plateString));

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper dbhelper = new DatabaseHelper(getBaseContext());
                if (dbhelper.getPlates().contains(plateString.replaceAll("-",""))){
                    if (vehicle.equals("car")) {
                        Toast.makeText(getBaseContext(), "Car already added", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getBaseContext(), "Motorcyle already added", Toast.LENGTH_SHORT).show();
                    }
                }else{
                dbhelper.addCarFromLookup(brand, type, editionString, priceString, yearString,
                        power, colorString, accelerationString, topspeedString, rankString, plateString, vehicle);

            }}
        });

    }

    String formatPlate(String plate){
        String formattedPlate = "";
        plate = plate.replaceAll("-","");
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
            if (formattedPlate.charAt(2)=='-') {
                formattedPlate = formattedPlate.substring(0, 5) + "-" + formattedPlate.substring(5);
            }
            else{
                formattedPlate = formattedPlate.substring(0, 2) + "-" + formattedPlate.substring(2);
            }
        }

        return formattedPlate;
    }
}