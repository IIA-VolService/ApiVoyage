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
import volservice.iia.apivoyage.adapters.HostelAdapter;
import volservice.iia.apivoyage.fragments.HostelFragment;
import volservice.iia.apivoyage.items.HostelItem;

public class HostelResultFragment extends Fragment {

    public final static String ITEMS = "ITEMS";

    private Bundle arguments;

    private ListView listView;
    private Button btnReturn;
    private Button btnValid;

    private String idHostel;
    private int idItemSelected = -1;
    private View lastViewSelected = null;

    HostelItem[] allItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        arguments = getArguments();
        return inflater.inflate(R.layout.frg_resultlist_hostel, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.list_hostel);
        btnReturn = view.findViewById(R.id.list_return);
        btnValid = view.findViewById(R.id.list_accept);
        btnValid.setEnabled(false);
        btnValid.setText(getString(R.string.txt_btn_select));
        allItems = (HostelItem[]) arguments.getSerializable(ITEMS);
        listView.setAdapter(new HostelAdapter(view.getContext(), allItems));

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
                Fragment fragment = new HostelFragment();
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
                sendPost();
                afterRequest();
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
        idHostel = idItemSelected != -1 ? ((HostelItem) listView.getAdapter().getItem(position)).getId() : "-1";
        btnValid.invalidate();
    }

    public void sendPost() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://192.168.214.11:5001/api/hebergement/book?token=7woAE69CqstvfvdeFLW8KA==");
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setRequestMethod("PUT");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    HostelItem finalItem = null;
                    for (HostelItem item : allItems) {
                        if (item.getId().matches(idHostel)) {
                            finalItem = item;
                            break;
                        }
                    }

                    if (finalItem != null) {
                        JSONObject jsonParam = new JSONObject();
                        jsonParam.put("Id", finalItem.getId());
                        jsonParam.put("Nom", finalItem.getNom());
                        jsonParam.put("Description", finalItem.getDescription());
                        jsonParam.put("Latitude", finalItem.getLatitude());
                        jsonParam.put("Longitude", finalItem.getLongitude());
                        jsonParam.put("Type", finalItem.getType());
                        jsonParam.put("Etoile", finalItem.getEtoiles());
                        jsonParam.put("Adresse", finalItem.getAdresse());
                        jsonParam.put("Ville", finalItem.getVille());
                        jsonParam.put("Pays", finalItem.getPays());
                        jsonParam.put("Telephone", finalItem.getTelephone());
                        jsonParam.put("Mail", finalItem.getMail());
                        jsonParam.put("Prix", finalItem.getPrix());
                        jsonParam.put("DateDebutBooked", finalItem.getDateDebut());
                        jsonParam.put("DateFinBooked", finalItem.getDateFin());

                        DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                        //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
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
                    }
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void afterRequest() {
        Toast.makeText(this.getContext(), getString(R.string.txt_toast_success), Toast.LENGTH_SHORT).show();
        Fragment fragment = new HostelFragment();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        ft.replace(R.id.screenArea, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

}