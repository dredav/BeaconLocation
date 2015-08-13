package de.dhbwloerrach.beaconlocation.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;

import java.util.ArrayList;

import de.dhbwloerrach.beaconlocation.R;
import de.dhbwloerrach.beaconlocation.database.DatabaseHandler;
import de.dhbwloerrach.beaconlocation.models.Beacon;
import de.dhbwloerrach.beaconlocation.models.Machine;

/**
 * Created by Lukas on 13.08.2015.
 */
public abstract class AddMachineBaseFragment extends BaseFragment {
    protected void addMachine(final EditText textField, final ArrayList<Beacon> beacons, final ActivityCommons.FragmentType aim) {
        final ActivityCommons commons = activity.getCommons();

        // Wenn Texfeld nicht leet ist
        if (textField.getText() != null && !textField.getText().toString().isEmpty()) {
            // databaseHandler erstellen
            final DatabaseHandler databaseHandler = new DatabaseHandler(activity);
            final Machine newMachine = new Machine();
            newMachine.setName(textField.getText().toString());
            if (newMachine.checkMachineinDB(newMachine, databaseHandler)){
                new AlertDialog.Builder(activity)
                        .setTitle(R.string.alert_title_warning)
                        .setMessage(R.string.alert_message_name)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            else {

                String allOverwriteBeacons = "";
                for (Beacon beacon : beacons) {

                    if (!beacon.checkBecaoninDB(beacon, databaseHandler)) {
                        continue;
                    }
                    if (beacons.indexOf(beacon) <= beacons.size()-1){
                        allOverwriteBeacons += beacon.getMinor().toString() + ", ";
                    }
                    else {
                        allOverwriteBeacons += beacon.getMinor().toString() + " ";
                    }
                }

                if (allOverwriteBeacons.isEmpty()) {
                    writeChangesToDB(newMachine, databaseHandler, beacons);
                    commons.switchFragment(aim);
                } else {
                    new AlertDialog.Builder(activity)
                            .setTitle(R.string.alert_title_warning)
                            .setMessage("The Beacon " + allOverwriteBeacons + "is already part of a machine. Are you sure you want to overwrite this entry?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    writeChangesToDB(newMachine, databaseHandler, beacons);
                                    commons.switchFragment(aim);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    commons.switchFragment(aim);
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

    protected void writeChangesToDB(Machine newMachine, DatabaseHandler databaseHandler, ArrayList<Beacon> beacons) {
        Integer machineID = databaseHandler.createMachine(newMachine);
        for (Beacon beacon: beacons){
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

}
