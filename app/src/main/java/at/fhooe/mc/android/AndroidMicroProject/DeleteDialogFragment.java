package at.fhooe.mc.android.AndroidMicroProject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

public class DeleteDialogFragment extends DialogFragment {
    public String deleteName;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //get the name
        deleteName = getArguments().getString("name");
        //create dialog
        AlertDialog.Builder bob = new AlertDialog.Builder(getContext());
        bob.setMessage(R.string.deleteFragment_text);
        //if okay: make toast, item gets deleted in main activity
        bob.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), R.string.deleteFragment_delete, Toast.LENGTH_SHORT).show();
               //problem
                ((InsideListView)getActivity()).itemGetsDeleted(deleteName);
            }
        });
        //if cancel: nothing happens
        bob.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        Dialog d = bob.create();
        return d;
    }
}
