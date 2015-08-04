package de.dhbwloerrach.beaconlocation.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import de.dhbwloerrach.beaconlocation.R;
import de.dhbwloerrach.beaconlocation.adapters.MachineAdapter;
import de.dhbwloerrach.beaconlocation.database.DatabaseHandler;
import de.dhbwloerrach.beaconlocation.models.Beacon;
import de.dhbwloerrach.beaconlocation.models.Machine;

/**
 * Created by Lukas on 04.08.2015.
 */
public class AddManualMachineFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_machine_manual, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!initialized) {
            initialized = true;
        }
    }

    @Override
    protected void createActionBarMenu(Menu menu) {
        activity.getMenuInflater().inflate(R.menu.menu_newmachine, menu);
    }

    @Override
    protected boolean handleMenuClick(int itemId) {
        switch (itemId) {
            case R.id.save_machine:
                // save machine to database
                DatabaseHandler databaseHandler = new DatabaseHandler(activity);

                TextView editText = (TextView)getActivity().findViewById(R.id.name);
                Machine machine = new Machine().setName(editText.getText().toString());
                databaseHandler.createMachine(machine);

                TextView minor1 = (TextView)getActivity().findViewById(R.id.minor1);
                int minorId1 = Integer.parseInt(minor1.getText().toString());
                // TODO : get beacons with all major and uuid
                Beacon beacon1 = databaseHandler.getBeacon(minorId1, 0, 0);
                if(beacon1 != null) {
                    beacon1.setMachineId(machine.getId());
                    databaseHandler.updateBeacon(beacon1);
                } else {
                    beacon1 = new Beacon().setMinor(minorId1).setMachineId(machine.getId());
                    databaseHandler.createBeacon(beacon1);
                }

                TextView minor2 = (TextView)getActivity().findViewById(R.id.minor2);
                int minorId2 = Integer.parseInt(minor2.getText().toString());
                Beacon beacon2 = databaseHandler.getBeacon(minorId2, 0, 0);
                if(beacon2 != null) {
                    beacon2.setMachineId(machine.getId());
                    databaseHandler.updateBeacon(beacon2);
                } else {
                    beacon2 = new Beacon().setMinor(minorId1).setMachineId(machine.getId());
                    databaseHandler.createBeacon(beacon2);
                }
                return true;
            default:
                return false;
        }
    }
}
