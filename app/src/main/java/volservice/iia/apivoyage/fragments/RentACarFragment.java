package volservice.iia.apivoyage.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import volservice.iia.apivoyage.R;
import volservice.iia.apivoyage.fragments.resultsList.CarResultFragment;
import volservice.iia.apivoyage.fragments.resultsList.HostelResultFragment;
import volservice.iia.apivoyage.items.CarItem;

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

    private CarItem[] lstAPI01;
    private CarItem[] lstAPI02;


    //requetes https Voiture
    public boolean startRequest() throws IOException {

        if (askCarsAPI01()) {
            if (askCarsAPI02()) {
                return true;

            } else {
                Toast.makeText(this.getContext(), "Impossible de trouver les voitures correspondantes", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        Toast.makeText(this.getContext(), "Impossible de trouver des voitures correspondantes", Toast.LENGTH_SHORT).show();
        return false;

    }

    private boolean askCarsAPI01() {
        return false;
    }

    private boolean askCarsAPI02() {
        return false;
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
                    try {
                        if (startRequest()) {
                            Bundle args = new Bundle();
                            args.putSerializable(CarResultFragment.ITEMS_API01, lstAPI01);
                            args.putSerializable(CarResultFragment.ITEMS_API02, lstAPI02);
                            Fragment fragment = new HostelResultFragment();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction ft = fragmentManager.beginTransaction();
                            fragment.setArguments(args);

                            ft.replace(R.id.screenArea, fragment);
                            ft.commit();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


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
