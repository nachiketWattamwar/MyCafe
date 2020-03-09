package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, SearchView.OnQueryTextListener {

    String provider;
    //Location lastLocation;
    private GoogleApiClient mGoogleApiClient;
    private Marker currentUserLocationMarker,cafes;
    private Double lat,lng;
    private JSONArray jsonArray = null;
    private static final int request_user_location_code = 99;
    public static final String TAG = MapsActivity.class.getSimpleName();
    FusedLocationProviderClient fusedLocationProviderClient;
    GoogleMap mMap;
    LocationRequest locationRequest;
    public Location lastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;
    String queryCafe;
    private TreeMap<String, String> placesMapOfCafes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        //fetchLocation();
        final SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkUserLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
//    private void fetchLocation() {
//        if (ActivityCompat.checkSelfPermission(
//                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
//            return;
//        }
//
//        if(fusedLocationProviderClient != null){
//            Task<Location> task = fusedLocationProviderClient.getLastLocation();
//            task.addOnSuccessListener(new OnSuccessListener<Location>() {
//                @Override
//                public void onSuccess(Location location) {
//                    if (location != null) {
//                        currentLocation = location;
//                        Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
////                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.myMap);
////                    assert supportMapFragment != null;
////                    supportMapFragment.getMapAsync(MapsActivity.this);
//                    }
//                }
//            });
//
//
//        }
//
//
//    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(ContextCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }


//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(120000); // two minute interval
//        mLocationRequest.setFastestInterval(120000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)
//                    == PackageManager.PERMISSION_GRANTED) {
//                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
//                mMap.setMyLocationEnabled(true);
//            } else {
//                checkLocationPermission();
//            }
//        }
//        else {
//            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
//            mMap.setMyLocationEnabled(true);
//        }

        // Add a marker in Sydney and move the camera

//        if(currentLocation != null){
//
//            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//
//            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Sydney"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//
//        }

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        mGoogleApiClient.connect();
    }


//    LocationCallback mLocationCallback = new LocationCallback() {
//        @Override
//        public void onLocationResult(LocationResult locationResult) {
//            List<Location> locationList = locationResult.getLocations();
//            if (locationList.size() > 0) {
//                //The last location in the list is the newest
//                Location location = locationList.get(locationList.size() - 1);
//                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
//                mLastLocation = location;
//                System.out.println("inside maps "+location.getLatitude());
//                if (mCurrLocationMarker != null) {
//                    mCurrLocationMarker.remove();
//                }
//
//                //Place current location marker
//
//                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//
//                //move map camera
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
//            }
//        }
//    };

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        if(currentUserLocationMarker!=null) {
            currentUserLocationMarker.remove();
        }
        lat = location.getLatitude();
        lng = location.getLongitude();
        Log.d("default lat", lat.toString());
        Log.d("default long is ", lng.toString());
        LatLng latLng = new LatLng(location.getLatitude() , location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("My location!");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        currentUserLocationMarker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(14));

        if(mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient , this);
        }

    }
    public boolean checkUserLocationPermission() {
        if(ContextCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if(ActivityCompat.shouldShowRequestPermissionRationale(this , Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this , new String[]{Manifest.permission.ACCESS_FINE_LOCATION} , request_user_location_code);
            }
            else {
                ActivityCompat.requestPermissions(this , new String[]{Manifest.permission.ACCESS_FINE_LOCATION} , request_user_location_code);

            }
            return false;

        }
        else {
            return true;
        }
    }
    private String getUrl(double lat, double lng, String cafe){
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+lat+","+lng);
        googlePlaceUrl.append("&radius=10000");
        googlePlaceUrl.append("&type="+cafe);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"insert key here");

        return googlePlaceUrl.toString();
    }

//    public void showNearbyCafes(double latitude, double longitude){
//        String cafe = "cafe";
//        String url = getUrl(latitude, longitude, cafe);
//        Object[] dataTransfer = new Object[4];
//        dataTransfer[0] = mMap;
//        dataTransfer[1] = url;
//        dataTransfer[2] = latitude;
//        dataTransfer[3] = longitude;
//
//        Log.d("data", String.valueOf(dataTransfer));
//        NearbyPlacesData nearbyPlacesData = new NearbyPlacesData();
//        nearbyPlacesData.execute(dataTransfer);
//        View view = findViewById(R.id.mapView);
//
//    }

    public void seeDetailsList(View view){
        Log.d("Demo","inside details ");
        Intent myIntent = new Intent(this, DetailsCafe.class);
        Log.d("Demo placesMapOfCafe", String.valueOf(placesMapOfCafes));
        myIntent.putExtra("nameAndRating",placesMapOfCafes);
        startActivity(myIntent);
    }

    @SuppressLint("LongLogTag")
    public void searchNearByNew(View view){
        Log.d("inside image button","tests");
        Log.d("Demo",String.valueOf(lat));
        Log.d("Demo ",String.valueOf(lng));

        String url = getUrl(lat, lng, "cafe");
        Log.d("Demo" , "url = "+url);
        Object[] dataTransfer = new Object[4];
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;

        Log.d("data", String.valueOf(dataTransfer));
        GetNearByPlaces getNearByPlaces = new GetNearByPlaces();
        try {
            String s = getNearByPlaces.execute(dataTransfer).get();
            //Log.d("Demo nearby in main activity",s);
            JSONObject allNearbyDataInJSON = new JSONObject(s);
            jsonArray = allNearbyDataInJSON.getJSONArray("results");
            Log.d("Demo all nearby in results ", String.valueOf(jsonArray));

            placesMapOfCafes = new TreeMap<String,String>();
            for(int i=0;i<jsonArray.length();i++){
               JSONObject obj = jsonArray.getJSONObject(i);
               String rating = obj.optString("rating");
               String name = obj.optString("name");
               placesMapOfCafes.put(name,rating);
            }

            Log.d("Demo before send", String.valueOf(placesMapOfCafes));


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("LongLogTag")
    public void searchCafe(){
        final String cafeName = queryCafe;

        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList = null;
        MarkerOptions mo = new MarkerOptions();
        try{

        addressList = geocoder.getFromLocationName(cafeName,3);
        Log.d("addresslist",String.valueOf(addressList));

            mMap.clear();
            for (Address a : addressList){
                Log.d("DemoLog loop called, cafe:= ", String.valueOf(a));
                Log.d(" DemoLog Lat is ", String.valueOf(a.getLatitude()));
                Log.d("DemoLog Long is ", String.valueOf(a.getLongitude()));
                LatLng latlng = new LatLng(a.getLatitude(), a.getLongitude());

                mo.position(latlng);
                mo.title("Cafe");
                mMap.addMarker(mo);
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
                mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                //searchNearBy(a.getLatitude(),a.getLongitude());
            }
            lat = addressList.get(0).getLatitude();
            lng = addressList.get(0).getLongitude();
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
        System.out.println("addresses are "+addressList);
            Log.d("addresses are ", String.valueOf(addressList));
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
    @Override
    public boolean onQueryTextSubmit(String s) {
        queryCafe = s;
        searchCafe();
        //showNearbyCafes(lastLocation.getLatitude(),lastLocation.getLongitude());
        return true;

    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);



        if(ContextCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient ,
                    locationRequest , this );

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
