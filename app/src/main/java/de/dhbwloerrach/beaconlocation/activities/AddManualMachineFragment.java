package de.dhbwloerrach.beaconlocation.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.dhbwloerrach.beaconlocation.R;
import de.dhbwloerrach.beaconlocation.database.DatabaseHandler;
import de.dhbwloerrach.beaconlocation.models.Beacon;
import de.dhbwloerrach.beaconlocation.models.Machine;

/**
 * Created by Lukas on 04.08.2015.
 */
public class AddManualMachineFragment extends BaseFragment {
    ArrayList<EditText> dynamicMinorIds = new ArrayList<>();

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

    protected void addBeaconInputField() {
        LinearLayout layout = (LinearLayout) activity.findViewById(R.id.minors);

        EditText editText = new EditText(activity);
        editText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        editText.setHint(R.string.minorId);
        editText.setId(9500 + dynamicMinorIds.size());

        dynamicMinorIds.add(editText);
        layout.addView(editText);
    }

    @Override
    protected boolean handleMenuClick(int itemId) {
        switch (itemId) {
            case R.id.add_beacon:
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addBeaconInputField();
                    }
                });
                return true;

            case R.id.save_machine:
                // save machine to database
                DatabaseHandler databaseHandler = new DatabaseHandler(activity);

                TextView editText = (TextView) activity.findViewById(R.id.editText);
                Machine machine = new Machine().setName(editText.getText().toString());
                databaseHandler.createMachine(machine);

                for(EditText minor : dynamicMinorIds){
                    int minorId = Integer.parseInt(minor.getText().toString());
                    // TODO : get beacons with all major and uuid
                    Beacon beacon = databaseHandler.getBeacon(minorId, 0, "");
                    if(beacon != null) {
                        beacon.setMachineId(machine.getId());
                        databaseHandler.updateBeacon(beacon);
                    } else {
                        beacon = new Beacon()
                                .setMinor(minorId)
                                .setMachineId(machine.getId());
                        databaseHandler.createBeacon(beacon);
                    }
                }
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void disconnectView() {
        //
    }
}
