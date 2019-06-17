package at.fhooe.mc.android.AndroidMicroProject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter {

    //to reference the Activity
    private final Activity context;

    //to store the list of names
    private final ArrayList<String> nameArray;

    //to store the list of dates
    private final ArrayList<String> infoArray;

    public CustomListAdapter(Activity context, ArrayList<String> nameArrayParam, ArrayList<String> infoArrayParam) {
        super(context, R.layout.listview_row, nameArrayParam);
        this.context = context;
        this.nameArray = nameArrayParam;
        this.infoArray = infoArrayParam;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_row, null, true);

        //this code gets references to objects in the listview_row.xml file
        TextView nameTextField = (TextView) rowView.findViewById(R.id.nameTextViewID);
        TextView infoTextField = (TextView) rowView.findViewById(R.id.infoTextViewID);

        //this code sets the values of the objects to values from the arrays
        nameTextField.setText(nameArray.get(position));
        infoTextField.setText(infoArray.get(position));
        return rowView;
    }
}
