package de.dhbwloerrach.beaconlocation.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import de.dhbwloerrach.beaconlocation.R;
import de.dhbwloerrach.beaconlocation.adapters.MachineAdapter;
import de.dhbwloerrach.beaconlocation.database.DatabaseHandler;
import de.dhbwloerrach.beaconlocation.models.Beacon;
import de.dhbwloerrach.beaconlocation.models.Machine;

/**
 * Created by alirei on 09.08.2015.
 */
public class AddBeaconsToMachineFragment extends BaseFragment{
    private ArrayList<Beacon> selectedBeacons = new ArrayList<>();
    private Machine machine;
    private ActivityCommons commons;
    private MachineAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_machines, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        commons = activity.getCommons();

        if (!initialized) {
            adapter = new MachineAdapter(activity);
            adapter.addItems(new DatabaseHandler(activity).getAllMachines());
            initialized = true;
        }

        final ListView listView = (ListView) activity.findViewById(R.id.listView2);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                new AlertDialog.Builder(activity)
                        .setTitle(R.string.alert_title_warning)
                        .setMessage("Are you sure that you want to add the selected beacons to the machine?")
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedBeacons = getArguments().getParcelableArrayList("selectedBeacons");
                                machine = adapter.getItem(position);
                                DatabaseHandler databaseHandler = new DatabaseHandler(activity);
                                for (Beacon beacon: selectedBeacons) {
                                    beacon.setMachineId(machine.getId());
                                    if (!beacon.checkBecaoninDB(beacon, databaseHandler)){
                                        databaseHandler.createBeacon(beacon);
                                    }
                                    else {
                                        databaseHandler.updateBeacon(beacon);
                                    }
                                }
                                commons.switchFragment(ActivityCommons.FragmentType.BEACON_SEARCH);
                                }}
                            )
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                commons.switchFragment(ActivityCommons.FragmentType.BEACON_SEARCH);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        listView.setAdapter(adapter);

    }


        @Override
    protected void createActionBarMenu(Menu menu) {

    }

    @Override
    protected boolean handleMenuClick(int itemId) {
        return false;
    }
}
