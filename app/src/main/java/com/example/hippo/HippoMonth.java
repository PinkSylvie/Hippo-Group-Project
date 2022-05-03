package com.example.hippo;

import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class HippoMonth {
    private final static String TAG = "HippoMonth";

    public static final int DECEMBER = 12;
    public static final int JANUARY = 1;

    protected List<Task> tasks;


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
        tasks = new ArrayList<>();
        queryTasks();
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

    protected void queryTasks(){

        ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
        query.include(Task.KEY_USER);
        query.orderByAscending(Task.KEY_DUE_TIME);

        // Constraints

        Calendar monthAfter = Calendar.getInstance();
        Calendar monthBefore = Calendar.getInstance();

        int monthNumberOffset = getMonthNumber() - 1;

        monthBefore.set(getYear(), monthNumberOffset, 1);
        monthBefore.add(Calendar.DATE, -1);

        monthAfter.set(getYear(), monthNumberOffset, 1);
        monthAfter.add(Calendar.MONTH, 1);

        Log.i(TAG, "Querying between: " + monthBefore.getTime().toString() + "-" + monthAfter.getTime().toString());

        query.whereLessThan(Task.KEY_DUE_TIME, monthAfter.getTime());
        query.whereGreaterThan(Task.KEY_DUE_TIME, monthBefore.getTime());

        query.findInBackground(new FindCallback<Task>() {
            @Override
            public void done(List<Task> objects, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with getting taks", e);
                    return;
                }
                tasks.addAll(objects);
            }
        });
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

    public List<Task> getTasks(){
        return tasks;
    }

}
