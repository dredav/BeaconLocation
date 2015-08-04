package de.dhbwloerrach.beaconlocation.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import de.dhbwloerrach.beaconlocation.R;


public class MainActivity extends Activity {
    private ActivityCommons commons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        commons = new ActivityCommons(this);
        commons.createDrawer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        commons.unbind();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        commons.setMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return commons.menuHandler(item);
    }

    public ActivityCommons getCommons() {
        return commons;
    }
}
