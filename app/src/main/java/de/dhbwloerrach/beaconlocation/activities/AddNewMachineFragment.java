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
                // Maschine erstellen
                Machine newMachine = new Machine();
                newMachine.setName(textField.getText().toString());
                // Maschine in DB eintragen
                final DatabaseHandler databaseHandler = new DatabaseHandler(activity);
                Integer machineID = databaseHandler.createMachine(newMachine);
                // Beacons prüfen, ob sie bereits in der DB vorliegen und einrtagen
                for (final Beacon beacon: selectedBeacons){
                    Beacon databaseBeacon = databaseHandler.getBeacon(beacon.getMinor(), beacon.getMajor(), beacon.getUuid());
                    beacon.setMachineId(machineID);
                    if (databaseBeacon != null){
                        databaseHandler.createBeacon(beacon);

                    if(allOverwriteBeacons.isEmpty()) {
                        writeChangesToDB(textField, databaseHandler);
                    }
                    else {
                        new AlertDialog.Builder(activity)
                                .setTitle("Overwrite Entry")
                                .setMessage("The Beacon " + allOverwriteBeacons + "is already part of a machine. Are you sure you want to overwrite this entry?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        writeChangesToDB(textField, databaseHandler);
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

                else {
                    new AlertDialog.Builder(activity)
                            .setTitle("Warning")
                            .setMessage("Please enter a name for your machine")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }


            }


            protected boolean checkBecaoninDB (Beacon beacon, DatabaseHandler databaseHandler){
                Beacon databaseBeacon = databaseHandler.getBeacon(beacon.getMinor(), beacon.getMajor(), beacon.getUuid());
                return databaseBeacon != null;
            }

            protected void writeChangesToDB(EditText textField, DatabaseHandler databaseHandler) {
                Machine newMachine = new Machine();
                newMachine.setName(textField.getText().toString());
                Integer machineID = databaseHandler.createMachine(newMachine);
                for (Beacon beacon: selectedBeacons){
                    beacon.setMachineId(machineID);
                    if (checkBecaoninDB(beacon, databaseHandler)) {
                        databaseHandler.updateBeacon(beacon);
                    }
                    else{
                        databaseHandler.createBeacon(beacon);

                    }

                }

                new AlertDialog.Builder(activity)
                        .setTitle("Sucess")
                        .setMessage("Your chnges have been saved to the database")
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
