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
    private AddNewMachineFragement addNewMachineFragement;

    public ActivityCommons(Activity context){
        this.context = context;
    }

    @Override
    public boolean onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
        switchFragment((FragmentType) iDrawerItem.getTag());

        drawer.closeDrawer();
        return true;
    }

    public void switchFragment(FragmentType type) {
        FragmentManager fragmentManager = context.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (type) {
            case BEACON_SEARCH:
                if (beaconsFragment == null) {
                    beaconsFragment = new BeaconsFragment();
                    beaconsFragment.setActivity(context);
                }

                fragmentTransaction.replace(R.id.mainView, beaconsFragment);
                currentFragment = beaconsFragment;
                break;

            case MACHINE_VIEW:
                if (machinesFragment == null) {
                    machinesFragment = new MachinesFragment();
                    machinesFragment.setActivity(context);
                }

                fragmentTransaction.replace(R.id.mainView, machinesFragment);
                currentFragment = machinesFragment;
                break;

            case ADD_MACHINE:
                if (addNewMachineFragement == null) {
                    addNewMachineFragement = new AddNewMachineFragement();
                    addNewMachineFragement.setActivity(context);
                }

                fragmentTransaction.replace(R.id.mainView, addNewMachineFragement);
                currentFragment = addNewMachineFragement;
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
                        new PrimaryDrawerItem().withTag(FragmentType.BEACON_SEARCH).withName(R.string.menu_beaconView).withDescription(R.string.menu_beaconViewDescription),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withTag(FragmentType.MACHINE_VIEW).withName(R.string.menu_machineView)
                )
                .withOnDrawerItemClickListener(this)
                .build();

        switchFragment(FragmentType.BEACON_SEARCH);
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

    public enum FragmentType
    {
        BEACON_SEARCH,
        MACHINE_VIEW,
        ADD_MACHINE,
    }
}
