package com.example.familymapclient;

import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Arrays;

import Model.Person;
import Request.LoginRequest;
import Request.RegisterRequest;
import Response.EventResponse;
import Response.LoginResponse;
import Response.PersonResponse;
import Response.RegisterResponse;

public class SettingsActivity extends AppCompatPreferenceActivity {
    private static final String TAG = SettingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // load settings fragment
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
    }

    public static class MainPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            ModelSingleton model = ModelSingleton.getInstance();
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_main);
            PreferenceManager.setDefaultValues(getActivity(), R.xml.pref_main, false);

            Preference reSyncButton = findPreference(getString(R.string.resync_data));
            reSyncButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    ModelSingleton model = ModelSingleton.getInstance();
                    if (model.isReSync()) {
                        model.setReSync(false);
                    } else {
                        model.setReSync(true);
                        LoginTask task = new LoginTask();
                        LoginRequest request = new LoginRequest(model.getUser().getUserName(), model.getUser().getPassword());
                        task.execute(request);
                    }

                    return true;
                }
            });

            Preference logoutButton = findPreference(getString(R.string.key_logout));
            logoutButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    ModelSingleton model = ModelSingleton.getInstance();
                    model.setLoggedIn(false);
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    return true;
                }
            });

            SwitchPreference storyLines = (SwitchPreference) findPreference(getString(R.string.key_storyLines));
            storyLines.setChecked(model.getStoryLines());
            storyLines.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    ModelSingleton model = ModelSingleton.getInstance();

                    if (model.getStoryLines()) {
                        model.setStoryLines(false);
                    } else {
                        model.setStoryLines(true);
                    }
                    return true;
                }
            });

            SwitchPreference treeLines = (SwitchPreference) findPreference(getString(R.string.key_treeLines));
            treeLines.setChecked(model.getTreeLines());
            treeLines.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    ModelSingleton model = ModelSingleton.getInstance();

                    if (model.getTreeLines()) {
                        model.setTreeLines(false);
                    } else {
                        model.setTreeLines(true);
                    }
                    return true;
                }
            });

            SwitchPreference spouseLines = (SwitchPreference) findPreference(getString(R.string.key_spouseLines));
            spouseLines.setChecked(model.getSpouseLines());
            spouseLines.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    ModelSingleton model = ModelSingleton.getInstance();

                    if (model.getSpouseLines()) {
                        model.setSpouseLines(false);
                    } else {
                        model.setSpouseLines(true);
                    }
                    return true;
                }
            });

            //ListPreference spouseColor = getPreferenceManager();
            final ListPreference spouselist = (ListPreference) findPreference("spouseColor_prefList");
            spouselist.setValue(Integer.toString(model.getSpouseColor()));
            spouselist.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    ModelSingleton model = ModelSingleton.getInstance();
                    int index = spouselist.findIndexOfValue(newValue.toString());

                    if (index != -1) {
                        Toast.makeText(getActivity(), spouselist.getEntries()[index] + " chosen", Toast.LENGTH_LONG).show();
                    }
                    int color = 0;

                    switch (index) {
                        case 0:
                            index = 0;
                            break;
                        case 1:
                            index = 1;
                            break;
                        case 2:
                            index = 2;
                            break;
                    }

                    model.setSpouseColor(index);
                    return true;
                }
            });

            final ListPreference treeList = (ListPreference) findPreference("treeColor_prefList");
            treeList.setValue(Integer.toString(model.getTreeColor()));
            treeList.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    ModelSingleton model = ModelSingleton.getInstance();
                    int index = treeList.findIndexOfValue(newValue.toString());

                    if (index != -1) {
                        Toast.makeText(getActivity(), treeList.getEntries()[index] + " chosen", Toast.LENGTH_LONG).show();
                    }
                    int color = 0;

                    switch (index) {
                        case 0:
                            index = 0;
                            break;
                        case 1:
                            index = 1;
                            break;
                        case 2:
                            index = 2;
                            break;
                    }

                    model.setTreeColor(index);
                    return true;
                }
            });

            final ListPreference storyList = (ListPreference) findPreference("storyColor_prefList");
            storyList.setValueIndex(model.getStoryColor());
            storyList.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    ModelSingleton model = ModelSingleton.getInstance();
                    int index = storyList.findIndexOfValue(newValue.toString());

                    if (index != -1) {
                        Toast.makeText(getActivity(), storyList.getEntries()[index] + " chosen", Toast.LENGTH_LONG).show();
                    }
                    int color = 0;

                    switch (index) {
                        case 0:
                            index = 0;
                            break;
                        case 1:
                            index = 1;
                            break;
                        case 2:
                            index = 2;
                            break;
                    }

                    model.setStoryColor(index);
                    return true;
                }
            });

            final ListPreference mapList = (ListPreference) findPreference("mapType_prefList");
            mapList.setValue(model.getMapType());
            mapList.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    ModelSingleton model = ModelSingleton.getInstance();
                    int index = mapList.findIndexOfValue(newValue.toString());

                    if (index != -1) {
                        Toast.makeText(getActivity(), mapList.getEntries()[index] + " chosen", Toast.LENGTH_LONG).show();
                    }
                    String type = "0";

                    switch (index) {
                        case 0:
                            type = "0";
                            break;
                        case 1:
                            type = "1";
                            break;
                        case 2:
                            type = "2";
                            break;
                        case 3:
                            type = "3";
                            break;
                    }

                    model.setMapType(type);
                    return true;
                }
            });

        }

        public class LoginTask extends AsyncTask<LoginRequest, Void, LoginResponse> {
            ModelSingleton model;

            @Override
            protected LoginResponse doInBackground(LoginRequest... request) {
                model = ModelSingleton.getInstance();
                LoginResponse response = ServerProxy.login(model.getServerHost(), model.getServerPort(), request[0]);

                return response;
            }

            @Override
            protected void onPostExecute(LoginResponse response) {
                model = ModelSingleton.getInstance();
                if (response.getMessage() == null) {
                    model.getUser().setPersonID(response.getPersonID());
                    model.setAuthToken(response.getAuthToken());
                    GetEventsTask eventTask = new GetEventsTask();
                    GetDataTask task = new GetDataTask();
                    eventTask.execute(response.getAuthToken());
                    task.execute(response.getPersonID(), response.getAuthToken());
                } else {
                    Toast.makeText(getActivity(), response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        public class GetDataTask extends AsyncTask<String, Void, PersonResponse> {
            ModelSingleton model;

            @Override
            protected PersonResponse doInBackground(String... info) {
                model = ModelSingleton.getInstance();
                PersonResponse response = ServerProxy.getPersons(model.getServerHost(), model.getServerPort(), info[0], info[1]);

                return response;

            }

            @Override
            protected void onPostExecute(PersonResponse response) {
                model = ModelSingleton.getInstance();
                if (response.getMessage() == null) {
                    StringBuilder message = new StringBuilder();
                    model.setPersons(Arrays.asList(response.getData()));

                    Person[] persons = response.getData();
                    Person person = persons[0];
                    //Person person = (Person) model.getPersons().get(model.getUser().getPersonID());
                    message.append("Welcome, " + person.getFirstName() + " " + person.getLastName());
                    Toast.makeText(getActivity(), message.toString(), Toast.LENGTH_SHORT).show();

                    /*MainActivity ma =  new MainActivity();
                    ma.switchActivity();*/
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(getActivity(), response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        }

        public class GetEventsTask extends AsyncTask<String, Void, EventResponse> {
            ModelSingleton model;

            @Override
            protected EventResponse doInBackground(String... authToken) {
                model = ModelSingleton.getInstance();
                EventResponse response = ServerProxy.getEvents(model.getServerHost(), model.getServerPort(), authToken[0]);

                return response;

            }

            @Override
            protected void onPostExecute(EventResponse response) {
                model = ModelSingleton.getInstance();
                if (response.getMessage() == null) {
                    StringBuilder message = new StringBuilder();
                    model.setEvents(Arrays.asList(response.getData()));
                } else {
                    Toast.makeText(getActivity(), response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}