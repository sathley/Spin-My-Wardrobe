package com.athley.spinmywardrobe;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class CalendarActivity extends Activity {

//    @InjectView(R.id.calendar_view)
//    CalendarView mCalendarView;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.inject(this);
        mContext = this;
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Toast.makeText(mContext, day + "/" + String.valueOf(month + 1) + "/" + year, Toast.LENGTH_SHORT).show();
                Intent dayIntent = new Intent(mContext, CalendarDayActivity.class);
                dayIntent.putExtra("date", String.valueOf(year) + String.valueOf(month+1) + String.valueOf(day) );
                startActivity(dayIntent);
            }
        }, year, month, day);
        dialog.show();
//        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
//                Toast.makeText(mContext, day + "/" + String.valueOf(month + 1) + "/" + year, Toast.LENGTH_SHORT).show();
//                Intent dayIntent = new Intent(mContext, CalendarDayActivity.class);
//                dayIntent.putExtra("date", String.valueOf(year) + String.valueOf(month+1) + String.valueOf(day) );
//                startActivity(dayIntent);
//            }
//        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        return true;}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
