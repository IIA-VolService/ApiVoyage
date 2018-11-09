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

import volservice.iia.apivoyage.R;
import volservice.iia.apivoyage.adapters.FlightAdapter;
import volservice.iia.apivoyage.fragments.FlightFragment;
import volservice.iia.apivoyage.items.FlightItem;

public class FlightResultFragment extends Fragment {

    public final static String ITEMS_ALLER = "ITEMS_ALLER";
    public final static String ITEMS_RETOUR = "ITEMS_RETOUR";
    // ALLER = true REOUR = false
    public final static String SELECTION_STATE = "SELECTION_STATE";

    private Bundle arguments;

    private ListView listView;
    private Button btnReturn;
    private Button btnValid;

    private int idItemSelected = -1;
    private View lastViewSelected = null;


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
        btnValid.setActivated(false);

        final boolean isAller = arguments.getBoolean(SELECTION_STATE);
        btnValid.setText(isAller ? getText(R.string.txt_flight_btn_select_aller) : getString(R.string.txt_flight_btn_select_retour));
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
                ft.commit();
            }
        });

        btnValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;
                Bundle bundle = null;
                if (isAller) {
                    Toast.makeText(v.getContext(), getString(R.string.txt_toast_success), Toast.LENGTH_SHORT).show();
                    bundle = new Bundle();
                    bundle.putSerializable(ITEMS_ALLER, null);
                    bundle.putSerializable(ITEMS_RETOUR, (FlightItem[]) arguments.getSerializable(ITEMS_RETOUR));
                    bundle.putSerializable(SELECTION_STATE, false);
                    fragment = new FlightResultFragment();
                } else {
                    fragment = new FlightFragment();
                }

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                if (bundle != null)
                    fragment.setArguments(bundle);

                ft.replace(R.id.screenArea, fragment);
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
            view.setBackgroundColor(Color.parseColor("#0000AA"));
            view.invalidate();
            lastViewSelected = view;
            btnValid.setEnabled(true);
        }
        btnValid.invalidate();
    }
}