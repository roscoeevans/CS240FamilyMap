package com.example.familymapclient;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import java.util.Comparator;
import Model.Event;

public class EventActivity extends AppCompatActivity {
    private ModelSingleton model;
    private float color;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        color = new Float("0.0");
        model = ModelSingleton.getInstance();

        ModelSingleton model = ModelSingleton.getInstance();
        if (!model.isReSync()) {
            if (!model.isLoggedIn()) {
                model.clear();
            }
        }

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment != null) {
            fm.beginTransaction().remove(fragment).commit();
        }

        Fragment mapFragment = new MapFragment();
        fm.beginTransaction().add(R.id.fragment_container, mapFragment).commit();

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(false);
        menu.getItem(2).setVisible(false);
        return true;
    }

    static class EventSortingComparator implements Comparator<Event> {

        @Override
        public int compare(Event event1, Event event2) {

            Event eventA = event1;
            Event eventB = event2;

            // for comparison
            int year1 = eventA.getYear();
            int year2 = eventB.getYear();

            // 2-level comparison using if-else block
            if (eventA.getEventType().equals("birth")) {
                return -1;
            } else if (eventB.getEventType().equals("birth")) {
                return 1;
            } else {
                return year1 - year2;
            }
        }
    }
}
