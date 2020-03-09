package com.example.myapplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser {

    private HashMap<String,String> getSingeNearByPlace(JSONObject googlePlaceJSON){
        HashMap<String,String> googlePlaceMap = new HashMap<>();
        String NameOfPlace = "-NA-";
        String vicinity = "-NA-";
        String latitude ="";
        String longitude ="";
        String reference ="";
        int rating = 0;

        try {
            if(!googlePlaceJSON.isNull("name")){
                NameOfPlace = googlePlaceJSON.getString("name");
            }

            if(!googlePlaceJSON.isNull("vicinity")){
                NameOfPlace = googlePlaceJSON.getString("vicinity");
            }

            latitude = googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference = googlePlaceJSON.getString("reference");
            rating = googlePlaceJSON.getInt("rating");

            googlePlaceMap.put("place_name",NameOfPlace);
            googlePlaceMap.put("vicinity",vicinity);
            googlePlaceMap.put("lat",latitude);
            googlePlaceMap.put("lng",longitude);
            googlePlaceMap.put("reference",reference);
            googlePlaceMap.put("rating", String.valueOf(rating));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return googlePlaceMap;

    }

    private List<HashMap<String,String>> getAllNearByPlaces(JSONArray jsonArray){
        int counter = jsonArray.length();

        List<HashMap<String,String>> NearByPlacesList = new ArrayList<>();
        HashMap<String,String> NearbyPlaceMap = null;

        for(int i=0;i<counter;i++){
            try {
                NearbyPlaceMap = getSingeNearByPlace((JSONObject) jsonArray.get(i));
                NearByPlacesList.add(NearbyPlaceMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return NearByPlacesList;

    }

    public List<HashMap<String,String>> parse(String JSONData){
        JSONArray jsonArray = null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(JSONData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getAllNearByPlaces(jsonArray);
    }


}


