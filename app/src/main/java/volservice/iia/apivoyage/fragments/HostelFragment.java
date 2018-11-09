package volservice.iia.apivoyage.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import volservice.iia.apivoyage.R;

public class HostelFragment extends Fragment {

    private EditText editTextPaysHebergement;
    private EditText editTextVilleHebergement;
    private EditText editTextTypeHebergement;
    private EditText editTextDateDebutReservationHebergement;
    private EditText editTextDateFinReservationHebergement;
    private EditText editTextNbPersonnesHebergement;

    private Button btnSearch;
    private TextView editTextMessageErreur;

    private String paysHebergement;
    private String villeHebergement;
    private String typeHebergement;
    private String dateDebutReservationHebergement;
    private String dateFinReservationHebergement;
    private Integer nbPersonnes;


    //requete https Hebergement

    public void startRequest() throws IOException {

        String urlLink = "";
        URL url = new URL(urlLink);
        HttpsURLConnection cnn = (HttpsURLConnection) url.openConnection();
        cnn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(cnn.getInputStream()));

        StringBuffer sb = new StringBuffer();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hostel, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextPaysHebergement = view.findViewById(R.id.editTextPaysHebergement);
        editTextVilleHebergement = view.findViewById(R.id.editTextVilleHebergement);
        editTextTypeHebergement = view.findViewById(R.id.editTextTypeHebergement);
        editTextDateDebutReservationHebergement = view.findViewById(R.id.editTextDebutHebergement);
        editTextDateFinReservationHebergement = view.findViewById(R.id.editTextFinHebergement);
        editTextNbPersonnesHebergement = view.findViewById(R.id.editTextNbPersonnesHebergement);

        btnSearch = view.findViewById(R.id.hostel_btn_search);
        editTextMessageErreur = view.findViewById(R.id.MessageErreur);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paysHebergement = editTextPaysHebergement.toString();
                villeHebergement = editTextVilleHebergement.toString();
                typeHebergement = editTextTypeHebergement.toString();
                dateDebutReservationHebergement = editTextDateDebutReservationHebergement.toString();
                dateFinReservationHebergement = editTextDateFinReservationHebergement.toString();
                nbPersonnes = Integer.valueOf(editTextNbPersonnesHebergement.toString());

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
        if (!paysHebergement.isEmpty() && !villeHebergement.isEmpty() && !typeHebergement.isEmpty() && !dateDebutReservationHebergement.isEmpty() && !dateFinReservationHebergement.isEmpty() && nbPersonnes > 0) {
            return true;
        }
        editTextMessageErreur.setText(getText(R.string.txt_frg_flight_error_champs_invalides));
        return false;
    }
}
