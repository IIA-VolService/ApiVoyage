package volservice.iia.apivoyage.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
    private EditText editTextMessageErreur;

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
        DateFormat formatterDay = new SimpleDateFormat("dd-MM-yyyy");
        String requestGetVolRetour = "https://192.168.214.14:8890/api_vols/vol/getListVol.php?start=" + lieuArrivee + "&end=" + lieuDepart + "&date=" + formatterDay.format(dateRetour) + "&nbPassager=" + nbPassager + "&classVol=" + classe;

        URL url = new URL(requestGetVolRetour);
        HttpsURLConnection cnn = (HttpsURLConnection) url.openConnection();
        cnn.setRequestMethod("GET");

        JsonReader in = new JsonReader(new InputStreamReader(cnn.getInputStream()));
        JSONObject reader = new JSONObject(in.toString());
        try {
            parseFlyJson(reader, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lstRetour != null && lstRetour.length > 0;
    }

    private boolean askAller() throws IOException, JSONException {
        DateFormat formatterDay = new SimpleDateFormat("dd-MM-yyyy");
        String requestGetVolAller = "https://192.168.214.14:8890/api_vols/vol/getListVol.php?start=" + lieuDepart + "&end=" + lieuArrivee + "&date=" + formatterDay.format(dateAller) + "&nbPassager=" + nbPassager + "&classVol=" + classe;
        URL url = new URL(requestGetVolAller);
        HttpsURLConnection cnn = (HttpsURLConnection) url.openConnection();
        cnn.setRequestMethod("GET");

        JsonReader in = new JsonReader(new InputStreamReader(cnn.getInputStream()));
        JSONObject reader = new JSONObject(in.toString());
        try {
            parseFlyJson(reader, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lstAller != null && lstAller.length > 0;
    }

    private void parseFlyJson(JSONObject reader, boolean isAller) throws JSONException {
        FlightItem item;
        JSONObject objRes = reader.getJSONObject("result");
        String code = objRes.getString("code");

        // ERROR
        if (!code.equalsIgnoreCase("success"))
            return;

        int size = objRes.getInt("nombre");

        FlightItem[] listRes = new FlightItem[size];

        JSONObject obj = new JSONObject("data");
        JSONArray arr = obj.getJSONArray("vols");
        for (int i = 0; i < arr.length(); i++) {
            item = new FlightItem(arr.getJSONObject(i).getString("villeDepart"),
                    arr.getJSONObject(i).getString("villeArrivee"),
                    arr.getJSONObject(i).getString("codeAeroportDepart"),
                    arr.getJSONObject(i).getString("codeAeroportArrivee"),
                    arr.getJSONObject(i).getString("categorie"),
                    arr.getJSONObject(i).getInt("prix"),
                    arr.getJSONObject(i).getInt("placeDisponible"),
                    arr.getJSONObject(i).getString("dateDepart"),
                    arr.getJSONObject(i).getString("dateArrivee")
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
