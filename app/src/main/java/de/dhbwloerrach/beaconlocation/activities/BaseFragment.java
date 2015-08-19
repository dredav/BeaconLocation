package de.dhbwloerrach.beaconlocation.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

/**
 * Created by David on 7/31/15.
 */
public abstract class BaseFragment extends Fragment {
    protected MainActivity activity;

    protected boolean initialized = false;

    protected abstract void createActionBarMenu(Menu menu);

    protected abstract boolean handleMenuClick(int itemId);

    protected abstract void disconnectView();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public BaseFragment setActivity(MainActivity activity) {
        this.activity = activity;
        return this;
    }
}
