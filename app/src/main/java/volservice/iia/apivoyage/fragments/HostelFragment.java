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

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import volservice.iia.apivoyage.R;
import volservice.iia.apivoyage.fragments.resultsList.HostelResultFragment;
import volservice.iia.apivoyage.items.HostelItem;

public class HostelFragment extends Fragment {

    private EditText editTextPaysHebergement;
    private EditText editTextVilleHebergement;
    private EditText editTextDateDebutReservationHebergement;
    private EditText editTextDateFinReservationHebergement;
    private EditText editTextNbPersonnesHebergement;

    private Button btnSearch;
    private TextView editTextMessageErreur;

    private String paysHebergement;
    private String villeHebergement;
    private String dateDebutReservationHebergement;
    private String dateFinReservationHebergement;
    private Integer nbPersonnes;

    private HostelItem[] lstHostel;


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
                dateDebutReservationHebergement = editTextDateDebutReservationHebergement.toString();
                dateFinReservationHebergement = editTextDateFinReservationHebergement.toString();
                nbPersonnes = !editTextNbPersonnesHebergement.getText().toString().trim().isEmpty() ? (Integer.valueOf(editTextNbPersonnesHebergement.getText().toString())) : -1;

                if (checkIfFormIsCorrect()) {
                    editTextMessageErreur.setVisibility(View.INVISIBLE);
                    try {
                        try {
                            if (startRequest()) {
                                Bundle args = new Bundle();
                                args.putSerializable(HostelResultFragment.ITEMS, lstHostel);

                                Fragment fragment = new HostelResultFragment();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                fragment.setArguments(args);

                                ft.replace(R.id.screenArea, fragment);
                                ft.commit();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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

    //requete https Hebergement

    public boolean startRequest() throws IOException, JSONException {

        if (askHostels()) {
            return true;
        }
        Toast.makeText(this.getContext(), "Impossible de trouver des logements correspondants", Toast.LENGTH_SHORT).show();
        return false;
    }

    private boolean askHostels() throws IOException, JSONException {
        String requestGetListVilleHebergement = "https://192.168.214.11:5001/api/hebergement/findby/city/" + villeHebergement + "?token=7woAE69CqstvfvdeFLW8KA==";
        URL url = new URL(requestGetListVilleHebergement);
        HttpsURLConnection cnn = (HttpsURLConnection) url.openConnection();
        cnn.setRequestMethod("GET");

        InputStreamReader in = new InputStreamReader(cnn.getInputStream());

        BufferedReader br = new BufferedReader(in);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();

        JSONArray jsonObject = new JSONArray(sb.toString());

        try {
            parseFlyJson(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lstHostel != null && lstHostel.length > 0;
    }

    private void parseFlyJson(JSONArray reader) throws JSONException {
        HostelItem item;

        int size = reader.length();

        HostelItem[] listRes = new HostelItem[size];

//        JSONObject obj = new JSONObject("result");
        for (int i = 0; i < reader.length(); i++) {
            item = new HostelItem(
                    reader.getJSONObject(i).getString("id"),
                    reader.getJSONObject(i).getString("nom"),
                    reader.getJSONObject(i).getInt("etoile"),
                    reader.getJSONObject(i).getDouble("prix"),
                    reader.getJSONObject(i).getString("dateDebutBooked"),
                    reader.getJSONObject(i).getString("dateFinBooked"),
                    reader.getJSONObject(i).getString("adresse"),
                    reader.getJSONObject(i).getString("ville"),
                    reader.getJSONObject(i).getString("pays")
            );
            listRes[i] = item;
        }
        lstHostel = listRes;
    }


    private boolean checkIfFormIsCorrect() {
        if (!paysHebergement.isEmpty() && !villeHebergement.isEmpty() && !dateDebutReservationHebergement.isEmpty() && !dateFinReservationHebergement.isEmpty() && nbPersonnes > 0) {
            return true;
        }
        editTextMessageErreur.setText(getText(R.string.txt_frg_flight_error_champs_invalides));
        return false;
    }
}
