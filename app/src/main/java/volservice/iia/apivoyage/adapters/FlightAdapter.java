package volservice.iia.apivoyage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import volservice.iia.apivoyage.R;
import volservice.iia.apivoyage.items.FlightItem;

public class FlightAdapter extends ArrayAdapter<FlightItem> {

    private final Context context;
    private final FlightItem[] values;

    public FlightAdapter(Context context, FlightItem[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_flight, parent, false);
        TextView txtDate = itemView.findViewById(R.id.descPlaneDate);
        TextView txtvol = itemView.findViewById(R.id.firstLineFlight);
        TextView txtplaces = itemView.findViewById(R.id.secondLinePlacesFlight);
        TextView txtprix = itemView.findViewById(R.id.secondLinePrixFlight);
        ImageView imageView = itemView.findViewById(R.id.iconPlaneItem);

        txtDate.setText(values[position].getDate());
        txtvol.setText(values[position].getVolInfos());
        txtplaces.setText(values[position].getPlacesRestantes());
        txtprix.setText(values[position].getDate());

        return itemView;
    }
}
