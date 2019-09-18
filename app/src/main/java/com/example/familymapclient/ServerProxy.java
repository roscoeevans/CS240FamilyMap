package com.example.familymapclient;

import android.util.Log;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import Request.LoginRequest;
import Request.RegisterRequest;
import Response.EventResponse;
import Response.LoginResponse;
import Response.PersonResponse;
import Response.RegisterResponse;

public class ServerProxy {
    public static void main(String[] args) {

        String serverHost = args[0];
        String serverPort = args[1];
    }

    public static EventResponse getEvents(String serverHost, String serverPort, String authToken) { //NOT SURE IF THESE ARE THE RIGHT PARAMS

        EventResponse response = null;
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);

            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Accept", "application/json");

            http.connect();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream stream = http.getInputStream();
                String resp = readString(stream);
                Gson gson = new Gson();
                response = gson.fromJson(resp, EventResponse.class);
                return response;
            } else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static PersonResponse getPersons(String serverHost, String serverPort, String personID, String authToken) { //NOT SURE IF THESE ARE THE RIGHT PARAMS

        PersonResponse response = null;

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person/");

            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            http.setRequestMethod("GET");
            http.setDoOutput(false);

            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Accept", "application/json");

            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream stream = http.getInputStream();
                String resp = readString(stream);
                Gson gson = new Gson();
                response = gson.fromJson(resp, PersonResponse.class);
                return response;
            } else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static LoginResponse login(String serverHost, String serverPort, LoginRequest request) {

        LoginResponse response = new LoginResponse("", "", "");

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);    // There is a request body
            http.addRequestProperty("Accept", "application/json");
            http.connect();
            Gson gson = new Gson();
            String reqData = gson.toJson(request);
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream stream = http.getInputStream();
                String resp = readString(stream);
                response = gson.fromJson(resp, LoginResponse.class);
                return response;
            } else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static RegisterResponse register(String serverHost, String serverPort, RegisterRequest request) {
        RegisterResponse response = null;
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);    // There is a request body
            http.addRequestProperty("Accept", "application/json");

            http.connect();

            Gson gson = new Gson();
            String reqData = gson.toJson(request);

            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream stream = http.getInputStream();
                String jRespBody = readString(stream);
                response = gson.fromJson(jRespBody, RegisterResponse.class);
            } else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
