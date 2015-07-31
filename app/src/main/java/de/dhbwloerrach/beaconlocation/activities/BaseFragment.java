package de.dhbwloerrach.beaconlocation.activities;

import android.app.Fragment;
import android.view.Menu;

/**
 * Created by David on 7/31/15.
 */
public abstract class BaseFragment extends Fragment {
    protected MainActivity activity;

    protected boolean initialized = false;

    protected abstract void createActionBarMenu(Menu menu);

    protected abstract boolean handleMenuClick(int itemId);

    public BaseFragment setActivity(MainActivity activity) {
        this.activity = activity;
        return this;
    }
}
