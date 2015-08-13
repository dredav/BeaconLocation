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

        if(!initialized) {
            adapter = new BeaconAdapter(activity);
            initialized = true;
        }

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

                menu.getItem(0).setEnabled(!updatePaused);
                menu.getItem(1).setEnabled(updatePaused);
            }
        });

        listView.setAdapter(adapter);
        listView.setEmptyView(activity.findViewById(R.id.emptyList_beacons));
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
        this.menu = menu;

        activity.getMenuInflater().inflate(R.menu.menu_main, menu);
        setSortTitle();

        menu.getItem(1).setEnabled(false);
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
                RssiAverageType rssiAverageType;
                switch (adapter.getRssiAverageType()) {
                    case None:
                        rssiAverageType = RssiAverageType.Average;
                        menu.getItem(3).setTitle("RSSI Avg.");
                        break;
                    case Average:
                        rssiAverageType = RssiAverageType.SmoothedAverage;
                        menu.getItem(3).setTitle("RSSI Adv.");
                        break;
                    case SmoothedAverage:
                        rssiAverageType = RssiAverageType.None;
                        menu.getItem(3).setTitle("RSSI");
                        break;
                    default:
                        return false;
                }
                adapter.setRssiAverageType(rssiAverageType);
                break;
            default:
                return false;
        }

        return true;
    }

    @Override
    protected void disconnectView() {
        activity.getCommons().stopMonitoring(this);
    }
}
