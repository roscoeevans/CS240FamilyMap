package com.example.familymapclient;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import Model.Event;
import Model.Person;


public class PersonActivity extends AppCompatActivity {
    private ModelSingleton model;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        model = ModelSingleton.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        model.getAssociatedEvents().clear();
        model.getCurrentFamily().clear();
        model.setFilterButton(false);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild) {
        };

        // setting list adapter
        expListView.setAdapter(listAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().s

        TextView firstName = findViewById(R.id.firstName);
        TextView lastName = findViewById(R.id.lastName);
        TextView gender = findViewById(R.id.gender);

        firstName.setText(model.getCurrentPerson().getFirstName());
        lastName.setText(model.getCurrentPerson().getLastName());
        String genderString;
        if (model.getCurrentPerson().getGender().equals("m")) {
            genderString = "Male";
        } else {
            genderString = "Female";
        }
        gender.setText(genderString);

    }

    private void prepareListData() {
        List<Event> currentEvents = new ArrayList<>();


        if (model.getFilteredEvents().size() == 0) {
            currentEvents = model.getEvents();
        } else {
            currentEvents = model.getFilteredEvents();
        }

        for (int i = 0; i < currentEvents.size(); i++) {

            if (currentEvents.get(i).getPersonID().equals(model.getCurrentPerson().getPersonID())) {
                model.getAssociatedEvents().add(currentEvents.get(i));
            }
        }
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("LIFE EVENTS");
        listDataHeader.add("FAMILY");

        // Adding child data
        List<String> lifeEvents = new ArrayList<String>();
        List<Event> newEvents = new ArrayList<>();
        int lowest = model.getAssociatedEvents().get(0).getYear();

        Collections.sort(model.getAssociatedEvents(), new PersonActivity.EventSortingComparator());

        for (int i = 0; i < model.getAssociatedEvents().size(); i++) {

            String info = model.getAssociatedEvents().get(i).getEventType() + ": " + model.getAssociatedEvents().get(i).getCity() +
                    ", " + model.getAssociatedEvents().get(i).getCountry() + " (" + model.getAssociatedEvents().get(i).getYear() + ") \n" +
                    model.getCurrentPerson().getFirstName() + " " + model.getCurrentPerson().getLastName();

            if (model.getAssociatedEvents().get(i).getEventType().toLowerCase().equals("birth")) {
                lifeEvents.add(0, info);
                newEvents.add(0, model.getAssociatedEvents().get(i));

            } else if (model.getAssociatedEvents().get(i).getEventType().toLowerCase().equals("death")) {
                lifeEvents.add(info);
                newEvents.add(model.getAssociatedEvents().get(i));
            } else if (model.getAssociatedEvents().get(i).getYear() > lowest) {
                lifeEvents.add(info);
                newEvents.add(model.getAssociatedEvents().get(i));
            } else if (model.getAssociatedEvents().get(i).getYear() < lowest) {
                lifeEvents.add(0, info);
                newEvents.add(0, model.getAssociatedEvents().get(i));
                lowest = model.getAssociatedEvents().get(i).getYear();
            } else {
                int index = 0;

                if (lifeEvents.size() > 0) {
                    index = lifeEvents.size() - 1;
                }

                lifeEvents.add(index, info);
                newEvents.add(index, model.getAssociatedEvents().get(i));
            }
        }
        model.setAssociatedEvents(newEvents);

        List<String> family = new ArrayList<String>();
        for (int k = 0; k < model.getPersons().size(); k++) {
            String info;

            if (model.getPersons().get(k).getPersonID().equals(model.getCurrentPerson().getFather())) {
                info = model.getPersons().get(k).getFirstName() + " " + model.getPersons().get(k).getLastName() + "\n" + "Father";
                family.add(info);
                model.getCurrentFamily().add(model.getPersons().get(k));
            } else if (model.getPersons().get(k).getPersonID().equals(model.getCurrentPerson().getMother())) {
                info = model.getPersons().get(k).getFirstName() + " " + model.getPersons().get(k).getLastName() + "\n" + "Mother";
                family.add(info);
                model.getCurrentFamily().add(model.getPersons().get(k));
            } else if (model.getPersons().get(k).getSpouse().equals(model.getCurrentPerson().getPersonID())) {
                info = model.getPersons().get(k).getFirstName() + " " + model.getPersons().get(k).getLastName() + "\n" + "Spouse";
                family.add(info);
                model.getCurrentFamily().add(model.getPersons().get(k));
            } else if (hasParent(model.getPersons().get(k))) {
                if (model.getPersons().get(k).getFather().equals(model.getCurrentPerson().getPersonID())) {
                    info = model.getPersons().get(k).getFirstName() + " " + model.getPersons().get(k).getLastName() + "\n" + "Child";
                    family.add(info);
                    model.getCurrentFamily().add(model.getPersons().get(k));
                } else if (model.getPersons().get(k).getMother().equals(model.getCurrentPerson().getPersonID())) {
                    info = model.getPersons().get(k).getFirstName() + " " + model.getPersons().get(k).getLastName() + "\n" + "Child";
                    family.add(info);
                    model.getCurrentFamily().add(model.getPersons().get(k));
                }
            }
        }

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                model = ModelSingleton.getInstance();
                if (groupPosition == 0) {
                    model.setCurrentEvent(model.getAssociatedEvents().get(childPosition));
                    model.setCurrentPerson((Person) model.getEventToPerson().get(model.getCurrentEvent()));
                    startActivity(new Intent(PersonActivity.this, EventActivity.class));

                } else {
                    model.setCurrentPerson(model.getCurrentFamily().get(childPosition));
                    startActivity(new Intent(PersonActivity.this, PersonActivity.class));
                }
                return false;
            }
        });

        listDataChild.put(listDataHeader.get(0), lifeEvents); // Header, Child data
        listDataChild.put(listDataHeader.get(1), family);
    }

    private boolean hasParent(Person person) {
        if (person.getMother() == null) {
            return false;
        } else if (person.getFather() == null) {
            return false;
        }
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
