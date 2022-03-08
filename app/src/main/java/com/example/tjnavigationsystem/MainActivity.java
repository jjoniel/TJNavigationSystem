package com.example.tjnavigationsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity {
    private HashMap<String, HashMap<String, ArrayList<String>>> paths;
    private HashMap<String, String> conversion;

    public ArrayList<String[]> dijkstra(String var1, String var2) {
        if(!paths.containsKey(var1))
            var1 = findRoom(var1);
        if(!paths.containsKey(var2))
            var2 = findRoom(var2);
        HashSet<String> var5 = new HashSet<>();
        Node var6 = new Node(0, var1);
        PriorityQueue<Node> var4 = new PriorityQueue<>();
        HashMap<String, String[]> var3 = new HashMap<>();
        var3.put(var1, null);
        var4.add(var6);

        while (true) {
            do {
                if (var4.isEmpty()) {
                    return null;
                }
                var6 = var4.remove();
                if (var6.getName().equals(var2)) {
                    String[] var11 = new String[]{var6.getName(), "END"};
                    ArrayList<String[]> var12 = new ArrayList<>();
                    var12.add(var11);

                    while (true) {
                        assert var11 != null;
                        if (var3.get(var11[0]) == null) break;
                        var11 = var3.get(var11[0]);
                        var12.add(var11);
                    }
                    Collections.reverse(var12);
                    return var12;
                }
            } while (var5.contains(var6.getName()));
            var5.add(var6.getName());

            for (String var7 :
                    Objects.requireNonNull(paths.get(var6.getName())).keySet())
                for (String var8 :
                        Objects.requireNonNull(paths.get(var6.getName()).get(var7))) {
                    if (paths.containsKey(var8) && !var5.contains(var8)) {
                        var3.put(var8, new String[]{var6.getName(), var7});
                        var4.add(new Node(var6.getDistance() + 1, var8));
                    }
                }
        }
    }
    public ArrayList<String> displayRoute(@NonNull ArrayList<String[]> var1, String var2) {
        boolean var4 = false;
        ArrayList<String> route = new ArrayList<>();
        for (String[] var5 : var1) {
            if (var5[0].contains("STAIR")) {
                route.add("\nUSE " + var5[0]);
                var4 = true;
            } else if (var5[0].contains("RAMP_")) {
                route.add("\nFOLLOW THE COURSE OF THE HALL IN FRONT OF YOU");
                var4 = false;
            } else {
                route.add("\n" + var5[0]);
                route.add("\n" + turn(var2, var5[1]));
                if (var4) {
                    route.add("\nCONTINUE STRAIGHT");
                    var4 = false;
                }
            }
            var2 = var5[1];
        }
        return route;
    }

    public String findRoom(String var1) {
        for (String var2 :
                paths.keySet()) {
            for (String var5 :
                    paths.get(var2).keySet()) {
                for (String var6 :
                        paths.get(var2).get(var5))
                    if (var6.equals(var1)) {
                        return var2;
                    }
            }
        }
        return null;
    }

    protected void onCreate(Bundle var1) {
        super.onCreate(var1);
        setContentView(R.layout.activity_main);
        Scanner var3 = new Scanner(getResources().openRawResource(R.raw.paths));
        paths = new HashMap<>();
        int var2;
        while (var3.hasNextLine()) {
            String[] var4 = var3.nextLine().split(" ");
            paths.put(var4[0], new HashMap<>());

            for (var2 = 1; var2 < var4.length; ++var2) {
                String var9 = var4[var2].substring(0, 1);
                paths.get(var4[0]).put(var9, new ArrayList<String>());
                paths.get(var4[0]).get(var9).add(var4[var2].substring(2));
            }
        }
        Scanner var10 = new Scanner(getResources().openRawResource(R.raw.rooms));
        String var16;
        while (var10.hasNextLine()) {
            String[] var5 = var10.nextLine().split(" ");
            var2 = var5[0].indexOf(":");
            var16 = var5[0].substring(0, var2);
            String var13 = var5[0].substring(var2 + 1, var2 + 2);
            if (paths.containsKey(var16)) {
                for (var2 = 1; var2 < var5.length; ++var2) {
                    if (!Objects.requireNonNull(paths.get(var16)).containsKey(var13)) {
                        Objects.requireNonNull(paths.get(var16)).put(var13, new ArrayList<>());
                    }
                    Objects.requireNonNull(Objects.requireNonNull(paths.get(var16)).get(var13)).add(var5[var2]);
                }
            } else {
                paths.put(var16, new HashMap<>());
                Objects.requireNonNull(paths.get(var16)).put(var13, new ArrayList<>());

                for (var2 = 1; var2 < var5.length; ++var2) {
                    if (!Objects.requireNonNull(paths.get(var16)).containsKey(var13)) {
                        Objects.requireNonNull(paths.get(var16)).put(var13, new ArrayList<>());
                    }
                    Objects.requireNonNull(Objects.requireNonNull(paths.get(var16)).get(var13)).add(var5[var2]);
                }
            }
        }
        ArrayList<String> rooms = new ArrayList();

        for (String var19 : paths.keySet()) {
            for (String value : paths.get(var19).keySet()) {
                String var6 = value;

                for (String o : paths.get(var19).get(var6)) {
                    if(o.matches("[0-9]+"))
                        rooms.add(o);
                }
            }
        }
        conversion = new HashMap<>();
        Scanner convert = new Scanner(getResources().openRawResource(R.raw.convert));
        while(convert.hasNextLine())
        {
            String temp = convert.nextLine();
            int i = temp.indexOf('"');
            int j = temp.lastIndexOf('"');
            rooms.add(temp.substring(i+1,j));
            conversion.put(temp.substring(i+1, j), temp.substring(j+2));
        }
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.destination);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rooms);
        textView.setAdapter(adapter);
    }

    public String turn(@NonNull String var1, String var2) {
        if (var1.equals(var2)) {
            return "CONTINUE STRAIGHT";
        } else {
            switch (var1) {
                case "E":
                    if (var2.equals("S")) {
                        return "TURN RIGHT";
                    }
                    if (var2.equals("N")) {
                        return "TURN LEFT";
                    }
                    if (var2.equals("W")) {
                        return "TURN AROUND";
                    }
                    break;
                case "N":
                    if (var2.equals("E")) {
                        return "TURN RIGHT";
                    }
                    if (var2.equals("W")) {
                        return "TURN LEFT";
                    }
                    if (var2.equals("S")) {
                        return "TURN AROUND";
                    }
                    break;
                case "S":
                    if (var2.equals("W")) {
                        return "TURN RIGHT";
                    }
                    if (var2.equals("E")) {
                        return "TURN LEFT";
                    }
                    if (var2.equals("N")) {
                        return "TURN AROUND";
                    }
                    break;
                case "W":
                    if (var2.equals("N")) {
                        return "TURN RIGHT";
                    }
                    if (var2.equals("S")) {
                        return "TURN LEFT";
                    }
                    if (var2.equals("E")) {
                        return "TURN AROUND";
                    }
                    break;
            }
            return null;
        }
    }

    public void showRoute(View view) {
        TextView dest = findViewById(R.id.destination);
        String destination = dest.getText().toString();
        if(!(destination.matches("[0-9]+"))) {
            System.out.println(destination);
            destination = conversion.get(destination);
            System.out.println(destination);
        }
        dijkstra("NOBEL", destination);
        System.out.println("ldfoaef");
        ArrayList<String> route = displayRoute(dijkstra("NOBEL", destination), "E");
        switchActivities(route);
    }
    private void switchActivities(ArrayList<String> arg) {
        Intent switchActivityIntent = new Intent(this, NavigationActivity.class);
        switchActivityIntent.putExtra("route", arg);
        startActivity(switchActivityIntent);
    }

    private static class Node implements Comparable<Node> {
        private final int distance;
        private final String name;

        public Node(int d, String n) {
            distance = d;
            name = n;
        }

        public int compareTo(@NonNull Node var1) {
            return distance - var1.getDistance();
        }

        public int getDistance() {
            return distance;
        }

        public String getName() {
            return name;
        }
    }

}
