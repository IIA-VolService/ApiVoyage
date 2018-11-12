package volservice.iia.apivoyage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import volservice.iia.apivoyage.R;
import volservice.iia.apivoyage.items.CarItem;

public class CarAdapter extends ArrayAdapter {

    private final Context context;
    private final CarItem[] values;

    public CarAdapter(Context context, CarItem[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_car, parent, false);
        TextView txtClasse = itemView.findViewById(R.id.descCarValue);
        TextView txtVehicule = itemView.findViewById(R.id.firstLineCar);
        TextView txtMArque = itemView.findViewById(R.id.secondLinePlacesCar);
        TextView txtprix = itemView.findViewById(R.id.secondLinePrixCar);
        ImageView imageView = itemView.findViewById(R.id.iconCarItem);

        int type = values[position].getType();
        imageView.setImageResource(type != 2 ? type == 1 ? R.drawable.ic_car_premium : R.drawable.ic_car_advanced : R.drawable.ic_car_basic);

        txtClasse.setText(values[position].getClasse());
        txtVehicule.setText(values[position].getModeleAndPlaces());
        txtMArque.setText(values[position].getMarque());
        txtprix.setText(values[position].getFormatPrix());

        return itemView;
    }
}
