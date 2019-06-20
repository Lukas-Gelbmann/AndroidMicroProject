package at.fhooe.mc.android.AndroidMicroProject;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    //storing important information
    public final static int Request_Code_Inside_Listview = 0;
    public static final String TAG = "qwer";
    public ArrayList<String> names = new ArrayList<String>();
    public ArrayList<String> dates = new ArrayList<String>();
    public String newDate;
    public String newName;
    int day;
    int month;
    int year;
    public int entries;
    public int sortingpref;
    CustomListAdapter customListAdapter;

    @Override //this happens when you start the app
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG,"MainActivity::onCreate: app started");

        //get entries out of shared preferences
        entries = getSharedPreferences("entries", 0).getInt("entries", 0);
        sortingpref = getSharedPreferences("entries", 0).getInt("sortingpref", 0);
        loadData();
        loadNotifications();

        //create toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //create custom listview --> new class
        customListAdapter = new CustomListAdapter(this, names, dates);

        //create list view
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(customListAdapter);

        //if you click on an item in the list --> new activity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Log.i(TAG,"MainActivity::onCreate: pressed on entry");
                Intent intent = new Intent(MainActivity.this, InsideListView.class);
                intent.putExtra("name", names.get(position));
                intent.putExtra("date", dates.get(position));
                startActivityForResult(intent, Request_Code_Inside_Listview);
            }
        });

        //button for adding birthdays
        FloatingActionButton fab = findViewById(R.id.fab);
        //opens first datepickerdialog
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"MainActivity::onCreate: fab pressed");
                showDatePickerDialog();
            }
        });
    }

    @Override //startactivityforresult-->
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case Request_Code_Inside_Listview: {
                if (resultCode == RESULT_OK) {
                    Log.i(TAG,"MainActivity::onActivityResult: deleted");
                    String deleteName = data.getStringExtra(ACTIVITY_SERVICE);
                    delete(deleteName);
                    }else{
                    Log.i(TAG,"MainActivity::onActivityResult: not deleted");
                }
            }break;
            default:
                Log.e(TAG, "MainActivity::onActivityResult: unexpected requestcode");
        }
    }

    //datepickerdialog
    private void showDatePickerDialog() {
        Log.i(TAG,"MainActivity::showDatePickerDialog: datepicker started");
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                //saves the date
                day = i2;
                month = ++i1;
                year = i;
                newDate = i2 + "." + i1 + "." + i;
                //if date is set --> dialog for name
                showNameDialog();
            }
        });
    }

    //namedialog
    private void showNameDialog() {
        Log.i(TAG,"MainActivity::showNameDialog: namedialog started");
        NameDialog nameDialog = new NameDialog();
        nameDialog.show(getSupportFragmentManager(), "NameDialog");
    }

    //loading data
    private void loadData() {
        Log.i(TAG,"MainActivity::loadData: loading data");
        names = loadArray("names");
        dates = loadArray("dates");
        sort();
    }

    //resets all notifications on start of program
    private void loadNotifications() {
        Log.i(TAG,"MainActivity::loadNotifications: loading notifications");
        for (int i = 1; i <= entries; i++) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent = new Intent(this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, i, alarmIntent, 0);
            SharedPreferences prefs = getSharedPreferences("entries", 0);
            String date = prefs.getString("dates_" + (i - 1), "x");
            if (!date.equals("x")) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.MONTH, (getMonth(date) - 1));
                calendar.set(Calendar.DAY_OF_MONTH, getDay(date));
                calendar.set(Calendar.HOUR_OF_DAY, 8);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                if (calendar.before(Calendar.getInstance())) {
                    calendar.add(Calendar.YEAR, 1);
                }
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
    }

    //returns the year of the date string
    public int getYear(String date) {
        int i = 0;
        while (date.charAt(i) != 46)
            i++;
        i++;
        while (date.charAt(i) != 46)
            i++;
        return Integer.parseInt(date.substring(++i));
    }

    //returns the month of the date string
    public int getMonth(String date) {
        int i = 0;
        while (date.charAt(i) != 46)
            i++;
        int j = ++i;
        while (date.charAt(i) != 46)
            i++;
        return Integer.parseInt(date.substring(j, i));
    }

    //returns the day of the date string
    public int getDay(String date) {
        int i = 0;
        while (date.charAt(i) != 46)
            i++;
        return Integer.parseInt(date.substring(0, i));
    }

    //method to get arrays out of shared preferences
    public ArrayList<String> loadArray(String arrayName) {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("entries", MODE_PRIVATE);
        ArrayList<String> list = new ArrayList();
        int count = 0;
        for (int i = 0; i < entries; i++) {
            String newString = prefs.getString(arrayName + "_" + i, null);
            if (newString != null) {
                list.add(count, newString);
                count++;
            }
        }
        return list;
    }

    //if name and date are set:
    public void add(String name) {
        newName = name;
        names.add(newName);
        dates.add(newDate);
        addToSharedPreferences();
        sort();
        customListAdapter.notifyDataSetChanged();
        loadNotifications();
        Log.i(TAG, "MainActivity::add: entry added");
    }

    //the process of adding new stuff to the shared preferences
    private void addToSharedPreferences() {
        entries++;
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("entries", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("dates_" + (entries - 1), newDate);
        editor.putString("names_" + (entries - 1), newName);
        editor.putInt("entries", entries);
        editor.commit();
    }

    //deleting
    private void delete(String deleteName) {
        int x = names.indexOf(deleteName);
        deleteInSharedPreferences(deleteName);
        names.remove(x);
        dates.remove(x);
        deleteNotification(x);
        customListAdapter.notifyDataSetChanged();
        Log.i(TAG, "MainActivity::delete: entry deleted");
    }

    //deleting the notification for the entry
    private void deleteNotification(int x) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, x, alarmIntent, 0);
        alarmManager.cancel(pendingIntent);
    }

    //deleting the entry in shared preferences
    public void deleteInSharedPreferences(String deleteName) {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("entries", 0);
        for (int i = 0; i < entries; i++) {
            String newString = prefs.getString("names_" + i, null);
            if (newString != null) {
                if (newString.equals(deleteName)) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.remove("dates_" + i);
                    editor.remove("names_" + i);
                    editor.commit();
                    Log.i("xdd", "entry deleted");
                    return;
                }
            }
        }
    }

    //decide between different algorithms
    public void sort() {
        if (sortingpref == 0)
            sortByName();
        if (sortingpref == 1)
            sortByDate();
        if (sortingpref == 2)
            sortByBirthYear();
    }

    //sorts the entries by name
    public void sortByName() {
        for (int j = 0; j < names.size(); j++) {
            for (int i = 0; i < names.size() - 1; i++) {
                if (names.get(i).compareTo(names.get(i + 1)) > 0) {
                    String n = names.get(i);
                    String d = dates.get(i);
                    names.set(i, names.get(i + 1));
                    dates.set(i, dates.get(i + 1));
                    names.set(i + 1, n);
                    dates.set(i + 1, d);
                }
            }
        }
    }

    //sorts the entries by date
    public void sortByDate() {
        ArrayList<Integer> list = new ArrayList();
        for (int i = 0; i < dates.size(); i++) {
            list.add(i, getMonth(dates.get(i)) * 100 + getDay(dates.get(i)));
        }
        for (int j = 0; j < names.size(); j++) {
            for (int i = 0; i < names.size() - 1; i++) {
                if (list.get(i) > list.get(i + 1)) {
                    int l = list.get(i);
                    String n = names.get(i);
                    String d = dates.get(i);
                    list.set(i, list.get(i + 1));
                    names.set(i, names.get(i + 1));
                    dates.set(i, dates.get(i + 1));
                    list.set(i + 1, l);
                    names.set(i + 1, n);
                    dates.set(i + 1, d);
                }
            }
        }
        DateFormat df = new SimpleDateFormat("MMdd");
        String date = df.format(Calendar.getInstance().getTime());
        int today = Integer.parseInt(date)-1;
        int stop = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) > today) {
                stop = i;
                break;
            }
        }
        for (int i = 0; i< stop; i++){
            names.add(names.size(), names.get(0));
            names.remove(0);
            dates.add(dates.size(), dates.get(0));
            dates.remove(0);
        }
    }

    //sorts the entries by birthyear
    public void sortByBirthYear() {
        ArrayList<Integer> list = new ArrayList();
        for (int i = 0; i < dates.size(); i++) {
            list.add(i, getYear(dates.get(i)));
        }
        for (int j = 0; j < names.size(); j++) {
            for (int i = 0; i < names.size() - 1; i++) {
                if (list.get(i) > list.get(i + 1)) {
                    int l = list.get(i);
                    String n = names.get(i);
                    String d = dates.get(i);
                    list.set(i, list.get(i + 1));
                    names.set(i, names.get(i + 1));
                    dates.set(i, dates.get(i + 1));
                    list.set(i + 1, l);
                    names.set(i + 1, n);
                    dates.set(i + 1, d);
                }
            }
        }
    }

    //reverses the list
    public ArrayList<String> reverseList(ArrayList list) {
        Collections.reverse(list);
        return list;
    }

    @Override //nothing happens
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        //nothing happens
    }

    @Override //inflates the options menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override //sets the new sorting preference and sorts it
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.reverse:{
                Log.i(TAG, "MainActivity::onOptionsItemSelected: entries reversed");
                Toast.makeText(this, R.string.listturned, Toast.LENGTH_SHORT).show();
                names = reverseList(names);
                dates = reverseList(dates);
                customListAdapter.notifyDataSetChanged();
            }break;
            case R.id.sortName: {
                Log.i(TAG, "MainActivity::onOptionsItemSelected: sorted by name");
                sortingpref = 0;
                sort();
                customListAdapter.notifyDataSetChanged();
            }
            break;
            case R.id.sortDate: {
                Log.i(TAG, "MainActivity::onOptionsItemSelected: sorted by date");
                sortingpref = 1;
                sort();
                customListAdapter.notifyDataSetChanged();
            }
            break;
            case R.id.sortBirthyear: {
                Log.i(TAG, "MainActivity::onOptionsItemSelected: sorted by birthyear");
                sortingpref = 2;
                sort();
                customListAdapter.notifyDataSetChanged();
            }
            break;
            default: {
                Log.e(TAG, "MainActivity::onOptionsItemSelected: unexpected menu id");
            }
            break;
        }
        SharedPreferences prefs = getSharedPreferences("entries", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("sortingpref", sortingpref);
        editor.commit();
        return super.onOptionsItemSelected(item);
    }
}
