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

public class RentACarFragment extends Fragment {

    private EditText editTextVilleLocation;
    private EditText editTextAgenceLocation;
    private EditText editTextDateDebutReservationLocation;
    private EditText editTextDateFinReservationLocation;
    private EditText editTextTypeVoiture;

    private Button btnSearch;
    private TextView editTextMessageErreur;

    private String villeLocation;
    private String agenceLocation;
    private String typeVoiture;
    private String dateDebutReservationLocation;
    private String dateFinReservationLocation;


    //requete https Voiture

    public void startRequest() throws IOException {

        String getListVoiture = "https://192.168.214.23:5001/api/VoitureReservation/localisation/" + villeLocation;
        URL url = new URL(getListVoiture);
        HttpsURLConnection cnn = (HttpsURLConnection) url.openConnection();
        cnn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(cnn.getInputStream()));

        StringBuffer sb = new StringBuffer();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rent_a_car, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextVilleLocation = view.findViewById(R.id.editTextVilleVoiture);
        editTextDateDebutReservationLocation = view.findViewById(R.id.editTextDebutReservationVoiture);
        editTextDateFinReservationLocation = view.findViewById(R.id.editTextFinReservationVoiture);
        editTextTypeVoiture = view.findViewById(R.id.editTextTypeVoiture);

        btnSearch = view.findViewById(R.id.car_btn_search);
        editTextMessageErreur = view.findViewById(R.id.MessageErreur);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                villeLocation = editTextVilleLocation.toString();
                agenceLocation = editTextAgenceLocation.toString();
                typeVoiture = editTextTypeVoiture.toString();
                dateDebutReservationLocation = editTextDateDebutReservationLocation.toString();
                dateFinReservationLocation = editTextDateFinReservationLocation.toString();

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
        if (!villeLocation.isEmpty() && !agenceLocation.isEmpty() && !typeVoiture.isEmpty() && !dateDebutReservationLocation.isEmpty() && !dateFinReservationLocation.isEmpty()) {
            return true;
        }
        editTextMessageErreur.setText(getText(R.string.txt_frg_flight_error_champs_invalides));
        return false;
    }
}
