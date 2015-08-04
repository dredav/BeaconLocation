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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import de.dhbwloerrach.beaconlocation.R;
import de.dhbwloerrach.beaconlocation.adapters.MachineAdapter;
import de.dhbwloerrach.beaconlocation.database.DatabaseHandler;
import de.dhbwloerrach.beaconlocation.models.Beacon;
import de.dhbwloerrach.beaconlocation.models.Machine;

/**
 * Created by Lukas on 04.08.2015.
 */
public class AddManualMachineFragment extends BaseFragment {
    ArrayList<TextView> minors = new ArrayList<>();

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
            case R.id.add_beacon:
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayout layout = (LinearLayout)activity.findViewById(R.id.minors);
                        TextView textView = new TextView(activity);
                        minors.add(textView);
                        layout.addView(textView);
                        layout.setWillNotDraw(false);
                        layout.invalidate();
                    }
                });
                return true;
            case R.id.save_machine:
                // save machine to database
                DatabaseHandler databaseHandler = new DatabaseHandler(activity);

                TextView editText = (TextView)getActivity().findViewById(R.id.editText);
                Machine machine = new Machine().setName(editText.getText().toString());
                databaseHandler.createMachine(machine);

                for(TextView minor : minors){
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
}
