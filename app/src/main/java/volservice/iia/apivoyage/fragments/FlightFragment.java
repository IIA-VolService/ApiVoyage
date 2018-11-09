package volservice.iia.apivoyage.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import volservice.iia.apivoyage.R;

public class FlightFragment extends Fragment {

    private EditText editTextArrivee;
    private EditText editTextDepart;
    private EditText editTextDateAller;
    private EditText editTextDateRetour;
    private EditText editTextNbPassager;

    private CheckBox checkBoxClasseEco;
    private CheckBox checkBoxClasseBusiness;
    private CheckBox checkBoxClassePremium;

    private Button btnSearch;
    private EditText editTextMessageErreur;

    private String lieuArrivee;
    private String lieuDepart;
    private String dateAller;
    private String dateRetour;
    private Integer nbPassager;

    private Boolean eco;
    private Boolean premium;
    private Boolean business;


    //requete https Vol

    public static void main(String[] args) throws IOException {

        String urlLink = "https://localhost.dev:8890/api_vols/vol/getListVol.php?start=paris&end=new york&date=\"24-11-2018&nbPassager=3&classVol=economique";
        URL url = new URL(urlLink);
        HttpsURLConnection cnn = (HttpsURLConnection) url.openConnection();
        cnn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(cnn.getInputStream()));

        StringBuffer sb = new StringBuffer();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flight, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextArrivee = view.findViewById(R.id.editTextArrivee);
        editTextDepart = view.findViewById(R.id.editTextDepart);
        editTextDateAller = view.findViewById(R.id.editTextAller);
        editTextDateRetour = view.findViewById(R.id.editTextRetour);
        editTextNbPassager = view.findViewById(R.id.editTextPassager);

        checkBoxClasseEco = view.findViewById(R.id.flight_checkbox_Eco);
        checkBoxClassePremium = view.findViewById(R.id.flight_checkbox_Premium);
        checkBoxClasseBusiness = view.findViewById(R.id.flight_checkbox_Business);

        btnSearch = view.findViewById(R.id.flight_btn_search);
        editTextMessageErreur = view.findViewById(R.id.MessageErreur);

        checkBoxClasseEco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxClasseBusiness.setChecked(false);
                checkBoxClassePremium.setChecked(false);
            }
        });

        checkBoxClassePremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxClasseBusiness.setChecked(false);
                checkBoxClasseEco.setChecked(false);
            }
        });

        checkBoxClasseBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxClasseEco.setChecked(false);
                checkBoxClassePremium.setChecked(false);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lieuDepart = editTextDepart.toString();
                lieuArrivee = editTextArrivee.toString();
                dateAller = editTextDateAller.toString();
                dateRetour = editTextDateRetour.toString();
                nbPassager = Integer.valueOf(editTextNbPassager.toString());

                eco = checkBoxClasseEco.isChecked();
                premium = checkBoxClassePremium.isChecked();
                business = checkBoxClasseBusiness.isChecked();
                if (checkIfFormIsCorrect()) {
                    editTextMessageErreur.setVisibility(View.INVISIBLE);
                    // do my request


                } else {
                    editTextMessageErreur.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private boolean checkIfFormIsCorrect() {
        if (eco || premium || business) {
            if (!lieuArrivee.isEmpty() && !lieuDepart.isEmpty() && !dateAller.isEmpty() && !dateRetour.isEmpty() && nbPassager > 0) {
                return true;
            }
            editTextMessageErreur.setText(getText(R.string.txt_frg_flight_error_champs_invalides));
            return false;
        }
        editTextMessageErreur.setText(getText(R.string.txt_frg_flight_error_checkbox));
        return false;

    }
}
