package com.example.familymapclient;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.Event;
import Model.Person;
import Model.User;

public class ModelSingleton {
    public String serverHost;
    public ServerProxy server;
    public String serverPort;
    public Model.User user;
    public List<Person> persons;
    public List<Event> events;
    public List<Event> filteredEvents;
    public String authToken;
    public Event currentEvent;
    public Person currentPerson;
    public List<Event> associatedEvents;
    public List<Person> currentFamily;
    public Map eventToPerson;
    public Map typeToFilter;
    public boolean loggedIn;
    public boolean spouseLines;
    public boolean treeLines;
    public boolean storyLines;
    public int spouseColor;
    public int treeColor;
    public int storyColor;
    public String mapType;
    public boolean reSync;
    public boolean filterButton;
    List<String> types;

    // static variable single_instance of type Singleton
    private static ModelSingleton instance = null;

    // private constructor restricted to this class itself
    private ModelSingleton() {
        loggedIn = false;
        serverHost = "";
        serverPort = "";
        server = new ServerProxy();
        user = new User();
        associatedEvents = new ArrayList<>();
        filteredEvents = new ArrayList<>();
        currentFamily = new ArrayList<>();
        types = new ArrayList<>();
        eventToPerson = new HashMap<Event, Person>();
        typeToFilter = new HashMap<String, Boolean>();
        spouseLines = false;
        treeLines = false;
        storyLines = false;
        mapType = "0";
        persons = new ArrayList<>();
        events = new ArrayList<>();
        reSync = false;
        spouseColor = 0;
        treeColor = 0;
        storyColor = 0;

    }

    public void clear() {

        types = new ArrayList<>();
        typeToFilter = new HashMap<String, Boolean>();
        spouseColor = 0;
        treeColor = 0;
        storyColor = 0;
        filteredEvents = new ArrayList<>();
        loggedIn = false;
        serverHost = "";
        serverPort = "";
        server = new ServerProxy();
        user = new User();
        associatedEvents = new ArrayList<>();
        currentFamily = new ArrayList<>();
        eventToPerson = new HashMap<Event, Person>();
        spouseLines = false;
        treeLines = false;
        storyLines = false;
        mapType = "0";
        persons = new ArrayList<>();
        events = new ArrayList<>();
        reSync = false;
    }

    // static method to create instance of Singleton class
    public static ModelSingleton getInstance() {
        if (instance == null)
            instance = new ModelSingleton();

        return instance;
    }

    public List<Event> getAssociatedEvents() {
        return associatedEvents;
    }

    public void setAssociatedEvents(List<Event> associatedEvents) {
        this.associatedEvents = associatedEvents;
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public ServerProxy getServer() {
        return server;
    }

    public void setServer(ServerProxy server) {
        this.server = server;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getCurrentEvent() {
        return currentEvent;
    }

    public Map getEventToPerson() {
        return eventToPerson;
    }

    public void setEventToPerson(Map eventToPerson) {
        this.eventToPerson = eventToPerson;
    }

    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
    }

    public Person getCurrentPerson() {
        return currentPerson;
    }

    public List<Person> getCurrentFamily() {
        return currentFamily;
    }

    public void setCurrentFamily(List<Person> currentFamily) {
        this.currentFamily = currentFamily;
    }

    public void setCurrentPerson(Person currentPerson) {
        this.currentPerson = currentPerson;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean getSpouseLines() {
        return spouseLines;
    }

    public void setSpouseLines(boolean spouseLines) {
        this.spouseLines = spouseLines;
    }

    public boolean getTreeLines() {
        return treeLines;
    }

    public void setTreeLines(boolean treeLines) {
        this.treeLines = treeLines;
    }

    public boolean getStoryLines() {
        return storyLines;
    }

    public void setStoryLines(boolean storyLines) {
        this.storyLines = storyLines;
    }

    public int getSpouseColor() {
        return spouseColor;
    }

    public void setSpouseColor(int spouseColor) {
        this.spouseColor = spouseColor;
    }

    public int getTreeColor() {
        return treeColor;
    }

    public void setTreeColor(int treeColor) {
        this.treeColor = treeColor;
    }

    public int getStoryColor() {
        return storyColor;
    }

    public void setStoryColor(int storyColor) {
        this.storyColor = storyColor;
    }

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public boolean isReSync() {
        return reSync;
    }

    public void setReSync(boolean reSync) {
        this.reSync = reSync;
    }

    public boolean isFilterButton() {
        return filterButton;
    }

    public void setFilterButton(boolean filterButton) {
        this.filterButton = filterButton;
    }

    public Map getTypeToFilter() {
        return typeToFilter;
    }

    public List<Event> getFilteredEvents() {
        return filteredEvents;
    }

    public void setFilteredEvents(List<Event> filteredEvents) {
        this.filteredEvents = filteredEvents;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}

