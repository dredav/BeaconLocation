package de.dhbwloerrach.beaconlocation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Collection;

import de.dhbwloerrach.beaconlocation.R;
import de.dhbwloerrach.beaconlocation.models.Beacon;
import de.dhbwloerrach.beaconlocation.models.BeaconList;

/**
 * Created by Lukas on 22.07.2015.
 */
public class BeaconAdapter extends ArrayAdapter<Beacon> {
    private final Context context;
    private BeaconList beacons = new BeaconList();
    private DecimalFormat distanceFormat = new DecimalFormat("#m");

    public BeaconAdapter(Context context) {
        super(context, R.layout.listitem_beacon);
        this.context = context;
    }

    public void addItem(Beacon item) {
        beacons.add(item);
        beacons.SortByDistance();
    }

    public void addItems(Collection<Beacon> items) {
        beacons.addAll(items);
        beacons.SortByDistance();
    }

    public void clearItems() {
        beacons.clear();
    }

    @Override
    public int getCount() {
        return beacons.size();
    }

    @Override
    public Beacon getItem(int position) {
        return beacons.get(position);
    }

    @Override
    public int getPosition(Beacon beacon) {
        return super.getPosition(beacon);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.listitem_beacon, parent, false);

        // 3. Get the two text view from the rowView
        TextView valueViewMinor = (TextView) rowView.findViewById(R.id.minor);
        TextView valueViewDistance = (TextView) rowView.findViewById(R.id.distance);
        TextView valueViewUuid = (TextView) rowView.findViewById(R.id.uuid);
        TextView valueViewBluetoothName = (TextView) rowView.findViewById(R.id.bluetoothname);
        TextView valueViewTxpower = (TextView) rowView.findViewById(R.id.txpower);
        TextView valueViewRssi = (TextView) rowView.findViewById(R.id.rssi);
        TextView valueViewMajor = (TextView) rowView.findViewById(R.id.major);
        TextView valueViewBluetoothAddress = (TextView) rowView.findViewById(R.id.bluetoothaddress);

        // 4. Set the text for textView
        valueViewUuid.setText(beacons.get(position).getUuid());
        valueViewMinor.setText(beacons.get(position).getMinor().toString());
        valueViewMajor.setText(beacons.get(position).getMajor().toString());
        valueViewDistance.setText(distanceFormat.format(beacons.get(position).getDistance()));
        valueViewBluetoothName.setText(beacons.get(position).getBluetoothName());
        valueViewTxpower.setText(beacons.get(position).getTxpower().toString());
        valueViewRssi.setText(beacons.get(position).getRssi().toString());
        valueViewBluetoothAddress.setText(beacons.get(position).getBluetoothAddress());

        // 5. retrn rowView
        return rowView;
    }
}
