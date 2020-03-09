package com.example.myapplication;

import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GetNearByPlaces extends AsyncTask<Object,String,String> {
    //public AsyncResponse delegate = null;
    private String googlePlacesData,url;
    private GoogleMap mMap;
    private Marker MyMarker;
    private HashMap<String,String> hm;

    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap) objects[0];
        url = (String)objects[1];

        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googlePlacesData = downloadUrl.ReadTheUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("Demo nearby in background",googlePlacesData);
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String,String>> nearbyPlacesList = null;
        HashMap<String,String> hm = null;
        DataParser dataParser = new DataParser();
        nearbyPlacesList = dataParser.parse(s);
        DisplayNearByPlaces(nearbyPlacesList);

    }

    private void DisplayNearByPlaces(List<HashMap<String,String>> nearbyPlacesList){

        //HashMap<String,String> hm = null;
        for(int i=0;i<nearbyPlacesList.size();i++){
            MarkerOptions mo = new MarkerOptions();
            HashMap<String,String> googleNearbyPlace = nearbyPlacesList.get(i);
            String nameofPlace = googleNearbyPlace.get("place_name");
            String vicinity = googleNearbyPlace.get("vicinity");
            String rating = googleNearbyPlace.get("rating");
            Double lat = Double.parseDouble(googleNearbyPlace.get("lat"));
            Double lng = Double.parseDouble(googleNearbyPlace.get("lng"));
//            Log.d("Demo lat", String.valueOf(lat));
//            Log.d("Demo lngv", String.valueOf(lng));
//            Log.d("Demo cafe name ",nameofPlace);
//            Log.d("Demo cafe rating",rating);
            //hm.put(nameofPlace,rating);
            LatLng latLng = new LatLng(lat ,lng);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("cafe");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
            MyMarker = mMap.addMarker(markerOptions);
        }

    }

}
