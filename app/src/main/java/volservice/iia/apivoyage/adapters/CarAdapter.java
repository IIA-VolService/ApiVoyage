package volservice.iia.apivoyage.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import volservice.iia.apivoyage.items.CarItem;

public class CarAdapter extends ArrayAdapter {

    private final Context context;
    private final CarItem[] values;

    public CarAdapter(Context context, CarItem[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }
}
