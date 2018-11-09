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

import volservice.iia.apivoyage.R;

public class HostelFragment extends Fragment {

    private EditText editTextPays;
    private EditText editTextVille;
    private EditText editTextTypeHebergement;
    private EditText editTextDateDebutReservation;
    private EditText editTextDateFinReservation;
    private EditText editTextNbPersonnes;

    private Button btnSearch;
    private EditText editTextMessageErreur;

    private String pays;
    private String ville;
    private String typeHebergement;
    private String dateDebutReservation;
    private String dateFinReservation;
    private Integer nbPersonnes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hostel, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextPays = view.findViewById(R.id.editTextPaysHebergement);
        editTextVille = view.findViewById(R.id.editTextVilleHebergement);
        editTextTypeHebergement = view.findViewById(R.id.editTextTypeHebergement);
        editTextDateDebutReservation = view.findViewById(R.id.editTextDebutHebergement);
        editTextDateFinReservation = view.findViewById(R.id.editTextFinHebergement);
        editTextNbPersonnes = view.findViewById(R.id.editTextNbPersonnes);

        btnSearch = view.findViewById(R.id.hostel_btn_search);
        editTextMessageErreur = view.findViewById(R.id.MessageErreur);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pays = editTextPays.toString();
                ville = editTextVille.toString();
                typeHebergement = editTextTypeHebergement.toString();
                dateDebutReservation = editTextDateDebutReservation.toString();
                dateFinReservation = editTextDateFinReservation.toString();
                nbPersonnes = Integer.valueOf(editTextNbPersonnes.toString());

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
            if (!pays.isEmpty() && !ville.isEmpty() && !typeHebergement.isEmpty() && !dateDebutReservation.isEmpty() && !dateFinReservation.isEmpty() && nbPersonnes > 0) {
                return true;
            }
            editTextMessageErreur.setText(getText(R.string.txt_frg_flight_error_champs_invalides));
            return false;
    }
}
