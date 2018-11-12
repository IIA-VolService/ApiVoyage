package volservice.iia.apivoyage.fragments.custom;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import volservice.iia.apivoyage.R;

public class MyUserFragment extends DialogFragment {

    private EditText nom;
    private EditText premon;

    private Button btnBack;
    private Button btnValid;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.popup_username, null);

        // Get your views by using view.findViewById() here and do your listeners.
        nom = view.findViewById(R.id.password);
        premon = view.findViewById(R.id.username);

        btnBack = view.findViewById(R.id.custom_btn_back);
        btnValid = view.findViewById(R.id.custom_btn_valid);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Set the dialog layout
        builder.setView(view);

        return builder.create();
    }


}
