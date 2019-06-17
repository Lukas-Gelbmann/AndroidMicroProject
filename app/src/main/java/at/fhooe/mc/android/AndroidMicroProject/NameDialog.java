package at.fhooe.mc.android.AndroidMicroProject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class NameDialog extends DialogFragment {
    private EditText input;
    private TextView ok, cancel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_name, container, false);
        // the buttons / input of the dialog
        cancel = view.findViewById(R.id.action_cancel);
        ok = view.findViewById(R.id.action_ok);
        input = view.findViewById(R.id.input);

        //if you click on cancel --> return to main activity
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
        //if you click on ok --> call method newinput() in main activity if input != null
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!input.getText().toString().equals(""))
                    ((MainActivity) getActivity()).add(input.getText().toString());
                getDialog().dismiss();
            }
        });
        return view;
    }
}
