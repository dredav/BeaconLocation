package de.dhbwloerrach.beaconlocation.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

import de.dhbwloerrach.beaconlocation.models.Beacon;
import de.dhbwloerrach.beaconlocation.adapters.BeaconAdapter;
import de.dhbwloerrach.beaconlocation.models.BeaconList;
import de.dhbwloerrach.beaconlocation.bluetooth.BeaconTools;
import de.dhbwloerrach.beaconlocation.bluetooth.IBeaconListView;
import de.dhbwloerrach.beaconlocation.R;
import de.dhbwloerrach.beaconlocation.models.FilterTyp;


public class MainActivity extends Activity implements IBeaconListView {
    private BeaconTools beaconTools;
    private BeaconAdapter adapter;
    private Boolean updatePaused = false;
    private ArrayList<Beacon> selectedBeacons = new ArrayList<>();
    private Menu actionBarMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Test").withDescription("aaaa"),
                        new SecondaryDrawerItem().withName("asdf"),
                        new DividerDrawerItem()
                )
                .build();

        beaconTools = new BeaconTools(this);
        adapter = new BeaconAdapter(this);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updatePaused = true;
                ListView listView = (ListView) parent;
                Beacon beacon = (Beacon) listView.getAdapter().getItem(position);
                if (selectedBeacons.contains(beacon)) {
                    selectedBeacons.remove(beacon);
                    view.setBackgroundColor(Color.TRANSPARENT);
                    if (selectedBeacons.isEmpty()) {
                        updatePaused = false;
                    }
                } else {
                    selectedBeacons.add(beacon);
                    view.setBackgroundColor(0xFF8db6cd);
                }
                actionBarMenu.getItem(0).setEnabled(!updatePaused);
                actionBarMenu.getItem(1).setEnabled(updatePaused);
            }
        });
        listView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconTools.unbind();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

            this.runOnUiThread(new Runnable() {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

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
