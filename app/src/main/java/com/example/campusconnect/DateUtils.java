package com.example.campusconnect;

public class DateUtils {

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
}

/*
int numOfDays = DateUtils.getNumberOfDays(month, year);
 */
