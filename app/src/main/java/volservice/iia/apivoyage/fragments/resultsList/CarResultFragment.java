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
import volservice.iia.apivoyage.adapters.CarAdapter;
import volservice.iia.apivoyage.fragments.FlightFragment;
import volservice.iia.apivoyage.fragments.RentACarFragment;
import volservice.iia.apivoyage.items.CarItem;

public class CarResultFragment extends Fragment {

    private final static String ITEMS = "ITEMS";

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
        return inflater.inflate(R.layout.frg_resultlist_car, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.list_car);
        btnReturn = view.findViewById(R.id.list_return);
        btnValid = view.findViewById(R.id.list_accept);
        btnValid.setActivated(false);

        btnValid.setText(getString(R.string.txt_btn_select));
        final CarItem[] items = (CarItem[]) arguments.getSerializable(ITEMS);
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
                // Récupérer l'item actuel + le sauvergarder et le transmettre au formulaire client

                Toast.makeText(v.getContext(), getString(R.string.txt_toast_success), Toast.LENGTH_SHORT).show();
                Fragment fragment = new RentACarFragment();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();

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