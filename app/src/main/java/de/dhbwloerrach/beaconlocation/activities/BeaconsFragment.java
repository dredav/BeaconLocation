package de.dhbwloerrach.beaconlocation.activities;

import android.app.AlertDialog;
import android.app.Fragment;
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
import de.dhbwloerrach.beaconlocation.models.Beacon;
import de.dhbwloerrach.beaconlocation.models.BeaconList;
import de.dhbwloerrach.beaconlocation.models.FilterTyp;

/**
 * Created by Lukas on 31.07.2015.
 */
public class BeaconsFragment extends Fragment implements IBeaconListView, IFragment{
    private BeaconAdapter adapter;
    private Boolean updatePaused = false;
    private ArrayList<Beacon> selectedBeacons = new ArrayList<>();
    private Menu actionBarMenu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_beacons, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new BeaconAdapter(this.getActivity());

        ((MainActivity) this.getActivity()).getCommons().startMonitoring(this);

        ListView listView = (ListView) this.getActivity().findViewById(R.id.listView);
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
                actionBarMenu.getItem(0).setEnabled(!updatePaused);
                actionBarMenu.getItem(1).setEnabled(updatePaused);
            }
        });
        listView.setAdapter(adapter);
    }
    public void setSortTitle() {

        MenuItem item = actionBarMenu.findItem(R.id.action_sort);
        if(adapter.getFilterTyp()== FilterTyp.Minor){
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

            this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.clearItems();
                    adapter.addItems(filteredBeacons);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        actionBarMenu = menu;
        setSortTitle();
        menu.getItem(1).setEnabled(false);
        //return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_beacon) {
            if (updatePaused == true) {
                buildDialog();
            }
            return true;
        }
        else if (id == R.id.action_sort){

            FilterTyp filterTyp;

            if(adapter.getFilterTyp()== FilterTyp.Minor){
                filterTyp = FilterTyp.RSSI;
            }

            else {
                filterTyp = FilterTyp.Minor;
            }

            adapter.setFilterTyp(filterTyp);
            setSortTitle();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

        builder.setTitle(R.string.dialog_title);

        // Add the buttons
        builder.setPositiveButton(R.string.addToMachine, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });

        builder.setNegativeButton(R.string.createNewMachine, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });

        // Set other dialog properties

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
