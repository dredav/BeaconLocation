package de.dhbwloerrach.beaconlocation.activities;

import android.app.Activity;
import android.app.Fragment;
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

import de.dhbwloerrach.beaconlocation.R;
import de.dhbwloerrach.beaconlocation.adapters.MachineAdapter;
import de.dhbwloerrach.beaconlocation.database.DatabaseHandler;
import de.dhbwloerrach.beaconlocation.models.Beacon;

/**
 * Created by Lukas on 31.07.2015.
 */
public class MachinesFragment extends Fragment implements IFragment {
    private Activity activity;
    private MachineAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_machines, container, false);
    }

    private boolean initialized = false;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(!initialized){
            adapter = new MachineAdapter(activity);
            adapter.addItems(new DatabaseHandler(activity).getAllMachines());
            initialized = true;
        }

        ListView listView = (ListView) activity.findViewById(R.id.listView2);
        listView.setAdapter(adapter);
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
