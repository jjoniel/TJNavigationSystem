package com.example.tjnavigationsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class NavigationActivity extends AppCompatActivity {
    private TextView route_display;
    private ArrayList<String> route;
    private int step = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        route_display = findViewById(R.id.route_display);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            route = extras.getStringArrayList("route");
            route_display.setText(route.get(step));
        }
        route_display.setOnClickListener(view -> {
            step++;
            if(step>=route.size())
                step--;
            route_display.setText(route.get(step));
        });
    }

}