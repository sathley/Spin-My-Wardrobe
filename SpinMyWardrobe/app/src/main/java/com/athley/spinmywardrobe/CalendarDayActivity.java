package com.athley.spinmywardrobe;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.ListView;

import com.athley.spinmywardrobe.adapters.CalendarDayItemsAdapter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class CalendarDayActivity extends Activity {


    @InjectView(R.id.day_listview)
    ListView mListView;

    private DateTime mDate = new DateTime();

    private CalendarDayItemsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_day);
        ButterKnife.inject(this);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");
        mDate = formatter.parseDateTime(getIntent().getStringExtra("date"));
        final DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MM-yyyy");
        getActionBar().setTitle(fmt.print(mDate));
        mAdapter = new CalendarDayItemsAdapter(this, mDate);
        mListView.setAdapter(mAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calendar_day, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
