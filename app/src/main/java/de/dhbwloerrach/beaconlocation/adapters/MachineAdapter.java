package de.dhbwloerrach.beaconlocation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

import de.dhbwloerrach.beaconlocation.R;
import de.dhbwloerrach.beaconlocation.database.DatabaseHandler;
import de.dhbwloerrach.beaconlocation.models.Machine;

/**
 * Created by Lukas on 31.07.2015.
 */
public class MachineAdapter extends ArrayAdapter<Machine> {
    private final Context context;
    private ArrayList<Machine> machines = new ArrayList<>();
    private DecimalFormat distanceFormat = new DecimalFormat("#m");

    public MachineAdapter(Context context) {
        super(context, R.layout.listitem_beacon);
        this.context = context;
    }

    public void addItem(Machine item) {
        machines.add(item);
    }

    public void addItems(Collection<Machine> items) {
        machines.addAll(items);
    }

    public void clearItems() {
        machines.clear();
    }

    @Override
    public int getCount() {
        return machines.size();
    }

    @Override
    public Machine getItem(int position) {
        return machines.get(position);
    }

    @Override
    public int getPosition(Machine beacon) {
        return machines.indexOf(beacon);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Machine machine = machines.get(position);

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.listitem_machine, parent, false);

        // 3. Get the two text view from the rowView
        TextView valueViewMinor = (TextView) rowView.findViewById(R.id.machineName);
        ImageView warningIcon = (ImageView) rowView.findViewById(R.id.machineWarning);

        // 4. Set the text for textView
        valueViewMinor.setText(machine.getName().toString());

        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        try {
            warningIcon.setVisibility(databaseHandler.getAllBeaconsByMachine(machine.getId()).size() == 0 ? View.VISIBLE : View.GONE);
        } finally {
            databaseHandler.close();
        }

        // 5. retrn rowView
        return rowView;
    }
}
