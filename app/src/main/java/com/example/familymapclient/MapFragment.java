package com.example.familymapclient;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.Event;
import Model.Person;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    private ModelSingleton model;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private Map markerToEvent;
    private Map typeToColor;
    private TextView bottomText;
    private ImageView genIcon;
    private float color;
    List<Polyline> lines = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        setHasOptionsMenu(true);
        super.onCreateView(layoutInflater, viewGroup, bundle);
        View v = layoutInflater.inflate(R.layout.fragment_map, viewGroup, false);


        markerToEvent = new HashMap<Marker, Event>();
        typeToColor = new HashMap<String, Float>();

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.mapmenu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // launch settings activity
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }
        if (id == R.id.action_filter) {
            // launch filter activity
            startActivity(new Intent(getActivity(), FilterActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        color = new Float("0.0");
        model = ModelSingleton.getInstance();
        this.bottomText = (TextView) getView().findViewById(R.id.bottomText);
        this.genIcon = getView().findViewById(R.id.genIcon);

        mMap = googleMap;
        loadMapType(mMap);

        if (model.getCurrentEvent() != null) {

            String newInfo = model.getCurrentPerson().getFirstName() + " " + model.getCurrentPerson().getLastName() + "\n"
                    + model.getCurrentEvent().getEventType() + ": " + model.getCurrentEvent().getCity() +
                    ", " + model.getCurrentEvent().getCountry() + " (" + model.getCurrentEvent().getYear() + ")";

            bottomText.setText(newInfo);
            if (model.getCurrentPerson().getGender().equals("m")) {
                genIcon.setImageResource(R.mipmap.male);
            } else {
                genIcon.setImageResource(R.mipmap.female);
            }

            LatLng coordinates = new LatLng(model.getCurrentEvent().getLatitude(), model.getCurrentEvent().getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));

        }

        populateMap();

        model = ModelSingleton.getInstance();

        clearLines();

        drawAllLines();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                model = ModelSingleton.getInstance();
                model.setCurrentEvent((Event) markerToEvent.get(marker));
                model.setCurrentPerson((Person) model.getEventToPerson().get(model.getCurrentEvent()));

                Event currentEvent = (Event) markerToEvent.get(marker);
                model.setCurrentEvent(currentEvent);
                model.setCurrentPerson((Person) model.getEventToPerson().get(currentEvent));


                String newInfo = model.getCurrentPerson().getFirstName() + " " + model.getCurrentPerson().getLastName() + "\n"
                        + model.getCurrentEvent().getEventType() + ": " + model.getCurrentEvent().getCity() +
                        ", " + model.getCurrentEvent().getCountry() + " (" + model.getCurrentEvent().getYear() + ")";

                bottomText.setText(newInfo);
                if (model.getCurrentPerson().getGender().equals("m")) {
                    genIcon.setImageResource(R.mipmap.male);
                } else {
                    genIcon.setImageResource(R.mipmap.female);
                }

                LatLng coordinates = new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));


                clearLines();

                drawAllLines();

                return true;
            }
        });

        bottomText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), PersonActivity.class));
            }
        });
    }

    private void populateMap() {
        List<Event> events = model.getEvents();
        List<Person> persons = model.getPersons();

        Event event;

        runFilters(events);

        events = model.getFilteredEvents();

        model.getTypeToFilter().put("male", false);
        model.getTypeToFilter().put("female", false);
        model.getTypeToFilter().put("dad's side", false);
        model.getTypeToFilter().put("mom's side", false);
        model.getTypes().add("male");
        model.getTypes().add("female");
        model.getTypes().add("dad's side");
        model.getTypes().add("mom's side");


        for (int i = 0; i < events.size(); i++) {
            event = events.get(i);

            if (typeToColor.containsKey(event.getEventType().toLowerCase())) {
                placeMarker(event);
            } else {
                float newColor = getNextColor();

                model.getTypeToFilter().put(event.getEventType().toLowerCase(), false);
                model.getTypes().add(event.getEventType().toLowerCase());
                typeToColor.put(event.getEventType().toLowerCase(), newColor);
                placeMarker(event);
            }

            for (int j = 0; j < persons.size(); j++) {
                if (persons.get(j).getPersonID().equals(event.getPersonID())) {
                    model.getEventToPerson().put(event, persons.get(j));
                }
            }
        }

    }

    private void drawSpouseLines() {
        Person subject = (Person) model.getEventToPerson().get(model.getCurrentEvent());
        if (subject != null) {
            Person spouse = getPerson(subject.getSpouse());
            if (spouse != null) {
                Event birth = getEarliestEvent(spouse);

                if (birth != null) {
                    drawLine(birth);
                }
            }
        }
    }

    private void clearLines() {
        for (Polyline line : lines) {
            line.remove();
            //lines.remove(line);
        }
        lines.clear();
    }

    private void drawAllLines() {
        ModelSingleton model = ModelSingleton.getInstance();

        if (model.getCurrentEvent() != null) {
            if (model.getSpouseLines()) {
                drawSpouseLines();
            }
            if (model.getTreeLines()) {
                drawTreeLines();
            }
            if (model.getStoryLines()) {
                drawStoryLines();
            }
        }
    }

    private void drawTreeLines() {
        float thickness = 10;
        ModelSingleton model = ModelSingleton.getInstance();
        //model.setCurrentPerson((Person) model.getEventToPerson().get(model.getCurrentEvent()));
        if (model.getCurrentPerson() != null) {
            drawTreeLinesHelper(model.getCurrentEvent(), thickness);
        }

    }

    private void drawTreeLinesHelper(Event current, float thickness) {
        ModelSingleton model = ModelSingleton.getInstance();
        Person currentPerson = (Person) model.getEventToPerson().get(current);
        Person father = getPerson(currentPerson.getFather());
        Person mother = getPerson(currentPerson.getMother());

        if (father != null) {
            thickness = (float) (thickness * 0.7);
            Event earliestF = getEarliestEvent(father);
            drawLineHelper(current, earliestF, thickness);
            drawTreeLinesHelper(earliestF, thickness);
        }
        if (mother != null) {
            Event earliestM = getEarliestEvent(mother);
            drawLineHelper(current, earliestM, thickness);
            drawTreeLinesHelper(earliestM, thickness);
        }
    }

    private Event getEarliestEvent(Person subject) {
        Event earliest = null;
        List<Event> associatedEvents = new ArrayList<>();

        for (int i = 0; i < model.getEvents().size(); i++) {
            if (model.getEvents().get(i).getPersonID().equals(subject.getPersonID())) {
                associatedEvents.add(model.getEvents().get(i));
                earliest = model.getEvents().get(i);
            }
        }

        for (int i = 0; i < associatedEvents.size(); i++) {
            if (associatedEvents.get(i).getYear() < earliest.getYear()) {
                earliest = associatedEvents.get(i);
            }
        }

        return earliest;
    }

    private Person getPerson(String personID) {
        Person found = null;
        for (int i = 0; i < model.getPersons().size(); i++) {
            if (model.getPersons().get(i).getPersonID().equals(personID)) {
                found = model.getPersons().get(i);
            }
        }

        return found;
    }


    private void drawLine(Event birth) {
        ModelSingleton model = ModelSingleton.getInstance();
        int color = getColor(model.getSpouseColor());

        lines.add(mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(birth.getLatitude(), birth.getLongitude()), new LatLng(model.getCurrentEvent().getLatitude(), model.getCurrentEvent().getLongitude())).color(color)));
    }

    private void drawLineHelper(Event child, Event parent, float thickness) {
        ModelSingleton model = ModelSingleton.getInstance();
        int color = getColor(model.getTreeColor());

        lines.add(mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(child.getLatitude(), child.getLongitude()), new LatLng(parent.getLatitude(), parent.getLongitude())).color(color).width(thickness)));
    }

    private int getColor(int num) {
        if (num == 0) {
            return Color.RED;
        } else if (num == 1) {
            return Color.GREEN;
        } else {
            return Color.BLUE;
        }
    }

    private void loadMapType(GoogleMap mMap) {
        ModelSingleton model = ModelSingleton.getInstance();
        switch (model.getMapType()) {
            case "0":
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case "1":
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case "2":
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case "3":
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
        }
    }

    private void placeMarker(Event event) {
        LatLng coordinates = new LatLng(event.getLatitude(), event.getLongitude());
        Marker marker;
        float matchingColor = (float) typeToColor.get(event.getEventType().toLowerCase());


        marker = mMap.addMarker(new MarkerOptions().position(coordinates).title(event.getEventType()).icon(BitmapDescriptorFactory.defaultMarker(matchingColor)));
        markerToEvent.put(marker, event);
    }

    private float getNextColor() {
        color += 29;

        if (color >= 360) {
            color = 14;
        }

        return color;
    }

    private List<Event> sortChronologically(List<Event> events) {
        List<Event> storyEvents = new ArrayList<>();
        ModelSingleton model = ModelSingleton.getInstance();
        Person person = model.getCurrentPerson();
        int lowest = events.get(0).getYear();

        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getPersonID().equals(person.getPersonID())) {


                storyEvents.add(events.get(i));
            }

        }
        Collections.sort(storyEvents, new EventActivity.EventSortingComparator());
        return storyEvents;
    }

    private void drawStoryLines() {
        ModelSingleton model = ModelSingleton.getInstance();
        List<Event> events = model.getEvents();
        List<Event> storyEvents = sortChronologically(events);

        for (int i = 0; i < storyEvents.size() - 1; i++) {
            drawLine(storyEvents.get(i), storyEvents.get(i + 1));
        }

    }

    private void drawLine(Event event1, Event event2) {
        ModelSingleton model = ModelSingleton.getInstance();
        int color = getColor(model.getStoryColor());

        lines.add(mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(event1.getLatitude(), event1.getLongitude()), new LatLng(event2.getLatitude(), event2.getLongitude())).color(color)));
    }

    private void runFilters(List<Event> events) {
        ModelSingleton model = ModelSingleton.getInstance();
        if (model.getTypeToFilter().size() > 0) {
            List<Event> filteredEvents = new ArrayList<>();

            for (int i = 0; i < model.getEvents().size(); i++) {

                Event currentEvent = model.getEvents().get(i);

                Boolean filtered = (Boolean) model.getTypeToFilter().get(currentEvent.getEventType().toLowerCase());
                if (!filtered) {

                    filteredEvents.add(events.get(i));
                }
            }
            if (model.getTypeToFilter().size() == 0) {
                model.setFilteredEvents(events);
            }
            else {
                model.setFilteredEvents(filteredEvents);
            }
            if ((Boolean) model.getTypeToFilter().get("male")) {
                filterMales();
            }
        }
        else {
            model.setFilteredEvents(model.getEvents());
        }
    }

    private void filterMales() {
        for (int i = 0; i < model.getFilteredEvents().size(); i++) {
            Person currentPerson = (Person) model.getEventToPerson().get(model.getFilteredEvents().get(i));
            if (currentPerson.getGender().equals("m")) {
                model.getFilteredEvents().remove(i);
            }
        }
    }
}
