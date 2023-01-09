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
            case "oranje": return "Orange";
            case "roze": return "Pink";
            case "rood": return "Red";
            case "wit": return "White";
            case "blauw": return "Blue";
            case "groen": return "Green";
            case "geel": return "Yellow";
            case "grijs": return "Grey";
            case "bruin": return "Brown";
            case "creme": return "Cream";
            case "paars": return "Purple";
            case "zwart": return "Black";
            case "diversen": return "Multi color";
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
        raw = raw.replaceAll("&quot;","\"");
        raw = raw.replaceAll("&#x2B;", "+");

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
            boolean section1found = false;
            boolean section2found = false;
            boolean section3found = false;
            boolean section4found = false;

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
//                System.out.println(line_counter +" " +line);

                if (line.contains("<p>Merk, model, kleur en meer</p>")){
                    section1found = true;
                    start = line_counter;
                    brand = start+14;
                    type = brand + 11;
                    edition = type + 10;
                    year = edition + 9;
                    color = year + 34;
                }
                if (section1found){
                    if (line.equals("        Merk")){
                        brand = line_counter + 3;
                    }
                    if (line.equals("        Model")){
                        type = line_counter + 3;
                    }
                    if (line.equals("        Uitvoering")){
                        edition = line_counter + 3;
                    }
                    if (line.equals("        Bouwjaar")){
                        year = line_counter + 3;
                    }
                    if (line.equals("        Kleur")){
                        color = line_counter + 3;
                    }
                }
                if (line.contains("<p>Waarde-indicatie, onderhoud en meer</p>")){
                    section2found = true;
                    stop1 = line_counter;
                    acc = stop1+18;
                    top = acc+6;
                }
                if (section2found){
                    if (line.equals("                    <h5>0-100 km/u</h5>")){
                        acc = line_counter + 1;
                    }
                    if (line.equals("                    <h5>Topsnelheid</h5>")){
                        top = line_counter + 1;
                    }
                }
                if (line.contains("<p>Exacte waarde, belasting en bijtelling</p>")){
                    section3found = true;
                    stop2 = line_counter;
                    price = stop2 + 16;
                }
                if (section3found){
                    if (line.equals("        Nieuwprijs")){
                        price = line_counter + 3;
                    }
                }

                if (line.contains("<p>Vermogen, snelheid, gewicht en meer</p>")){
                    section4found = true;
                    stop3 = line_counter;
                    power = stop3 + 52;
                }
                if (section4found){
                    if (line.equals("        Vermogen")){
                        power = line_counter + 3;
                    }
                }
                if (line.contains("sneller of meer PK")){
                    data.add(clean_rank(line));
                    in.close();
                    return data;
                }

                if (line.contains("Met de kentekencheck in 1 stap alle")){
                    data.add("");
                    in.close();
                    return data;
                }

                //Brand
                if (line_counter == brand) {
                    data.add(line.replaceAll("Citroen", "Citroën"));
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
                    data.add(translate(clean_data(line)).replaceAll(",","\\."));
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
