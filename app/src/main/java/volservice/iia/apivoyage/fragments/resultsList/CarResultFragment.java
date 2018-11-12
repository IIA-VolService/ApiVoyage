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
        final CarItem[] items01 = (CarItem[]) arguments.getSerializable(ITEMS_API01);
        final CarItem[] items02 = (CarItem[]) arguments.getSerializable(ITEMS_API02);

        final CarItem[] items = getList(items01, items02);

        listView.setAdapter(new CarAdapter(view.getContext(), items01));

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
                Fragment fragment = new MainFragment();
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
        btnValid.invalidate();
    }
}