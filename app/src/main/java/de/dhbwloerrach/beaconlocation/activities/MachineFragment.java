package de.dhbwloerrach.beaconlocation.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import de.dhbwloerrach.beaconlocation.R;
import de.dhbwloerrach.beaconlocation.adapters.BeaconAdapter;
import de.dhbwloerrach.beaconlocation.bluetooth.IBeaconListView;
import de.dhbwloerrach.beaconlocation.database.DatabaseHandler;
import de.dhbwloerrach.beaconlocation.models.Beacon;
import de.dhbwloerrach.beaconlocation.models.BeaconList;
import de.dhbwloerrach.beaconlocation.models.Machine;

/**
 * Created by Lukas on 05.08.2015.
 */
public class MachineFragment extends BaseFragment implements IBeaconListView {
    BeaconAdapter adapter;
    private Boolean updatePaused = false;
    private ArrayList<Beacon> selectedBeacons = new ArrayList<>();
    private Menu menu;
    private ArrayList<Beacon> machineBeacons;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_machine, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(!initialized){
            adapter = new BeaconAdapter(activity);
            //activity.getCommons().startMonitoring(this);
            initialized = true;
        }

        Machine machine = getArguments().getParcelable("machine");
        DatabaseHandler databaseHandler = new DatabaseHandler(activity);
        machineBeacons = databaseHandler.getAllBeaconsByMachine(machine.getId());

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.clearItems();
                adapter.addItems(machineBeacons);
                adapter.notifyDataSetChanged();
            }
        });

        TextView textView = (TextView) activity.findViewById(R.id.name);
        textView.setText(machine.getName());

        ListView listView = (ListView) activity.findViewById(R.id.beaconList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updatePaused=true;
                ListView listView = (ListView) parent;
                Beacon beacon = (Beacon) listView.getAdapter().getItem(position);
                if (selectedBeacons.contains(beacon)){
                    selectedBeacons.remove(beacon);
                    view.setBackgroundColor(Color.TRANSPARENT);
                    if (selectedBeacons.isEmpty()){
                        updatePaused=false;
                    }
                }
                else{
                    selectedBeacons.add(beacon);
                    view.setBackgroundColor(0xFF8db6cd);
                }

                //menu.getItem(0).setEnabled(!updatePaused);
                //menu.getItem(1).setEnabled(updatePaused);
            }
        });


        listView.setAdapter(adapter);
    }

    @Override
    protected void createActionBarMenu(Menu menu) {
        activity.getMenuInflater().inflate(R.menu.menu_machine, menu);
    }

    @Override
    protected boolean handleMenuClick(int itemId) {
        return false;
    }

    @Override
    public void RefreshList(ArrayList<Beacon> beacons) {
        if (!updatePaused) {
            final BeaconList beaconList = new BeaconList();
            BeaconList filteredBeacons = beaconList.filterByLast(5);
            for(Beacon beacon : filteredBeacons) {
                if(machineBeacons.contains(beacon)){
                    beaconList.add(beacon);
                }
            }

            for(Beacon beacon : machineBeacons){
                if(filteredBeacons.contains(beacon)){
                    beaconList.add(beacon);
                }
            }

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.clearItems();
                    adapter.addItems(beaconList);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }
}