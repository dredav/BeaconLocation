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
 * Created by alirei on 09.08.2015.
 */
public class AddBeaconsToMachineFragment extends BaseFragment{
    private ArrayList<Beacon> selectedBeacons = new ArrayList<>();
    private Machine machine;
    private ActivityCommons commons;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_beacons_to_machine, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        commons = activity.getCommons();
        selectedBeacons = getArguments().getParcelableArrayList("selectedBeacons");
        machine = getArguments().getParcelable("machine");

        final Button cancelButton = (Button) activity.findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                commons.switchFragment(ActivityCommons.FragmentType.BEACON_SEARCH);

            }
        });
        final Button CommitButton = (Button) activity.findViewById(R.id.button_create);
        CommitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatabaseHandler databaseHandler = new DatabaseHandler(activity);
                for (Beacon beacon: selectedBeacons){
                    beacon.setMachineId(machine.getId());
                    databaseHandler.updateBeacon(beacon);
                    // prüfen, ob noch nicht in DB Methode aus AddNewMachineFragment klauen
                }


            }
        });
    }


        @Override
    protected void createActionBarMenu(Menu menu) {

    }

    @Override
    protected boolean handleMenuClick(int itemId) {
        return false;
    }
}
