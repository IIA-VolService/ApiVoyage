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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import volservice.iia.apivoyage.R;
import volservice.iia.apivoyage.fragments.resultsList.CarResultFragment;
import volservice.iia.apivoyage.fragments.resultsList.HostelResultFragment;
import volservice.iia.apivoyage.items.CarItem;
import volservice.iia.apivoyage.items.EnumAPI;

public class RentACarFragment extends Fragment {

    private EditText editTextVilleLocation;
    private EditText editTextDateDebutReservationLocation;
    private EditText editTextDateFinReservationLocation;
    private EditText editTextTypeVoiture;

    private Button btnSearch;
    private TextView editTextMessageErreur;

    private String villeLocation;
    private String marqueVoiture;
    private String dateDebutReservationLocation;
    private String dateFinReservationLocation;

    private CarItem[] lstAPI01;
    private CarItem[] lstAPI02;

    //requetes https Voiture
    public boolean startRequest() throws IOException, JSONException {
        lstAPI01 = null;
        lstAPI02 = null;
        if (askCarsAPI01() && askCarsAPI02()) {
            if (lstAPI01.length > 0 || lstAPI02.length > 0) {
                Bundle args = new Bundle();
                args.putSerializable(CarResultFragment.ITEMS_API01, lstAPI01);
                args.putSerializable(CarResultFragment.ITEMS_API02, lstAPI02);

                Fragment fragment = new CarResultFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                fragment.setArguments(args);

                ft.replace(R.id.screenArea, fragment);
                ft.commit();
            } else {
                Toast.makeText(this.getContext(), "Impossible de trouver les voitures correspondantes", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        Toast.makeText(this.getContext(), "Impossible de trouver des voitures correspondantes", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * Tanguy et Mael
     *
     * @return bool
     */
    private boolean askCarsAPI01() throws IOException, JSONException {
        String getListVoiture = "https://192.168.214.23:5001/api/VoitureReservation/marque/" + marqueVoiture;

        URL url = new URL(getListVoiture);
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

        JSONArray jsonArray = new JSONArray(sb.toString());

        try {
            parseFlyJsonAPI01(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lstAPI01 != null;
    }

    private void parseFlyJsonAPI01(JSONArray reader) throws JSONException {
        CarItem item;

        int size = reader.length();

        CarItem[] listRes = new CarItem[size];

        for (int i = 0; i < size; i++) {
            item = new CarItem(
                    EnumAPI.API01,
                    reader.getJSONObject(i).getString("modele"),
                    reader.getJSONObject(i).getString("prixReservation"),
                    dateDebutReservationLocation,
                    dateFinReservationLocation,
                    Integer.valueOf(reader.getJSONObject(i).getString("nbPassager")),
                    reader.getJSONObject(i).getString("localisation"),
                    reader.getJSONObject(i).getString("marque")
            );
            listRes[i] = item;
        }
        lstAPI01 = listRes;
    }


    /**
     * Camille et Robin
     *
     * @return bool
     */
    private boolean askCarsAPI02() throws JSONException, IOException {
        ArrayList<Integer> nbMaxIdCar;
        JSONArray jsonArray = new JSONArray(getSbForRequest("https://192.168.214.31/car/read.php?token=SZAdlqfwCdb18CzUads4tLTqN6EaLRlr").toString());
        try {
            nbMaxIdCar = askForAllAvailableCars(jsonArray);
            if (!nbMaxIdCar.isEmpty()) {
                parseFlyJsonAPI02(nbMaxIdCar);
            } else {
                lstAPI02 = new CarItem[0];
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lstAPI02 != null;
    }

    private void parseFlyJsonAPI02(ArrayList<Integer> idCar) throws JSONException, IOException {
        CarItem item;
        JSONObject jsonObject;
        ArrayList<CarItem> listRes = new ArrayList<CarItem>();
        for (Integer id : idCar) {
            jsonObject = new JSONObject(getSbForRequest("https://192.168.214.31/car/read_one_formatted.php?token=SZAdlqfwCdb18CzUads4tLTqN6EaLRlr&id=" + id).toString());
            if (jsonObject.getString("brandname").compareToIgnoreCase(marqueVoiture) == 0) {
                item = new CarItem(
                        EnumAPI.API02,
                        jsonObject.getString("brandname"),
                        jsonObject.getString("price"),
                        dateDebutReservationLocation,
                        dateFinReservationLocation,
                        5,
                        jsonObject.getString("agency"),
                        jsonObject.getString("model"));
                listRes.add(item);
            }
        }
        if (!listRes.isEmpty()) {
            CarItem[] items = new CarItem[listRes.size()];
            int i = 0;
            for (CarItem carItem : listRes) {
                items[i++] = carItem;
            }
            lstAPI02 = items;
        } else lstAPI02 = new CarItem[0];
    }

    private StringBuilder getSbForRequest(String s) throws IOException {
        URL url = new URL(s);
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
        return sb;
    }

    private ArrayList<Integer> askForAllAvailableCars(JSONArray reader) throws JSONException {
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < reader.length(); i++) {
            result.add(reader.getJSONObject(i).getInt("id"));
        }
        return result;
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
        editTextTypeVoiture = view.findViewById(R.id.editTextMArqueVoiture);

        btnSearch = view.findViewById(R.id.car_btn_search);
        editTextMessageErreur = view.findViewById(R.id.MessageErreur);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                villeLocation = editTextVilleLocation.getText().toString();
                marqueVoiture = editTextTypeVoiture.getText().toString();
                dateDebutReservationLocation = editTextDateDebutReservationLocation.getText().toString();
                dateFinReservationLocation = editTextDateFinReservationLocation.getText().toString();

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
        if (!villeLocation.isEmpty() && !marqueVoiture.isEmpty() && !dateDebutReservationLocation.isEmpty() && !dateFinReservationLocation.isEmpty()) {
            return true;
        }
        editTextMessageErreur.setText(getText(R.string.txt_frg_flight_error_champs_invalides));
        return false;
    }
}
