package com.example.carcollectiondatabase;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Crawler {

    public Crawler(){
    }

    public String translate_color(String s){
        switch (s.toLowerCase()){
            case "zwart": return "Black";
            case "wit": return "White";
            case "geel": return "Yellow";
            case "groen": return "Green";
            case "grijs": return "Grey";
            case "blauw": return "Blue";
            case "rood": return "Red";
            case "zilver": return "Silver";
            case "bruin": return "Brown";
            case "paars": return "Purple";
            case "goud": return "Gold";
            case "oranje": return "Orange";
            default: return s;
        }
    }

    public ArrayList<String> getCarDataFast(String kenteken) throws IOException {
        ArrayList<String> data = new ArrayList<>();
        String result = "";

        String url = "https://www.rdwdata.nl/kenteken/" + kenteken;

        try {

            URL url_object = new URL(url);
            // read text returned by server
            BufferedReader in = new BufferedReader(new InputStreamReader(url_object.openStream()));


            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains("Voertuigssoort")) {
                    in.readLine();
                    String vehicle = in.readLine();
                    if (vehicle.contains("Motor")){
                        data.add("motorcycle");
                    }else {
                        data.add("car");
                    }
                    in.close();
                    return data;
                }
            }

            in.close();

        } catch (MalformedURLException e) {
            System.out.println("Malformed URL: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O Error: " + e.getMessage());
        }

        return data;
    }

    String clean_data(String raw){
        String clean = "";
        boolean relevant = false;
        for(int i = 0; i<raw.length(); i++){
            if (raw.charAt(i) == '>'){
                relevant = true;
                continue;
            }
            if (raw.charAt(i) == '<'){
                relevant = false;
                continue;
            }
            if (relevant){
                clean += raw.charAt(i);
            }
        }
        if (clean.equals("-") || clean.equals("- km/h")){
            return "";
        }
        return clean;
    }

    String translate(String raw){
        if (raw.equals("Niet geregistreerd") || raw.equals("Onbekend")){
            return "Unknown";
        }
        return raw.replaceAll("km/u", "km/h");
    }

    String translate_power(String raw){
        return raw.replaceAll("PK", "HP");
    }

    String clean_price2(String raw){return raw.replaceAll("&#x20AC; ", "€");}

    String clean_rank(String raw) {
        String clean = "";
        boolean relevant = false;
        for (int i = 0; i < raw.length(); i++) {
            if (Character.isDigit(raw.charAt(i)) || raw.charAt(i) == '.' ){
                relevant = true;
            }
            else{relevant = false;}
            if (relevant) {
                clean += raw.charAt(i);
            }
        }
        return clean.replaceAll("\\.","");
    }


    public ArrayList<String> getCarData3(String plate){
        ArrayList<String> data = new ArrayList<>();
        String url = "https://centraalbeheer.finnik.nl/kenteken/" + plate + "/gratis";

        try {

            URL url_object = new URL(url);

            // read text returned by server
            BufferedReader in = new BufferedReader(new InputStreamReader(url_object.openStream()));

            String line;

            int line_counter = -1;
            int  brand, type, edition, price, year, color, acc, top, power, start, stop1, stop2, stop3;
            brand = -1;
            type = -1;
            price = -1;
            edition = -1;
            year = -1;
            acc = -1;
            top = -1;
            power = -1;
            color = -1;
            while ((line = in.readLine()) != null) {
                line_counter++;
                if (line.contains("<p>Merk, model, kleur en meer</p>")){
                    start = line_counter;
                    brand = start+14;
                    type = brand + 12;
                    edition = type + 10;
                    year = edition + 9;
                    color = year + 34;
                }
                if (line.contains("<p>Waarde-indicatie, onderhoud en meer</p>")){
                    stop1 = line_counter;
                    acc = stop1+18;
                    top = acc+6;
                }
                if (line.contains("<p>Exacte waarde, belasting en bijtelling</p>")){
                    stop2 = line_counter;
                    price = stop2 + 16;
                }

                if (line.contains("<p>Vermogen, snelheid, gewicht en meer</p>")){
                    stop3 = line_counter;
                    power = stop3 + 52;
                }
                if (line.contains("sneller of meer PK")){
                    data.add(clean_rank(line));
                    in.close();
                    return data;
                }

                //Brand
                if (line_counter == brand) {
                    data.add(line);
                }
                //Type
                if (line_counter == type){
                    data.add(translate(line.replaceAll("&#xE9;","é")));
                }
                //Edition
                if (line_counter == edition) {
                    data.add(translate(line));
                }
                //Price
                if (line_counter == price){
                    data.add(translate(clean_price2(line)));
                }
                //Year
                if (line_counter == year){
                    data.add(line);
                }
                //Color
                if (line_counter == color){
                    data.add(translate(translate_color(line)));
                }
                //0-100
                if (line_counter == acc) {
                    data.add(translate(clean_data(line)));
                }
                //Top
                if (line_counter == top) {
                    data.add(translate(clean_data(line)));
                }
                //Power
                if (line_counter == power) {
                    data.add(translate_power(translate(line)));
                }

            }
            in.close();

        } catch (MalformedURLException e) {
            System.out.println("Malformed URL: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O Error: " + e.getMessage());
        }
        return data;

    }


}
