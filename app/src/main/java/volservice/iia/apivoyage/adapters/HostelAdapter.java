package volservice.iia.apivoyage.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import volservice.iia.apivoyage.items.HostelItem;

public class HostelAdapter extends ArrayAdapter<HostelItem> {

    private final Context context;
    private final HostelItem[] values;

    public HostelAdapter(Context context, HostelItem[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }
}
