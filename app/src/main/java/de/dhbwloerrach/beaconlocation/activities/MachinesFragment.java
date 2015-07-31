package de.dhbwloerrach.beaconlocation.activities;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import de.dhbwloerrach.beaconlocation.R;

/**
 * Created by Lukas on 31.07.2015.
 */
public class MachinesFragment extends Fragment implements IFragment {
    private Activity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_machines, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public IFragment setActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    @Override
    public void initializeFragment() {

    }
}
