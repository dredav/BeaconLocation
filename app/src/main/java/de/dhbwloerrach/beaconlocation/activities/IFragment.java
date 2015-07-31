package de.dhbwloerrach.beaconlocation.activities;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;


/**
 * Created by Lukas on 31.07.2015.
 */
public interface IFragment {
    boolean onCreateOptionsMenu(Menu menu);

    boolean onOptionsItemSelected(MenuItem item);

    IFragment setActivity(Activity activity);

    void initializeFragment();
}
