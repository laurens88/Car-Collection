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
import java.lang.reflect.Array;
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
    private static final String COLUMN_NOTE = "car_note";

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
                        COLUMN_RANK + " TEXT, " +
                        COLUMN_NOTE + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addColumn(String column_name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_NAME, null);
        String[] columnNames = cursor.getColumnNames();
        Boolean columnPresent = false;
        for (int i = 0; i<columnNames.length; i++){
            if (column_name.equals(columnNames[i])){
                columnPresent = true;
            }
        }
        SQLiteDatabase dbw = this.getWritableDatabase();
        if (!columnPresent){
            db.execSQL("alter table carTable add column "+column_name+" text");

        }
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
        cv.put(COLUMN_NOTE, "");

        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed to add car", Toast.LENGTH_SHORT).show();
        }else {Toast.makeText(context, "Car added", Toast.LENGTH_SHORT).show();}
    }

    void addCarFromLookup(String brand, String type, String edition, String price, String year,
                          String power, String color, String acc, String topspeed, String rank, String plate, String vehicle){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_BRAND, brand);
        if (vehicle.equals("car")) {
            cv.put(COLUMN_TYPE, type);
            cv.put(COLUMN_EDITION, edition);
        }
        else{
            cv.put(COLUMN_TYPE, edition);
        }
        cv.put(COLUMN_YEAR, year);
        cv.put(COLUMN_PRICE, price);
        cv.put(COLUMN_COLOR, color);
        cv.put(COLUMN_HP, power);
        cv.put(COLUMN_PLATE, plate.replaceAll("-", ""));
        cv.put(COLUMN_COUNT, "placeholder");
        cv.put(COLUMN_ACCELERATION, acc);
        cv.put(COLUMN_TOPSPEED, topspeed);
        cv.put(COLUMN_RANK, rank);
        cv.put(COLUMN_NOTE, "");

        long result = db.insert(TABLE_NAME, null, cv);
        if (vehicle.equals("car")) {
            if (result == -1) {
                Toast.makeText(context, "Failed to add car", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Car added", Toast.LENGTH_SHORT).show();
            }
        }else{
            if (result == -1) {
                Toast.makeText(context, "Failed to add motorcycle", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Motorcycle added", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void addCarPlate(String plate, String vehicleType) throws IOException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        Crawler c = new Crawler();

        plate = plate.replaceAll("-", "");

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
        cv.put(COLUMN_NOTE, "");

        long result = db.insert(TABLE_NAME, null, cv);
        if (vehicleType.equals("car")) {
            if (result == -1) {
                Toast.makeText(context, "Failed to add car", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Car added", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            if (result == -1) {
                Toast.makeText(context, "Failed to add motorcycle", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Motorcycle added", Toast.LENGTH_SHORT).show();
            }
        }
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

    void updataData(String row_id, String brand, String type, String edition, String plate,
                    String price, String power, String color, String year,
                    String acceleration, String topspeed, String rank, String note){
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
            cv.put(COLUMN_NOTE, note);
        }else{
            Crawler c = new Crawler();
            try {
                ArrayList<String> data = c.getCarDataFast(plate);
                if(data.isEmpty()){
                    Toast.makeText(context, "Invalid plate", Toast.LENGTH_SHORT).show();
                }
                else{

                    ArrayList<String> car_data = c.getCarData3(plate);

                    String crawBrand = car_data.get(0);
                    String crawlType = car_data.get(1);
                    String crawlEdition = car_data.get(2);
                    String crawlYear = car_data.get(3);
                    String crawlColor = car_data.get(4);
                    String crawlAcc = car_data.get(5);
                    String crawlTopspeed = car_data.get(6);
                    String crawlPrice = car_data.get(7);
                    String crawlPower = car_data.get(8);
                    String crawlRank = car_data.get(9);

                    cv.put(COLUMN_BRAND, crawBrand);
                    if (data.get(0).equals("car")){
                        if (crawlType.equals("Unknown")){
                            cv.put(COLUMN_TYPE, type);
                        }else {
                            cv.put(COLUMN_TYPE, crawlType.toUpperCase().replaceAll(crawBrand + " ".toUpperCase(), "")
                                    .replaceAll("MCLAREN ", ""));
                        }
                        if (crawlEdition.equals("Unknown")){
                            cv.put(COLUMN_EDITION, edition);
                        }else{
                            cv.put(COLUMN_EDITION, crawlEdition);
                        }
                    }
                    else{
                        if (crawlEdition.equals("Unknown")){
                            cv.put(COLUMN_TYPE, type);
                        }else{
                        cv.put(COLUMN_TYPE, crawlEdition);
                        }
                    }

                    if (crawlYear.equals("Unknown")){
                        cv.put(COLUMN_YEAR, year);
                    }else {
                        cv.put(COLUMN_YEAR, crawlYear);
                    }

                    if (crawlPrice.equals("Unknown")){
                        cv.put(COLUMN_PRICE, price);
                    }else {
                        cv.put(COLUMN_PRICE, crawlPrice);
                    }

                    if (crawlColor.equals("Unknown")){
                        cv.put(COLUMN_COLOR, color);
                    }else {
                        cv.put(COLUMN_COLOR, crawlColor);
                    }

                    if (crawlPower.equals("Unknown")){
                        cv.put(COLUMN_HP, power);
                    }else {
                        cv.put(COLUMN_HP, crawlPower);
                    }
                    cv.put(COLUMN_PLATE, plate);

                    cv.put(COLUMN_COUNT, "placeholder");

                    if (crawlAcc.equals("Unknown")){
                        cv.put(COLUMN_ACCELERATION, acceleration);
                    }else {
                        cv.put(COLUMN_ACCELERATION, crawlAcc);
                    }

                    if (crawlTopspeed.equals("Unknown")){
                        cv.put(COLUMN_TOPSPEED, topspeed);
                    }else {
                        cv.put(COLUMN_TOPSPEED, crawlTopspeed);
                    }

                    if (crawlRank.equals("Unknown")){
                        cv.put(COLUMN_RANK, rank);
                    }else {
                        cv.put(COLUMN_RANK, crawlRank);
                    }
                    cv.put(COLUMN_NOTE, note);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Updated successfully", Toast.LENGTH_SHORT).show();
        }
    }

    void updateWithoutPlate(String row_id, String brand, String type, String edition, String price,
                            String power, String color, String year, String acceleration,
                            String topspeed, String rank, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

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
        cv.put(COLUMN_NOTE, note);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Updated successfully", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Entry deleted", Toast.LENGTH_SHORT).show();
        }

    }

    public Cursor search(String term){
        String query = "SELECT * FROM " + TABLE_NAME+" WHERE "+"UPPER("+COLUMN_BRAND+") = ? order by "+COLUMN_TYPE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, new String[]{term.toUpperCase()});
        }
        return cursor;
    }

    public Cursor searchType(String term){
        String query = "SELECT * FROM " + TABLE_NAME+" WHERE "+"UPPER("+COLUMN_TYPE+") = ?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, new String[]{term.toUpperCase()});
        }
        return cursor;
    }

    public String searchFastest(){
        String query = "SELECT car_brand, car_type, car_rank FROM " + TABLE_NAME +" where car_rank > 0";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, new String[]{});
        }

        ArrayList<String> ranking = cursorToArrayList(cursor);
        if (!ranking.isEmpty()) {
            ArrayList<String> rankedCars = new ArrayList<>();
            ArrayList<Integer> rankedRanks = new ArrayList<>();
            int lowestRank = Integer.MAX_VALUE;
            int pos = 0;
            for (int i = 0; i < ranking.size(); i = i + 3) {
                rankedCars.add(ranking.get(i) + " " + ranking.get(i + 1));
                rankedRanks.add(Integer.parseInt(ranking.get(i + 2)));
            }
            for (int i = 0; i < rankedRanks.size(); i++) {
                if (rankedRanks.get(i) < lowestRank) {
                    lowestRank = rankedRanks.get(i);
                    pos = i;
                }
            }
            return rankedCars.get(pos);
        }else{
            return "";
        }
    }

    public ArrayList<String> searchMostFrequentBrand(){
        String query = "SELECT *, count(car_brand) as brand_freq FROM "+TABLE_NAME+" GROUP BY "+COLUMN_BRAND+" ORDER BY brand_freq DESC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, new String[]{});
        }
        return cursorToArrayList(cursor);
    }

    public ArrayList<String> getPrices(){
        String query = "SELECT "+COLUMN_PRICE+" from "+TABLE_NAME+" where "+COLUMN_PRICE+" != \"Unknown\"";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, new String[]{});
        }
        return cursorToArrayList(cursor);
    }

    public int getTotalValue(ArrayList<String> prices){
        int sum = 0;
        for(String price : prices){
            if(!price.equals("")) {
                sum += Integer.parseInt(price.replaceAll("\\.|€|,|-", ""));
            }
        }
        return sum;
    }

    public ArrayList<String> getDistinctColors(){
        String query = "SELECT count(distinct "+COLUMN_COLOR+") from "+TABLE_NAME+" where "+COLUMN_COLOR+" != \"Unknown\"";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, new String[]{});
        }
        return cursorToArrayList(cursor);
    }

    public ArrayList<String> getDistinctBrands(){
        String query = "SELECT count(distinct "+COLUMN_BRAND+") from "+TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, new String[]{});
        }
        return cursorToArrayList(cursor);
    }

    public ArrayList<String> getOldest(){
        String query = "SELECT car_brand, car_type, min(car_year) from "+TABLE_NAME+" where car_year > 0";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, new String[]{});
        }
        return cursorToArrayList(cursor);
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



    @SuppressLint("Range")
    public ArrayList<String> cursorToArrayList(Cursor cursor){
        ArrayList<String> cursorList = new ArrayList<>();
        if (cursor.moveToFirst() ){
            String[] columnNames = cursor.getColumnNames();
            do {
                for (String name: columnNames) {
                    cursorList.add(cursor.getString(cursor.getColumnIndex(name)));
                }
            } while (cursor.moveToNext());
        }
        return cursorList;
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
