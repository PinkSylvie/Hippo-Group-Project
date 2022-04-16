package com.example.hippo;

import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.time.Month;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class HippoMonth {
    private final static String TAG = "HippoMonth";

    public static final int DECEMBER = 12;
    public static final int JANUARY = 1;


    private String name;
    private int year;
    private int days;
    private int startingDay;
    private int monthNumber;

    public HippoMonth(int month, int year){
        name = Month.of(month).name();

        this.year = year;

        this.monthNumber = month;

        YearMonth yearMonthObj = YearMonth.of(year, month);
        days = yearMonthObj.lengthOfMonth();

        Calendar cal = new GregorianCalendar();
        cal.set(year, month - 1, 1);
        Log.i(TAG, cal.getTime().toString());
        startingDay = cal.get(Calendar.DAY_OF_WEEK);
    }


    public void arrangeDays(TableLayout tl){
        boolean isFirstDaySet = false;
        int dayNumber = 1;

        int firstNameDayOfMonth = getStartingDay() - 1;
        int daysInMonth = getMaxDays() + 1;

        for(int i = 1; i < tl.getChildCount(); i++){
            View view = tl.getChildAt(i);
            if(view instanceof TableRow){
                TableRow row = (TableRow) view;
                for(int j = 0; j < row.getChildCount(); j++){
                    if(!isFirstDaySet && j == firstNameDayOfMonth && dayNumber < daysInMonth){
                        isFirstDaySet = true;
                        TextView day = (TextView) row.getChildAt(j);
                        day.setText(String.valueOf(dayNumber));
                        dayNumber++;
                    }else if(isFirstDaySet && dayNumber < daysInMonth){
                        TextView day = (TextView) row.getChildAt(j);
                        day.setText(String.valueOf(dayNumber));
                        dayNumber++;
                    } else{
                        TextView day = (TextView) row.getChildAt(j);
                        day.setText("");
                    }
                }
            }
        }

    }

    public int getMaxDays(){
        return days;
    }

    public int getYear(){
        return year;
    }

    public int getStartingDay(){
        return startingDay;
    }

    public int getMonthNumber(){
        return monthNumber;
    }

    public String getMonth(){
        return name;
    }

}
