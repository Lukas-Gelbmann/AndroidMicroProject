package at.fhooe.mc.android.AndroidMicroProject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class InsideListView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inside_listview);

        // display the name
        String savedExtra = getIntent().getStringExtra("name");
        TextView myText = findViewById(R.id.inside_listview_name);
        myText.setText(savedExtra);

        //display the date
        savedExtra = getIntent().getStringExtra("date");
        myText = findViewById(R.id.inside_listview_date);
        myText.setText(savedExtra);

        //calculate the difference
        int years = Calendar.getInstance().get(Calendar.YEAR) - getYear(savedExtra);
        long diff = 0;
        try {
            diff = getDifference(savedExtra);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (diff < 0) {
            diff = 365 + diff;
            years++;
        }

        //display the difference
        myText = findViewById(R.id.inside_listview_diff);
        myText.setText("Gets " + years + " in " + diff + " days.");

        //the delete button
        Button button = findViewById(R.id.delete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteDialogFragment deleteDialogFragment = newInstance(getIntent().getStringExtra("name"));
                deleteDialogFragment.show(getSupportFragmentManager(), "delete");
            }
        });

        //the return button
        button = findViewById(R.id.returning);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnWithoutDelete();
            }
        });
    }

    public static DeleteDialogFragment newInstance(String name) {
        DeleteDialogFragment f = new DeleteDialogFragment();
        // Supply name input as an argument.
        Bundle args = new Bundle();
        args.putString("name", name);
        f.setArguments(args);
        return f;
    }

    public void itemGetsDeleted(String deleteName) {
        Intent i = new Intent();
        i.putExtra(MainActivity.ACTIVITY_SERVICE, deleteName);
        setResult(RESULT_OK, i);
        finish();
    }

    public void returnWithoutDelete() {
        Intent i = new Intent();
        setResult(RESULT_CANCELED, i);
        finish();
    }

    long getDifference(String _date) throws ParseException {
        SimpleDateFormat myFormat = new SimpleDateFormat("MMdd");
        int date = (getMonth(_date)) * 100 + getDay(_date);
        DateFormat df = new SimpleDateFormat("MMdd");
        String inputString1 = "";
        if (getMonth(_date) <= 9)
            inputString1 = 0 + String.valueOf(date);
        else
            inputString1 = String.valueOf(date);
        String inputString2 = df.format(Calendar.getInstance().getTime());
        Date date1 = myFormat.parse(inputString1);
        Date date2 = myFormat.parse(inputString2);
        long diff = date1.getTime() - date2.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public int getYear(String date) {
        int i = 0;
        while (date.charAt(i) != 46)
            i++;
        i++;
        while (date.charAt(i) != 46)
            i++;
        return Integer.parseInt(date.substring(++i));
    }

    public int getMonth(String date) {
        int i = 0;
        while (date.charAt(i) != 46)
            i++;
        int j = ++i;
        while (date.charAt(i) != 46)
            i++;
        return Integer.parseInt(date.substring(j, i));

    }

    public int getDay(String date) {
        int i = 0;
        while (date.charAt(i) != 46)
            i++;
        return Integer.parseInt(date.substring(0, i));
    }


}
