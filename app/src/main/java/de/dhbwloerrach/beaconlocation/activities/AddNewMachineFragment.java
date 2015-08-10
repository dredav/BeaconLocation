package de.dhbwloerrach.beaconlocation.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import de.dhbwloerrach.beaconlocation.R;
import de.dhbwloerrach.beaconlocation.adapters.BeaconAdapter;
import de.dhbwloerrach.beaconlocation.database.DatabaseHandler;
import de.dhbwloerrach.beaconlocation.models.Beacon;
import de.dhbwloerrach.beaconlocation.models.Machine;

/**
 * Created by alirei on 31.07.2015.
 */
public class AddNewMachineFragment extends BaseFragment {
    private ArrayList<Beacon> selectedBeacons = new ArrayList<>();
    private ActivityCommons commons;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_machines, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        commons = activity.getCommons();
        selectedBeacons = getArguments().getParcelableArrayList("selectedBeacons");


        BeaconAdapter adapter = new BeaconAdapter(activity);
        ListView beaconList = (ListView) activity.findViewById(R.id.beaconList);
        for (Beacon beacon : selectedBeacons){
            adapter.addItem(beacon);
        }
        beaconList.setAdapter(adapter);


        final Button cancelButton = (Button) activity.findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                commons.switchFragment(ActivityCommons.FragmentType.BEACON_SEARCH);

            }
        });
        final Button CommitButton = (Button) activity.findViewById(R.id.button_create);
        CommitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Textfeld auslesen
                final EditText textField = (EditText) activity.findViewById(R.id.editText);
                // Wenn Texfeld nicht leet ist
                if (textField.getText() != null && !textField.getText().toString().isEmpty()) {
                    // databashandler erstellen
                    final DatabaseHandler databaseHandler = new DatabaseHandler(activity);
                    Machine newMachine = new Machine();
                    if (newMachine.checkMachineinDB(textField.getText().toString(), databaseHandler)){
                        new AlertDialog.Builder(activity)
                                .setTitle(R.string.alert_title_warning)
                                .setMessage(R.string.alert_message_name)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                    else {

                        String allOverwriteBeacons = "";
                        for (Beacon beacon : selectedBeacons) {

                            if (!beacon.checkBecaoninDB(beacon, databaseHandler)) {
                                continue;
                            }
                            if (selectedBeacons.indexOf(beacon) <= selectedBeacons.size()-1){
                                allOverwriteBeacons += beacon.getMinor().toString() + ", ";
                            }
                            else {
                                allOverwriteBeacons += beacon.getMinor().toString() + " ";
                            }
                        }

                        if (allOverwriteBeacons.isEmpty()) {
                            writeChangesToDB(textField, databaseHandler);
                            commons.switchFragment(ActivityCommons.FragmentType.BEACON_SEARCH);
                        } else {
                            new AlertDialog.Builder(activity)
                                    .setTitle(R.string.alert_title_warning)
                                    .setMessage("The Beacon " + allOverwriteBeacons + "is already part of a machine. Are you sure you want to overwrite this entry?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            writeChangesToDB(textField, databaseHandler);
                                            commons.switchFragment(ActivityCommons.FragmentType.BEACON_SEARCH);
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            commons.switchFragment(ActivityCommons.FragmentType.BEACON_SEARCH);
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    }
                }
                else {
                    new AlertDialog.Builder(activity)
                            .setTitle(R.string.alert_title_warning)
                            .setMessage(R.string.alert_message_enterName)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }


            protected void writeChangesToDB(EditText textField, DatabaseHandler databaseHandler) {
                Machine newMachine = new Machine();
                newMachine.setName(textField.getText().toString());
                Integer machineID = databaseHandler.createMachine(newMachine);
                for (Beacon beacon: selectedBeacons){
                    beacon.setMachineId(machineID);
                    if (beacon.checkBecaoninDB(beacon, databaseHandler)) {
                        databaseHandler.updateBeacon(beacon);
                    }
                    else{
                        databaseHandler.createBeacon(beacon);
                    }
                }

                new AlertDialog.Builder(activity)
                        .setTitle(R.string.alert_title_sucess)
                        .setMessage(R.string.alert_message_save)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    @Override
    protected void createActionBarMenu(Menu menu) {
        //
    }

    @Override
    protected boolean handleMenuClick(int itemId) {
        return true;
    }
}
