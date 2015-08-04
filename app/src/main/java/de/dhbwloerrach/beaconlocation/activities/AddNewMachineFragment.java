package de.dhbwloerrach.beaconlocation.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import de.dhbwloerrach.beaconlocation.R;
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
        final Button cancelButton = (Button) activity.findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                commons.switchFragment(ActivityCommons.FragmentType.BEACON_SEARCH);

            }
        });
        final Button CommitButton = (Button) activity.findViewById(R.id.button_commit);
        CommitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Textfeld auslesen
                final EditText textField = (EditText) activity.findViewById(R.id.editText);
                // Maschine erstellen
                Machine newMachine = new Machine();
                newMachine.setName(textField.getText().toString());
                // Maschine in DB eintragen
                DatabaseHandler databaseHandler = new DatabaseHandler(activity);
                databaseHandler.createMachine(newMachine);
                // Maschinen ID auslesen
                Integer machineID;

                // Beacons prüfen, ob sie bereits in der DB vorliegen und einrtagen
                for (Beacon beacon: selectedBeacons){
                    Beacon databaseBeacon = databaseHandler.getBeacon(beacon.getMinor(), beacon.getMajor(), beacon.getUuid());
                    beacon.setMachineId(machineID);
                    if (databaseBeacon != null){
                        databaseHandler.createBeacon(beacon);
                    }
                    else{
                        // WARNUNG fehlt
                        databaseHandler.updateBeacon(beacon);
                    }
                }


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
