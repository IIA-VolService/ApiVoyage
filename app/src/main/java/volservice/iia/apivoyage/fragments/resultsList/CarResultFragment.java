package volservice.iia.apivoyage.fragments.resultsList;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import volservice.iia.apivoyage.R;
import volservice.iia.apivoyage.adapters.CarAdapter;
import volservice.iia.apivoyage.fragments.MainFragment;
import volservice.iia.apivoyage.fragments.RentACarFragment;
import volservice.iia.apivoyage.items.CarItem;

public class CarResultFragment extends Fragment {

    public final static String ITEMS_API01 = "ITEMS_API01";
    public final static String ITEMS_API02 = "ITEMS_API02";

    private Bundle arguments;

    private ListView listView;
    private Button btnReturn;
    private Button btnValid;

    private CarItem itemSelected;
    private int idItemSelected = -1;
    private View lastViewSelected = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        arguments = getArguments();
        return inflater.inflate(R.layout.frg_resultlist_car, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.list_car);
        btnReturn = view.findViewById(R.id.list_return);
        btnValid = view.findViewById(R.id.list_accept);
        btnValid.setEnabled(false);

        btnValid.setText(getString(R.string.txt_btn_select));
        final CarItem[] items01 = (CarItem[]) arguments.getSerializable(ITEMS_API01);
        final CarItem[] items02 = (CarItem[]) arguments.getSerializable(ITEMS_API02);

        final CarItem[] items = getList(items01, items02);

        listView.setAdapter(new CarAdapter(view.getContext(), items));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(view, position);
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retour page de sélection
                Fragment fragment = new RentACarFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();

                ft.replace(R.id.screenArea, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        btnValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postRequest();


                Toast.makeText(v.getContext(), getString(R.string.txt_toast_success), Toast.LENGTH_SHORT).show();
                Fragment fragment = new MainFragment();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();

                ft.replace(R.id.screenArea, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

    }

    private void postRequest() {
        switch (itemSelected.getApi()) {
            case API01:
                postApi01();
                break;

            case API02:
                postApi02();
                break;
        }
    }

    private void postApi01() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://192.168.214.23:5001/api/VoitureReservation/Reserver");
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("marque", itemSelected.getMarque());
                    jsonParam.put("modele", itemSelected.getModele());
                    jsonParam.put("nbPassager", itemSelected.getNbPlaces());
                    jsonParam.put("prixReservation", itemSelected.getPrix());
                    jsonParam.put("localisation", itemSelected.getLocalisation());

                    JSONObject reservations = new JSONObject();
                    reservations.put("nomResa", "JOE");
                    reservations.put("prenomResa", "WOLOLO");
                    reservations.put("numeroPermisResa", "777777777777");
                    reservations.put("dateDebutReservation", itemSelected.getDateDebut());
                    reservations.put("dateFinReservation", itemSelected.getDateFin());
                    jsonParam.put("reservations", reservations);

                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    InputStreamReader in = new InputStreamReader(conn.getInputStream());

                    BufferedReader br = new BufferedReader(in);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();

                    System.out.println(sb.toString());

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void postApi02() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://192.168.214.31/car/rent.php?token=SZAdlqfwCdb18CzUads4tLTqN6EaLRlr");
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setRequestMethod("PUT");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("id", itemSelected.getIdCar());
                    jsonParam.put("idCustomer", 25);
                    jsonParam.put("dateDebutLoc", itemSelected.getDateDebut());
                    jsonParam.put("dateFinLoc", itemSelected.getDateFin());

                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    InputStreamReader in = new InputStreamReader(conn.getInputStream());

                    BufferedReader br = new BufferedReader(in);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();

                    System.out.println(sb.toString());

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private CarItem[] getList(CarItem[] items01, CarItem[] items02) {
        int size = items01.length + items02.length;
        CarItem[] items = new CarItem[size];
        for (int i = 0; i < size; i++) {
            if (i < items01.length) {
                items[i] = items01[i];
            } else {
                items[i] = items02[i - items01.length];
            }
        }
        return items;
    }

    private void selectItem(View view, int position) {
        //Si on reclique sur le même
        if (position == idItemSelected) {
            lastViewSelected.setBackground(null);
            idItemSelected = -1;
            btnValid.setEnabled(false);
        }
        // si on choisi un item différent
        else {
            if (idItemSelected != -1) {
                lastViewSelected.setBackground(null);
                lastViewSelected.invalidate();
            }
            // Si on sélectionne un item
            idItemSelected = position;
            view.setBackgroundColor(Color.parseColor("#00DD00"));
            view.invalidate();
            lastViewSelected = view;
            btnValid.setEnabled(true);
        }
        itemSelected = idItemSelected != -1 ? ((CarItem) listView.getAdapter().getItem(position)) : null;
        btnValid.invalidate();
    }
}