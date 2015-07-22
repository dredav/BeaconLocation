package de.dhbwloerrach.beaconlocation;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends Activity implements IBeaconListView {
    private BeaconTools beaconTools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        beaconTools = new BeaconTools(this);
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

        final Activity testt = this;

        this.runOnUiThread(new Runnable() {
            ArrayList<Beacon> test = beacons;

            @Override
            public void run() {
                ListView listView = (ListView) findViewById(R.id.listView);
                ArrayAdapter<Beacon> adapter;
                adapter = new ArrayAdapter<Beacon>(testt, R.layout.listitem_beacon, test);
                listView.setAdapter(adapter);
            }
        });


    }
}
