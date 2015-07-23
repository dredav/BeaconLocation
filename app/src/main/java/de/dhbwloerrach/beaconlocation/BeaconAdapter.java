package de.dhbwloerrach.beaconlocation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Lukas on 22.07.2015.
 */
public class BeaconAdapter extends ArrayAdapter<Beacon> {

        private final Context context;
        private final ArrayList<Beacon> itemsArrayList;
        private DecimalFormat distanceFormat = new DecimalFormat("#.##m");

        public BeaconAdapter(Context context, ArrayList<Beacon> itemsArrayList) {

            super(context, R.layout.listitem_beacon, itemsArrayList);

            this.context = context;
            this.itemsArrayList = itemsArrayList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // 1. Create inflater
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
            valueViewMinor.setText(itemsArrayList.get(position).getMinor());
            valueViewDistance.setText(distanceFormat.format(itemsArrayList.get(position).getAverageDistance(20)));
            valueViewUuid.setText(itemsArrayList.get(position).getUuid());
            valueViewBluetoothName.setText(itemsArrayList.get(position).getBluetoothName());
            valueViewTxpower.setText(itemsArrayList.get(position).getTxpower().toString());
            valueViewRssi.setText(itemsArrayList.get(position).getRssi().toString());
            valueViewMajor.setText(itemsArrayList.get(position).getMajor());
            valueViewBluetoothAddress.setText(itemsArrayList.get(position).getBluetoothAddress().toString());


            // 5. retrn rowView
            return rowView;
        }
}
