package com.example.familymapclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import Model.Person;

public class FilterActivity extends AppCompatActivity {

    private ModelSingleton model;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        model = ModelSingleton.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        model.getAssociatedEvents().clear();
        model.getCurrentFamily().clear();
        model.setFilterButton(true);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild) {};

        // setting list adapter
        expListView.setAdapter(listAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void prepareListData() {

        List<String> eventTypes = new ArrayList<>();

        eventTypes.add("male");
        eventTypes.add("female");
        eventTypes.add("dad's side");
        eventTypes.add("mom's side");

        for (int i = 0; i < model.getEvents().size(); i++) {

            if (!eventTypes.contains(model.getEvents().get(i).getEventType().toLowerCase())) {
                eventTypes.add(model.getEvents().get(i).getEventType().toLowerCase());
            }
        }
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Filters");
        List<String> lifeEvents = new ArrayList<String>();

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                model = ModelSingleton.getInstance();

                if (groupPosition == 0) {
                    model.setCurrentEvent(model.getAssociatedEvents().get(childPosition));
                    model.setCurrentPerson((Person) model.getEventToPerson().get(model.getCurrentEvent()));
                    startActivity(new Intent(FilterActivity.this, EventActivity.class));

                }
                else {
                    model.setCurrentPerson(model.getCurrentFamily().get(childPosition));
                    startActivity(new Intent(FilterActivity.this, PersonActivity.class));
                }
                return false;
            }
        });

        listDataChild.put(listDataHeader.get(0), eventTypes);
    }

}
