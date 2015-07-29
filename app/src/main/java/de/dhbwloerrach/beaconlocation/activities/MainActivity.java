package de.dhbwloerrach.beaconlocation.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;

import de.dhbwloerrach.beaconlocation.models.Beacon;
import de.dhbwloerrach.beaconlocation.adapters.BeaconAdapter;
import de.dhbwloerrach.beaconlocation.models.BeaconList;
import de.dhbwloerrach.beaconlocation.bluetooth.BeaconTools;
import de.dhbwloerrach.beaconlocation.bluetooth.IBeaconListView;
import de.dhbwloerrach.beaconlocation.R;


public class MainActivity extends Activity implements IBeaconListView {
    private BeaconTools beaconTools;
    private BeaconAdapter adapter;

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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void RefreshList(final ArrayList<Beacon> beacons) {
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
