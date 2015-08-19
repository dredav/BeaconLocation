package de.dhbwloerrach.beaconlocation.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import de.dhbwloerrach.beaconlocation.R;
import de.dhbwloerrach.beaconlocation.adapters.BeaconAdapter;
import de.dhbwloerrach.beaconlocation.bluetooth.IBeaconListView;
import de.dhbwloerrach.beaconlocation.database.DatabaseHandler;
import de.dhbwloerrach.beaconlocation.models.Beacon;
import de.dhbwloerrach.beaconlocation.models.BeaconList;
import de.dhbwloerrach.beaconlocation.models.Delegate;
import de.dhbwloerrach.beaconlocation.models.FilterTyp;
import de.dhbwloerrach.beaconlocation.models.Machine;
import de.dhbwloerrach.beaconlocation.models.RssiAverageType;

/**
 * Created by Lukas on 31.07.2015.
 */
public class BeaconsFragment extends AddMachineBaseFragment implements IBeaconListView {
    protected BeaconAdapter adapter;
    protected Boolean updatePaused = false;
    protected ArrayList<Beacon> selectedBeacons = new ArrayList<>();
    protected Menu menu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_beacons, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialize();

        setRSSIMode(RssiAverageType.None);

        activity.getCommons().startMonitoring(this);

        ListView listView = (ListView) activity.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updatePaused=true;
                ListView listView = (ListView) parent;
                Beacon beacon = (Beacon) listView.getAdapter().getItem(position);
                if (selectedBeacons.contains(beacon)){
                    selectedBeacons.remove(beacon);
                    view.setBackgroundColor(Color.TRANSPARENT);
                    if (selectedBeacons.isEmpty()){
                        updatePaused=false;
                    }
                }
                else{
                    selectedBeacons.add(beacon);
                    view.setBackgroundColor(0xFF8db6cd);
                }

                menu.findItem(R.id.action_sort).setEnabled(!updatePaused);
                menu.findItem(R.id.add_beacon).setEnabled(updatePaused);
            }
        });

        listView.setAdapter(adapter);
        listView.setEmptyView(activity.findViewById(R.id.emptyList_beacons));
    }

    private void initialize() {
        if(!initialized) {
            adapter = new BeaconAdapter(activity);
            initialized = true;
        }
    }

    public void setSortTitle() {
        MenuItem item = menu.findItem(R.id.action_sort);
        if (adapter.getFilterTyp()== FilterTyp.Minor) {
            item.setTitle(R.string.rssi);
        } else {
            item.setTitle(R.string.minor);
        }
    }

    @Override
    public void RefreshList(final ArrayList<Beacon> beacons) {
        if (!updatePaused) {
            BeaconList beaconList = new BeaconList();
            beaconList.addAll(beacons);
            final BeaconList filteredBeacons = beaconList.filterByLast(5);

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.clearItems();
                    adapter.addItems(filteredBeacons);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    public void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.dialog_title);

        builder.setPositiveButton(R.string.addToMachine, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("selectedBeacons", selectedBeacons);
                activity.getCommons().switchFragment(ActivityCommons.FragmentType.ADD_BEACON_TO_MACHINE, bundle);

            }
        });

        builder.setNegativeButton(R.string.createNewMachine, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("selectedBeacons", selectedBeacons);
                activity.getCommons().switchFragment(ActivityCommons.FragmentType.ADD_MACHINE, bundle);

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    protected void createActionBarMenu(Menu menu) {
        initialize();
        this.menu = menu;

        activity.getMenuInflater().inflate(R.menu.menu_main, menu);
        setSortTitle();

        menu.findItem(R.id.add_beacon).setEnabled(false);
    }

    @Override
    protected boolean handleMenuClick(int itemId) {
        switch (itemId) {
            case R.id.add_beacon:
                Machine machine = null;
                if(getArguments() != null)
                    machine = getArguments().getParcelable("oldMachine");
                if (updatePaused) {
                    if (machine != null) {
                        final DatabaseHandler databaseHandler = new DatabaseHandler(activity);

                        final int machineId = machine.getId();
                        final Delegate action = new Delegate() {
                            @Override
                            public void execute() {
                                insertBeacons(databaseHandler, selectedBeacons, machineId);
                                activity.getCommons().switchFragment(ActivityCommons.FragmentType.MACHINES_VIEW);
                            }
                        };

                        if(checkBeacons(selectedBeacons, databaseHandler, action)){
                            action.execute();
                        }
                    }
                    else {
                        buildDialog();
                    }
                }
                break;

            case R.id.action_sort:
                FilterTyp filterTyp;
                if (adapter.getFilterTyp() == FilterTyp.Minor) {
                    filterTyp = FilterTyp.RSSI;
                } else {
                    filterTyp = FilterTyp.Minor;
                }

                adapter.setFilterTyp(filterTyp);
                setSortTitle();
                break;

            case R.id.rssi_average:
                switch (adapter.getRssiAverageType()) {
                    case None:
                        setRSSIMode(RssiAverageType.Average);
                        break;
                    case Average:
                        setRSSIMode(RssiAverageType.SmoothedAverage);
                        break;
                    case SmoothedAverage:
                        setRSSIMode(RssiAverageType.None);
                        break;
                    default:
                        return false;
                }

                break;
            default:
                return false;
        }

        return true;
    }

    protected void setRSSIMode(RssiAverageType rssiAverageType){
        switch (rssiAverageType) {
            case None:
                menu.findItem(R.id.rssi_average).setTitle("Mode: RSSI Default");
                break;
            case Average:
                menu.findItem(R.id.rssi_average).setTitle("Mode: RSSI Average");
                break;
            case SmoothedAverage:
                menu.findItem(R.id.rssi_average).setTitle("Mode: RSSI Advanced");
                break;
            default:
        }
        adapter.setRssiAverageType(rssiAverageType);
    }

    @Override
    protected void disconnectView() {
        activity.getCommons().stopMonitoring(this);
        selectedBeacons = new ArrayList<>();
    }
}
