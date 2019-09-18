package com.example.familymapclient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Arrays;

import Model.Person;
import Request.LoginRequest;
import Request.RegisterRequest;
import Response.EventResponse;
import Response.LoginResponse;
import Response.PersonResponse;
import Response.RegisterResponse;


public class LoginFragment extends Fragment {
    private ModelSingleton model;
    private EditText userName;
    private EditText password;
    private EditText host;
    private EditText port;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private RadioGroup rg;
    private Button login;
    private Button register;


    public LoginFragment() {
        // Required empty public constructor
    }

    private void setUp(View v) {
        userName = v.findViewById(R.id.userName);
        password = v.findViewById(R.id.password);
        host = v.findViewById(R.id.serverHost);
        port = v.findViewById(R.id.serverPort);
        email = v.findViewById(R.id.email);
        firstName = v.findViewById(R.id.first);
        lastName = v.findViewById(R.id.last);
        rg = v.findViewById(R.id.genders);
        login = v.findViewById(R.id.loginButton);
        register = v.findViewById(R.id.registerButton);
        login.setEnabled(false);
        register.setEnabled(false);
    }

    private void CheckButtons() {

        boolean loginFields = true;
        boolean registerFields = true;
        boolean webInfo = true;

        if (model.getUser().getUserName().equals("")) {
            loginFields = false;
            registerFields = false;
            login.setEnabled(false);
            register.setEnabled(false);
        }
        if (model.getUser().getPassword().equals("")) {
            loginFields = false;
            registerFields = false;
            login.setEnabled(false);
            register.setEnabled(false);
        }
        if (model.getServerHost().equals("") || model.getServerPort().equals("")) {
            webInfo = false;
            login.setEnabled(false);
            register.setEnabled(false);
        }
        if (model.getUser().getEmail().equals("")) {
            registerFields = false;
            register.setEnabled(false);
        }
        if (model.getUser().getFirstName().equals("")) {
            registerFields = false;
            register.setEnabled(false);
        }
        if (model.getUser().getLastName().equals("")) {
            registerFields = false;
            register.setEnabled(false);
        }
        if (model.getUser().getGender().equals("")) {
            registerFields = false;
            register.setEnabled(false);
        }

        if (registerFields && webInfo) {
            this.register.setEnabled(true);
        }
        if (loginFields && webInfo) {
            this.login.setEnabled(true);
        }

    }

    public class LoginTask extends AsyncTask<LoginRequest, Void, LoginResponse> {

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
                Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class RegisterTask extends AsyncTask<RegisterRequest, Void, RegisterResponse> {

        @Override
        protected RegisterResponse doInBackground(RegisterRequest... request) {
            model = ModelSingleton.getInstance();
            RegisterResponse response = ServerProxy.register(model.getServerHost(), model.getServerPort(), request[0]);

            return response;

        }

        @Override
        protected void onPostExecute(RegisterResponse response) {
            model = ModelSingleton.getInstance();
            if (response.getMessage() == null) {
                model.getUser().setPersonID(response.getPersonID());
                model.setAuthToken(response.getAuthToken());
                GetEventsTask eventTask = new GetEventsTask();
                GetDataTask task = new GetDataTask();
                eventTask.execute(response.getAuthToken());
                task.execute(response.getPersonID(), response.getAuthToken());
            } else {
                Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    public class GetDataTask extends AsyncTask<String, Void, PersonResponse> {

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
                message.append("Welcome, " + person.getFirstName() + " " + person.getLastName());
                Toast.makeText(getContext(), message.toString(), Toast.LENGTH_SHORT).show();

                MainActivity ma = (MainActivity) getActivity();
                ma.switchActivity();
            } else {
                Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    public class GetEventsTask extends AsyncTask<String, Void, EventResponse> {

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
                Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        model = ModelSingleton.getInstance();
        View v = inflater.inflate(R.layout.fragment_login, container, false);

            setUp(v);

            rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.male:
                            model.getUser().setGender("m");
                            CheckButtons();
                            break;
                        case R.id.female:
                            model.getUser().setGender("f");
                            CheckButtons();
                            break;
                    }
                }
            });

            this.port.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    model.setServerPort(s.toString());
                    CheckButtons();

                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            this.host.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    model.setServerHost(s.toString());
                    CheckButtons();

                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            this.userName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    model.getUser().setUserName(s.toString());
                    CheckButtons();

                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            password.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    model.getUser().setPassword(s.toString());
                    CheckButtons();

                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            firstName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    model.getUser().setFirstName(s.toString());
                    CheckButtons();

                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            lastName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    model.getUser().setLastName(s.toString());
                    CheckButtons();

                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            email.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    model.getUser().setEmail(s.toString());
                    CheckButtons();

                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LoginTask task = new LoginTask();
                    LoginRequest request = new LoginRequest(model.getUser().getUserName(), model.getUser().getPassword());
                    task.execute(request);
                }

            });

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RegisterTask task = new RegisterTask();
                    RegisterRequest request = new RegisterRequest(model.getUser().getUserName(), model.getUser().getPassword(), model.getUser().getEmail(), model.getUser().getFirstName(), model.getUser().getLastName(), model.getUser().getGender());
                    task.execute(request);
                }

            });

            return v;
        }
}
