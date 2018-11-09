package volservice.iia.apivoyage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import volservice.iia.apivoyage.R;
import volservice.iia.apivoyage.items.HostelItem;

public class HostelAdapter extends ArrayAdapter<HostelItem> {

    private final Context context;
    private final HostelItem[] values;

    public HostelAdapter(Context context, HostelItem[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_hostel, parent, false);
        TextView txtDate = itemView.findViewById(R.id.descHostelValue);
        TextView txtvol = itemView.findViewById(R.id.firstLineHostel);
        TextView txtplaces = itemView.findViewById(R.id.secondLinePlacesHostel);
        TextView txtprix = itemView.findViewById(R.id.secondLinePrixHostel);
        ImageView imageView = itemView.findViewById(R.id.iconCarItem);

        int stars = values[position].getEtoiles();
        imageView.setImageResource(stars > 2 ? stars == 5 ? R.drawable.ic_hostel_premium : R.drawable.ic_hostel_advanced : R.drawable.ic_hostel_basic);

        txtDate.setText(values[position].getClasse());
        txtvol.setText(values[position].getNom());
        txtplaces.setText(values[position].getFullAdress());
        txtprix.setText(values[position].getPrix());

        return itemView;
    }
}
