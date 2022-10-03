package com.example.carcollectiondatabase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    Activity activity;
    private ArrayList car_id, brand, type, edition, plate, count, price, power, color, year,
            acceleration, topspeed, rank;


    public CustomAdapter(Activity activity, Context context,
                         ArrayList car_id,
                         ArrayList brand,
                         ArrayList type,
                         ArrayList edition,
                         ArrayList plate,
                         ArrayList count,
                         ArrayList price,
                         ArrayList power,
                         ArrayList color,
                         ArrayList year,
                         ArrayList acceleration,
                         ArrayList topspeed,
                         ArrayList rank){
        this.activity = activity;
        this.car_id = car_id;
        this.context = context;
        this.brand = brand;
        this.type = type;
        this.edition = edition;
        this.plate = plate;
        this.count = count;
        this.price = price;
        this.power = power;
        this.color = color;
        this.year = year;
        this.acceleration = acceleration;
        this.topspeed = topspeed;
        this.rank = rank;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.car_count_txt.setText(String.valueOf(count.get(position)));
        holder.brand_txt.setText(String.valueOf(brand.get(position)));
        holder.type_txt.setText(String.valueOf(type.get(position)));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(car_id.get(position)));
                intent.putExtra("brand", String.valueOf(brand.get(position)));
                intent.putExtra("type", String.valueOf(type.get(position)));
                intent.putExtra("edition", String.valueOf(edition.get(position)));
                intent.putExtra("plate", String.valueOf(plate.get(position)));
                intent.putExtra("price", String.valueOf(price.get(position)));
                intent.putExtra("power", String.valueOf(power.get(position)));
                intent.putExtra("color", String.valueOf(color.get(position)));
                intent.putExtra("year", String.valueOf(year.get(position)));
                intent.putExtra("acceleration", String.valueOf(acceleration.get(position)));
                intent.putExtra("topspeed", String.valueOf(topspeed.get(position)));
                intent.putExtra("rank", String.valueOf(rank.get(position)));
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return car_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView car_count_txt, brand_txt, type_txt;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            car_count_txt = itemView.findViewById(R.id.car_count_txt);
            brand_txt = itemView.findViewById(R.id.brand_text);
            type_txt = itemView.findViewById(R.id.type_text);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
