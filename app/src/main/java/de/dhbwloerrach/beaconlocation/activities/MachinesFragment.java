package de.dhbwloerrach.beaconlocation.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import de.dhbwloerrach.beaconlocation.R;
import de.dhbwloerrach.beaconlocation.adapters.MachineAdapter;
import de.dhbwloerrach.beaconlocation.database.DatabaseHandler;
import de.dhbwloerrach.beaconlocation.models.Machine;

/**
 * Created by Lukas on 31.07.2015.
 */
public class MachinesFragment extends BaseFragment {
    private MachineAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_machines, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!initialized) {
            adapter = new MachineAdapter(activity);
            initialized = true;
        }

        adapter.clearItems();
        adapter.addItems(new DatabaseHandler(activity).getAllMachines());

        final ListView listView = (ListView) activity.findViewById(R.id.listView2);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // go to view #5
                Bundle bundle = new Bundle();
                bundle.putParcelable("machine", (Machine)listView.getItemAtPosition(position));
                // got to AddBeaconsToMachineFragement if selected Beacons notEmpty?
                activity.getCommons().switchFragment(ActivityCommons.FragmentType.MACHINE, bundle);
            }
        });

        listView.setAdapter(adapter);
        listView.setEmptyView(activity.findViewById(R.id.emptyList_machines));
    }

    @Override
    protected void createActionBarMenu(Menu menu) {
        activity.getMenuInflater().inflate(R.menu.menu_machines, menu);
    }

    @Override
    protected boolean handleMenuClick(int itemId) {
        switch (itemId) {
            case R.id.add_machine:
                buildDialog();
                break;

            default:
                return false;
        }

        return true;
    }

    public void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.dialog_titleMachine);

        builder.setPositiveButton(R.string.addMachineManual, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                activity.getCommons().switchFragment(ActivityCommons.FragmentType.ADD_MACHINE_MANUAL);

            }
        });

        builder.setNegativeButton(R.string.addMachineWithScan, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                activity.getCommons().switchFragment(ActivityCommons.FragmentType.BEACON_SEARCH);

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void disconnectView() {
        //
    }
}
