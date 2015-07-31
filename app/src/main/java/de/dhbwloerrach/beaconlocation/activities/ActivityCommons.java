package de.dhbwloerrach.beaconlocation.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import de.dhbwloerrach.beaconlocation.R;
import de.dhbwloerrach.beaconlocation.bluetooth.BeaconTools;
import de.dhbwloerrach.beaconlocation.bluetooth.IBeaconListView;

/**
 * Created by Lukas on 31.07.2015.
 */
public class ActivityCommons implements Drawer.OnDrawerItemClickListener {
    private Activity context;
    private BeaconTools beaconTools;
    private Drawer drawer;
    private BeaconsFragment beaconsFragment;
    private MachinesFragment machinesFragment;
    private IFragment currentFragment;

    public ActivityCommons(Activity context){
        this.context = context;
    }

    @Override
    public boolean onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
        int identifier = iDrawerItem.getIdentifier();
        switchFragment(identifier);

        drawer.closeDrawer();
        return true;
    }

    private void switchFragment(int identifier) {
        FragmentManager fragmentManager = context.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (identifier) {
            case 0:
                if(beaconsFragment == null) {
                    beaconsFragment = new BeaconsFragment();
                    beaconsFragment.setActivity(context);
                }
                fragmentTransaction.replace(R.id.mainView, beaconsFragment);
                currentFragment = beaconsFragment;
                break;
            case 1:
                if(machinesFragment == null) {
                    machinesFragment = new MachinesFragment();
                    machinesFragment.setActivity(context);
                }
                fragmentTransaction.replace(R.id.mainView, machinesFragment);
                currentFragment = machinesFragment;
                break;
        }
        fragmentTransaction.commit();
    }

    public void createDrawer(){
        drawer = new DrawerBuilder()
                .withActivity(context)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(0).withName("Beacons").withDescription("zu Maschine kombinieren"),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withIdentifier(1).withName("Maschinen")
                )
                .withOnDrawerItemClickListener(this)
                .build();

        switchFragment(0);
    }

    public IFragment getCurrentFragment() {
        return currentFragment;
    }

    public void startMonitoring(IBeaconListView view){
        beaconTools = new BeaconTools(context, view);
    }

    public void unbind(){
        beaconTools.unbind();
    }
}
