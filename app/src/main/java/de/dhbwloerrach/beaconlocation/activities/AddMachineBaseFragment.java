package de.dhbwloerrach.beaconlocation.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;

import java.util.ArrayList;

import de.dhbwloerrach.beaconlocation.R;
import de.dhbwloerrach.beaconlocation.database.DatabaseHandler;
import de.dhbwloerrach.beaconlocation.models.Beacon;
import de.dhbwloerrach.beaconlocation.models.Delegate;
import de.dhbwloerrach.beaconlocation.models.Machine;

/**
 * Created by Lukas on 13.08.2015.
 */
public abstract class AddMachineBaseFragment extends BaseFragment {

    /**
     * Add a machine with beacons from form
     * @param textField EditText
     * @param beacons ArrayList
     * @param aim FragmentType
     */
    protected void addMachine(final EditText textField, final ArrayList<Beacon> beacons, final ActivityCommons.FragmentType aim) {
        final ActivityCommons commons = activity.getCommons();
        final DatabaseHandler databaseHandler = new DatabaseHandler(activity);

        final Machine newMachine = createMachine(textField, databaseHandler);
        if (newMachine != null) {

            Delegate action = new Delegate() {
                @Override
                public void execute() {
                    insertMachine(databaseHandler, newMachine);
                    insertBeacons(databaseHandler, beacons, newMachine.getId());
                    commons.switchFragment(aim);
                }
            };
            if(checkBeacons(beacons, databaseHandler, action)) {
                action.execute();
            }
        }
    }

    /**
     * Create a new machine from from
     * @param textField EditText
     * @param databaseHandler DatabaseHandler
     * @return Machine
     */
    protected Machine createMachine(EditText textField, DatabaseHandler databaseHandler){
        // Wenn Texfeld leer ist
        if (textField.getText() == null || textField.getText().toString().isEmpty()) {
            new AlertDialog.Builder(activity)
                    .setTitle(R.string.alert_title_warning)
                    .setMessage(R.string.alert_message_enterName)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return null;
        } else {
            // databaseHandler erstellen
            final Machine newMachine = new Machine();
            newMachine.setName(textField.getText().toString());
            if (newMachine.checkMachineinDB(newMachine, databaseHandler)) {
                new AlertDialog.Builder(activity)
                        .setTitle(R.string.alert_title_warning)
                        .setMessage(R.string.alert_message_name)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return null;
            } else {
                return newMachine;
            }
        }
    }

    /**
     * Check if some beacons already associated with one machine
     * @param beacons ArrayList
     * @param databaseHandler DatabaseHandler
     * @param action Delegate
     * @return boolean
     */
    protected boolean checkBeacons(ArrayList<Beacon> beacons, DatabaseHandler databaseHandler, final Delegate action) {
        String allOverwriteBeacons = "";
        for (Beacon beacon : beacons) {

            if (!beacon.checkBecaoninDB(beacon, databaseHandler)) {
                continue;
            }
            if (beacons.indexOf(beacon) <= beacons.size() - 1) {
                allOverwriteBeacons += beacon.getMinor().toString() + ", ";
            } else {
                allOverwriteBeacons += beacon.getMinor().toString() + " ";
            }
        }

        if (!allOverwriteBeacons.isEmpty()) {
            new AlertDialog.Builder(activity)
                    .setTitle(R.string.alert_title_warning)
                    .setMessage("The Beacon " + allOverwriteBeacons + "is already part of a machine. Are you sure you want to overwrite this entry?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            action.execute();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return false;
        }
        return true;
    }

    /**
     * Add a new machine to the database
     * @param databaseHandler DatabaseHandler
     * @param newMachine Machine
     * @return int
     */
    protected int insertMachine(DatabaseHandler databaseHandler, Machine newMachine) {
        int machineId = databaseHandler.createMachine(newMachine);
        newMachine.setId(machineId);
        return machineId;
    }

    /**
     * Insert a beacon to database and associate ith with one machine
     * @param databaseHandler DatabaseHandler
     * @param beacons Beacon
     * @param machineID int
     */
    protected void insertBeacons(DatabaseHandler databaseHandler, ArrayList<Beacon> beacons, int machineID) {
        for (Beacon beacon: beacons){
            beacon.setMachineId(machineID);

            Beacon databaseBeacon = databaseHandler.getBeacon(beacon.getMinor()); //, beacon.getMajor(), beacon.getUuid()

            if (databaseBeacon != null) {
                beacon.setId(databaseBeacon.getId());
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
