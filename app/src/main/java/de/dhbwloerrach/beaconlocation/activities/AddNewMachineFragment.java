package de.dhbwloerrach.beaconlocation.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import de.dhbwloerrach.beaconlocation.R;
import de.dhbwloerrach.beaconlocation.models.Beacon;

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
        final Button button = (Button) activity.findViewById(R.id.button_cancel);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                commons.switchFragment(ActivityCommons.FragmentType.BEACON_SEARCH);

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
