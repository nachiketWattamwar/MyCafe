package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DetailsCafe extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    String s1[],s2[];
    RecyclerView recyclerView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    public HashMap<String,String> placesMapOfCafe = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_cafe);
        s1 = getResources().getStringArray(R.array.programming);
        //s2 = getResources().getStringArray(R.array.desc);


        Intent i = getIntent();
        placesMapOfCafe = new HashMap<String,String>();
        placesMapOfCafe = (HashMap<String,String>)i.getSerializableExtra("nameAndRating");
        List<Map.Entry<String,String>> list = new LinkedList<Map.Entry<String, String>>(placesMapOfCafe.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                Double result1 = Double.parseDouble(o1.getValue());
                Double result2 = Double.parseDouble(o2.getValue());
                return result2.compareTo(result1);
            }
        });

        Log.d("Demo placesMapOfCafe", String.valueOf(list));

        LinkedHashMap<String,String> linkedHashMap = new LinkedHashMap<String,String>();
        for(Map.Entry<String,String> eachElement : list){
            linkedHashMap.put(eachElement.getKey(),eachElement.getValue());
        }
        s1 = new String[linkedHashMap.size()];
        s2 = new String[linkedHashMap.size()];
        int counter = 0;
        for(Map.Entry eachElement : linkedHashMap.entrySet()){
            s1[counter] = eachElement.getKey().toString();
            s2[counter] = eachElement.getValue().toString();
            counter++;
        }

        Log.d("Demo s1 is ", String.valueOf(s1[0]));
        Log.d("Demo s2 is ", String.valueOf(s2[0]));


        recyclerView = findViewById(R.id.recyclerView);
        drawerLayout = findViewById(R.id.drawernew);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        MyAdapter myAdapter = new MyAdapter(this,s1,s2);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.mapid:
                Intent i = new Intent(this,MapsActivity.class);
                startActivity(i);

                Toast.makeText(this,"map selected",Toast.LENGTH_SHORT).show();
                break;

            case R.id.listid:
                i = new Intent(this,DetailsCafe.class);
                startActivity(i);
                Toast.makeText(this,"list id  selected",Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }
}
