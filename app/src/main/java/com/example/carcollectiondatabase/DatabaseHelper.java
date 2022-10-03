package com.example.carcollectiondatabase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "CarDB.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "carTable";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_BRAND = "car_brand";
    private static final String COLUMN_TYPE = "car_type";
    private static final String COLUMN_EDITION = "car_edition";
    private static final String COLUMN_PRICE = "car_price";
    private static final String COLUMN_HP = "car_hp";
    private static final String COLUMN_COLOR = "car_color";
    private static final String COLUMN_YEAR = "car_year";
    private static final String COLUMN_PLATE = "car_plate";
    private static final String COLUMN_COUNT = "car_count";
    private static final String COLUMN_ACCELERATION = "car_acceleration";
    private static final String COLUMN_TOPSPEED = "car_topspeed";
    private static final String COLUMN_RANK = "car_rank";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "CREATE TABLE " + TABLE_NAME  +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_BRAND + " TEXT, " +
                        COLUMN_TYPE + " TEXT, " +
                        COLUMN_EDITION + " TEXT, " +
                        COLUMN_PRICE + " TEXT, " +
                        COLUMN_HP + " TEXT, " +
                        COLUMN_COLOR + " TEXT, " +
                        COLUMN_YEAR + " TEXT, " +
                        COLUMN_PLATE + " TEXT, " +
                        COLUMN_COUNT + " TEXT, " +
                        COLUMN_ACCELERATION + " TEXT, " +
                        COLUMN_TOPSPEED + " TEXT, " +
                        COLUMN_RANK + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void removeAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    void addCarManually(String brand, String type){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_BRAND, brand);
        cv.put(COLUMN_TYPE, type);

        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed to add car", Toast.LENGTH_SHORT).show();
        }else {Toast.makeText(context, "Car added", Toast.LENGTH_SHORT).show();}
    }

    void addCarPlate(String plate, String vehicleType) throws IOException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        Crawler c = new Crawler();

        ArrayList<String> car_data = c.getCarData3(plate);

        cv.put(COLUMN_BRAND, car_data.get(0));
        if (vehicleType.equals("car")) {
            cv.put(COLUMN_TYPE, car_data.get(1).toUpperCase().replaceAll(car_data.get(0) + " ".toUpperCase(), "")
                    .replaceAll("MCLAREN ", ""));
            cv.put(COLUMN_EDITION, car_data.get(2));
        }
        else{
            cv.put(COLUMN_TYPE, car_data.get(2));
        }
        cv.put(COLUMN_YEAR, car_data.get(3));
        cv.put(COLUMN_PRICE, car_data.get(7));
        cv.put(COLUMN_COLOR, car_data.get(4));
        cv.put(COLUMN_HP, car_data.get(8));
        cv.put(COLUMN_PLATE, plate);
        cv.put(COLUMN_COUNT, "placeholder");
        cv.put(COLUMN_ACCELERATION, car_data.get(5));
        cv.put(COLUMN_TOPSPEED, car_data.get(6));
        cv.put(COLUMN_RANK, car_data.get(9));

        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed to add car", Toast.LENGTH_SHORT).show();
        }else {Toast.makeText(context, "Car added", Toast.LENGTH_SHORT).show();}
    }

    public Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void updataData(String row_id, String brand, String type, String edition, String plate, String price,
                    String power, String color, String year, String acceleration, String topspeed, String rank){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        if(plate.equals("") || plate.equals("null")) {
            cv.put(COLUMN_BRAND, brand);
            cv.put(COLUMN_TYPE, type);
            cv.put(COLUMN_EDITION, edition);
            cv.put(COLUMN_PRICE, price);
            cv.put(COLUMN_HP, power);
            cv.put(COLUMN_COLOR, color);
            cv.put(COLUMN_YEAR, year);
            cv.put(COLUMN_COUNT, "placeholder");
            cv.put(COLUMN_PLATE, "");
            cv.put(COLUMN_ACCELERATION, acceleration);
            cv.put(COLUMN_TOPSPEED, topspeed);
            cv.put(COLUMN_RANK, rank);
        }else{
            Crawler c = new Crawler();
            try {
                ArrayList<String> data = c.getCarDataFast(plate);
                if(data.isEmpty()){
                    Toast.makeText(context, "Invalid plate", Toast.LENGTH_SHORT).show();
                }
                else{

                    ArrayList<String> car_data = c.getCarData3(plate);

                    cv.put(COLUMN_BRAND, car_data.get(0));
                    if (data.get(0).equals("car")){
                    cv.put(COLUMN_TYPE, car_data.get(1).toUpperCase().replaceAll(car_data.get(0)+" ".toUpperCase(), "")
                            .replaceAll("MCLAREN ", ""));
                    cv.put(COLUMN_EDITION, car_data.get(2));
                    }
                    else{
                        cv.put(COLUMN_TYPE, car_data.get(2));
                    }
                    cv.put(COLUMN_YEAR, car_data.get(3));
                    cv.put(COLUMN_PRICE, car_data.get(7));
                    cv.put(COLUMN_COLOR, car_data.get(4));
                    cv.put(COLUMN_HP, car_data.get(8));
                    cv.put(COLUMN_PLATE, plate);
                    cv.put(COLUMN_COUNT, "placeholder");
                    cv.put(COLUMN_ACCELERATION, car_data.get(5));
                    cv.put(COLUMN_TOPSPEED, car_data.get(6));
                    cv.put(COLUMN_RANK, car_data.get(9));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Car updated", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Car deleted", Toast.LENGTH_SHORT).show();
        }

    }

    public Cursor search(String term){
        String query = "SELECT * FROM " + TABLE_NAME+" WHERE "+COLUMN_BRAND+" = ? or "+COLUMN_TYPE+" = ?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, new String[]{term});
        }
        return cursor;
    }

    @SuppressLint("Range")
    public ArrayList<String> getPlates(){
        ArrayList<String> plates = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME+" WHERE "+COLUMN_COUNT+" = ?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = null;
        if (db != null){
            c = db.rawQuery(query, new String[]{"placeholder"});
        }
        while(c.moveToNext()){
            plates.add(c.getString(c.getColumnIndex("car_plate")));
        }
        return plates;
    }

    public ArrayList<String> getFastestCar(){
        ArrayList<String> car = new ArrayList<>();



        return car;
    }

    @SuppressLint("Range")
    public String cursorToString(Cursor cursor){
        String cursorString = "";
        if (cursor.moveToFirst() ){
            String[] columnNames = cursor.getColumnNames();
            for (String name: columnNames)
                cursorString += String.format("%s ][ ", name);
            cursorString += "\n";
            do {
                for (String name: columnNames) {
                    cursorString += String.format("%s ][ ",
                            cursor.getString(cursor.getColumnIndex(name)));
                }
                cursorString += "\n";
            } while (cursor.moveToNext());
        }
        return cursorString;
    }

    public void addList() {
        String plates = "8ZPT48XX456G74HSX1XD306LKH726V73JNZ1RH441FXK271PPS734PRJ452JRL963N02HJL447ZPP2V896FHV080BRRD735SN791BJSR561JJ258HNXG271S63PFZ5P389SV17ZRF7G417ZSXB662D60PBV1PJ389PXT566K28PGVKR822GNN061ZXP098VZJ739LNV636ZBJ748HGZJ753RN628BGL916NLP519RHK124GBN395VT9ZNK49ZF729FG675GKK243XSG233HRTD550ZL776SZ41SFRFTH471TPT276LV272SBN036GHXT345TTX817NV960GRH487RGRK309JH589SVPT390PKG030LG343VSP973JNVJK61NK178JKPT578BXZ085NH629BVXX041ZXS504FRX788RN741KKPX411JSB038HG174ZLTT345VNJ341JG635HNJ432FDL477FGHH061LG164TNH721BKVLS61GHH936ZV301DKTH190FPG734F";
        for (int i = 0; i < plates.length(); i = i + 6) {
            String plate = plates.substring(i, i + 6);
            if (!getPlates().contains(plate)) {
                if (plate.equals("G417ZS")) {
                    addCarManually("New Holland", "T7230");
                    addCarManually("New Holland", "T650");
                }
                try {
                    addCarPlate(plate, "car");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}
