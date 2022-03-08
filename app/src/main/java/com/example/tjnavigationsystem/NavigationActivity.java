package com.example.tjnavigationsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class NavigationActivity extends AppCompatActivity {
    private TextView route_display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        route_display = findViewById(R.id.route_display);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ArrayList<String> route = extras.getStringArrayList("route");
            route_display.setText(route.toString());
        }
    }
}