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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import volservice.iia.apivoyage.R;
import volservice.iia.apivoyage.fragments.resultsList.FlightResultFragment;
import volservice.iia.apivoyage.items.FlightItem;

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
    private TextView editTextMessageErreur;

    private String lieuArrivee;
    private String lieuDepart;
    private String dateAller;
    private String dateRetour;
    private Integer nbPassager;
    private String classe;

    private Boolean eco;
    private Boolean premium;
    private Boolean business;

    private FlightItem[] lstAller;
    private FlightItem[] lstRetour;

    //requete https Vol
    public boolean startRequest() throws IOException, JSONException {
        if (checkBoxClasseBusiness.isChecked()) {
            classe = "business";
        } else if (checkBoxClassePremium.isChecked()) {
            classe = "premium";
        } else
            classe = "economique";

        if (askAller()) {
            if (askRetour()) {
                return true;
            } else {
                Toast.makeText(this.getContext(), "Impossible de trouver des vols correspondants", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(this.getContext(), "Impossible de trouver des vols correspondants", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    private boolean askRetour() throws IOException, JSONException {
        String requestGetVolRetour = "https://192.168.214.14:8890/api_vols/vol/getListVol.php?start=" + lieuArrivee + "&end=" + lieuDepart + "&date=" + dateRetour + "&nbPassager=" + nbPassager + "&classVol=" + classe + "&token=&token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxfQ.FgKm8JNIbbmf-KLFZbQI9NGo_3S3NoIjprvPLISlh0c";

        URL url = new URL(requestGetVolRetour);
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

        JSONObject jsonObject = new JSONObject(sb.toString());

        try {
            parseFlyJson(jsonObject, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lstRetour != null && lstRetour.length > 0;
    }

    private boolean askAller() throws IOException, JSONException {
        String requestGetVolAller = "https://192.168.214.14:8890/api_vols/vol/getListVol.php?start=" + lieuDepart + "&end=" + lieuArrivee + "&date=" + dateAller + "&nbPassager=" + nbPassager + "&classVol=" + classe + "&token=&token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxfQ.FgKm8JNIbbmf-KLFZbQI9NGo_3S3NoIjprvPLISlh0c";
        URL url = new URL(requestGetVolAller);
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

        JSONObject jsonObject = new JSONObject(sb.toString());

        try {
            parseFlyJson(jsonObject, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lstAller != null && lstAller.length > 0;
    }

    private void parseFlyJson(JSONObject reader, boolean isAller) throws JSONException, IOException {
        FlightItem item;

        String code = reader.getString("message");

        // ERROR
        if (!code.equalsIgnoreCase("success"))
            return;

        int size = reader.getInt("nombre");

        FlightItem[] listRes = new FlightItem[size];

//        JSONObject obj = new JSONObject("result");
        JSONArray arr = reader.getJSONArray("result");
        for (int i = 0; i < arr.length(); i++) {
            JSONObject resa = arr.getJSONObject(i).getJSONObject("reservations");
            item = new FlightItem(
                    arr.getJSONObject(i).getInt("id"),
                    arr.getJSONObject(i).getString("villeDepart"),
                    arr.getJSONObject(i).getString("villeArrivee"),
                    arr.getJSONObject(i).getString("codeAeroportDepart"),
                    arr.getJSONObject(i).getString("codeAeroportArrivee"),
                    resa.getString("nom"),
                    resa.getInt("prix"),
                    resa.getInt("placeDisponible"),
                    arr.getJSONObject(i).getString("depart"),
                    arr.getJSONObject(i).getString("arrivee")
            );
            listRes[i] = item;
        }
        if (isAller)
            lstAller = listRes;
        else lstRetour = listRes;
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

        lstRetour = null;
        lstAller = null;

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
                lieuDepart = editTextDepart.getText().toString();
                lieuArrivee = editTextArrivee.getText().toString();
                dateAller = editTextDateAller.getText().toString();
                dateRetour = editTextDateRetour.getText().toString();
                nbPassager = !editTextNbPassager.getText().toString().trim().isEmpty() ? (Integer.valueOf(editTextNbPassager.getText().toString())) : -1;

                eco = checkBoxClasseEco.isChecked();
                premium = checkBoxClassePremium.isChecked();
                business = checkBoxClasseBusiness.isChecked();
                if (checkIfFormIsCorrect()) {
                    editTextMessageErreur.setVisibility(View.INVISIBLE);
                    try {
                        if (startRequest()) {
                            Bundle args = new Bundle();
                            args.putSerializable(FlightResultFragment.ITEMS_ALLER, lstAller);
                            args.putSerializable(FlightResultFragment.ITEMS_RETOUR, lstRetour);
                            args.putBoolean(FlightResultFragment.SELECTION_STATE, true);

                            Fragment fragment = new FlightResultFragment();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction ft = fragmentManager.beginTransaction();
                            fragment.setArguments(args);

                            ft.replace(R.id.screenArea, fragment);
                            ft.commit();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
