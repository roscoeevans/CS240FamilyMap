package com.example.familymapclient;

import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ERROR = 0;
    ModelSingleton model;

    public void switchActivity() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment != null) {
            fm.beginTransaction().remove(fragment).commit();
        }

        Fragment mapFragment = new MapFragment();
        fm.beginTransaction().add(R.id.fragment_container, mapFragment).commit();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.pref_main, false);
        ModelSingleton model = ModelSingleton.getInstance();
        if (!model.isReSync()) {
            if (!model.isLoggedIn()) {
                model.clear();
            }
        }

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new LoginFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        model = ModelSingleton.getInstance();

        if (model.isLoggedIn()) {
            switchActivity();
        } else {
            GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
            int errorCode = apiAvailability.isGooglePlayServicesAvailable(this);

            if (errorCode != ConnectionResult.SUCCESS) {
                Dialog errorDialog = apiAvailability.getErrorDialog(this, errorCode, REQUEST_ERROR,
                        new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                finish();
                            }
                        });
                errorDialog.show();
            }
        }
        model.setLoggedIn(true);
    }
}
