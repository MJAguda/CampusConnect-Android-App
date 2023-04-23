package com.example.campusconnect;

// TODO Use Network Time Protocol / Time Zone

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

    private static final String URL_TIME_API = "https://timeapi.io/api/Time/current/coordinate";
    private static final String LATITUDE = "18";
    private static final String LONGITUDE = "121";

    private static String month;
    private static String day;
    private static String year;
    private static String currentTimeIn24Hours;
    private static String currentTimeIn12Hours;

    Activity activity;
    String url = URL_TIME_API + "?latitude=" + LATITUDE + "&longitude=" + LONGITUDE;

    RequestQueue requestQueue;


    public DateUtils(Activity activity){
        this.activity = activity;

        requestQueue = Volley.newRequestQueue(activity);
    }

    public void getDateTime(VolleyCallBack volleyCallBack){
        JSONObject jsonObject = new JSONObject();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    // get Hour from HH:mm
                    String time24Hours = response.getString("time");
                    String time12Hours;
                    String[] parts = time24Hours.split(":");
                    int Hour = Integer.parseInt(parts[0]);
                    int Minute = Integer.parseInt(parts[1]);
                    int Seconds = Integer.parseInt(response.getString("seconds"));

                    String meridiem;

                    if(Hour == 12){
                        meridiem = "PM";
                    }
                    else if(Hour >= 12){
                        Hour -= 12;
                        meridiem = "PM";
                    }
                    else{
                        meridiem = "AM";
                    }
                    time12Hours = String.format("%02d:%02d %s", Hour, Minute, meridiem);

                    volleyCallBack.onGetDateTime(response.getString("month"), response.getString("day"), response.getString("year"),time24Hours, time12Hours);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(request);
    }

    public interface VolleyCallBack{
        void onGetDateTime(String month, String day, String year, String currentTimeIn24Hours, String currentTimeIn12Hours);
    }

    public static int getNumberOfDays(String month, String year) {
        int day = 0;
        if(month.equals("January") || month.equals("March") || month.equals("May") || month.equals("July") || month.equals("August") || month.equals("October") || month.equals("December")) {
            day = 31;
        }
        else if (month.equals("April") || month.equals("June") || month.equals("September") || month.equals("November")){
            day = 30;
        }
        else if (month.equals("February")){
            int yr = Integer.parseInt(year);
            // Check year if Leap Year
            if((yr % 400 == 0) || (yr % 4 == 0 && yr % 100 != 0)){
                day = 29;
            }
            else {
                day = 28;
            }
        }
        return day;
    }

    public static String getMonthName(String month) {
        int monthInt = Integer.parseInt(month);
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[monthInt-1];
    }

    public static int getMonthNumber(String monthName){
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for(int i = 0 ; i < monthNames.length ; i++){
            if(monthName.equals(monthNames[i])){
                return i;
            }
        }
        return -1;
    }
}