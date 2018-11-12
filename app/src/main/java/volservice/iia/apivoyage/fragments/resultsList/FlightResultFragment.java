package volservice.iia.apivoyage.fragments.resultsList;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import volservice.iia.apivoyage.R;
import volservice.iia.apivoyage.adapters.FlightAdapter;
import volservice.iia.apivoyage.fragments.FlightFragment;
import volservice.iia.apivoyage.fragments.HostelFragment;
import volservice.iia.apivoyage.fragments.MainFragment;
import volservice.iia.apivoyage.fragments.thankYouFragment;
import volservice.iia.apivoyage.items.FlightItem;
import volservice.iia.apivoyage.items.HostelItem;

public class FlightResultFragment extends Fragment {

    public final static String ITEMS_ALLER = "ITEMS_ALLER";
    public final static String ITEMS_RETOUR = "ITEMS_RETOUR";
    // ALLER = true REOUR = false
    public final static String SELECTION_STATE = "SELECTION_STATE";

    private Bundle arguments;

    private ListView listView;
    private Button btnReturn;
    private Button btnValid;

    private int idVol;
    private int idVolAller;
    private int idVolRetour;

    private FlightItem itemAller;
    private FlightItem itemRetour;

    private int idItemSelected = -1;
    private View lastViewSelected = null;
    private FlightItem currentItem;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        arguments = getArguments();
        return inflater.inflate(R.layout.frg_resultlist_flight, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.list_flight);
        btnReturn = view.findViewById(R.id.list_return);
        btnValid = view.findViewById(R.id.list_accept);
        btnValid.setEnabled(false);

        final boolean isAller = arguments.getBoolean(SELECTION_STATE);
        btnValid.setText(isAller ? getText(R.string.txt_flight_btn_select_aller) : getString(R.string.txt_flight_btn_select_retour));
        FlightItem[] tempItem = isAller ? new FlightItem[0] : ((FlightItem[]) arguments.getSerializable(ITEMS_ALLER));
        if (tempItem.length > 0 && !isAller) {
            itemAller = tempItem[0];
        }
        final FlightItem[] items = isAller ? (FlightItem[]) arguments.getSerializable(ITEMS_ALLER) : (FlightItem[]) arguments.getSerializable(ITEMS_RETOUR);
        listView.setAdapter(new FlightAdapter(view.getContext(), items));

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
                Fragment fragment = new FlightFragment();
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
                Fragment fragment;
                Bundle bundle = null;
                if (isAller) {
                    idVolAller = idVol;
                    Toast.makeText(v.getContext(), getString(R.string.txt_toast_aller_isok), Toast.LENGTH_SHORT).show();
                    bundle = new Bundle();
                    idVolAller = idItemSelected;
                    itemAller = currentItem;
                    FlightItem[] temp = new FlightItem[1];
                    temp[0] = itemAller;
                    bundle.putSerializable(ITEMS_ALLER, temp);
                    bundle.putSerializable(ITEMS_RETOUR, (FlightItem[]) arguments.getSerializable(ITEMS_RETOUR));
                    bundle.putSerializable(SELECTION_STATE, false);
                    fragment = new FlightResultFragment();
                } else {
                    idVolRetour = idVol;
                    itemRetour = currentItem;
                    sendPostAller();
                    sendPostRetour();
                    Toast.makeText(v.getContext(), getString(R.string.txt_toast_success), Toast.LENGTH_SHORT).show();
                    fragment = new thankYouFragment();
                }

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                if (bundle != null)
                    fragment.setArguments(bundle);
                ft.replace(R.id.screenArea, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

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
        idVol = idItemSelected != -1 ? ((FlightItem) listView.getAdapter().getItem(position)).getId() : -1;
        currentItem = idItemSelected != -1 ? ((FlightItem) listView.getAdapter().getItem(position)) : null;
        btnValid.invalidate();
    }

    public void sendPostAller() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://192.168.214.16:8890/api_vols/vol/addReservation.php");
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setRequestMethod("PUT");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("idVol", itemAller.getId());
                    jsonParam.put("nom", "DOE");
                    jsonParam.put("prenom", "JOE");
                    jsonParam.put("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxfQ.FgKm8JNIbbmf-KLFZbQI9NGo_3S3NoIjprvPLISlh0c");
                    jsonParam.put("classVol", itemAller.getCategorie());

                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(jsonParam.toString());

                    InputStreamReader in = new InputStreamReader(conn.getInputStream());

                    BufferedReader br = new BufferedReader(in);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();

                    System.out.println(sb.toString());

                    os.flush();
                    os.close();

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void sendPostRetour() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://192.168.214.16:8890/api_vols/vol/addReservation.php");
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setRequestMethod("PUT");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("idVol", itemRetour.getId());
                    jsonParam.put("nom", "DOE");
                    jsonParam.put("prenom", "JOE");
                    jsonParam.put("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxfQ.FgKm8JNIbbmf-KLFZbQI9NGo_3S3NoIjprvPLISlh0c");
                    jsonParam.put("classVol", itemRetour.getCategorie());

                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(jsonParam.toString());

                    InputStreamReader in = new InputStreamReader(conn.getInputStream());

                    BufferedReader br = new BufferedReader(in);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();

                    System.out.println(sb.toString());

                    os.flush();
                    os.close();
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


}