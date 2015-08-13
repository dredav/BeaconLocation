package de.dhbwloerrach.beaconlocation.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
    private Menu menu;
    private MainActivity context;
    private BeaconTools beaconTools;
    private Drawer drawer;
    private BaseFragment fragment;

    private BeaconsFragment beaconsFragment;
    private MachinesFragment machinesFragment;
    private AddNewMachineFragment addNewMachineFragment;
    private AddManualMachineFragment addManualMachineFragment;
    private AddBeaconsToMachineFragment addBeaconsToMachineFragment;
    private MachineFragment machineFragment;

    public ActivityCommons(MainActivity context) {
        this.context = context;
    }

    public ActivityCommons setMenu(Menu menu) {
        this.menu = menu;

        if(fragment != null) {
            menu.clear();
            fragment.createActionBarMenu(menu);
        }

        return this;
    }

    public boolean menuHandler(MenuItem item) {
        return fragment.handleMenuClick(item.getItemId());
    }

    @Override
    public boolean onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
        switchFragment((FragmentType) iDrawerItem.getTag());
        drawer.closeDrawer();

        return true;
    }

    public void switchFragment(FragmentType type) {
        switchFragment(type, null);
    }

    public void switchFragment(FragmentType type, Bundle bundle) {
        FragmentManager fragmentManager = context.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(fragment != null) {
            fragment.disconnectView();
        }

        switch (type) {
            case BEACON_SEARCH:
                if (beaconsFragment == null) {
                    beaconsFragment = new BeaconsFragment();
                }

                fragment = beaconsFragment;
                break;

            case MACHINES_VIEW:
                if (machinesFragment == null) {
                    machinesFragment = new MachinesFragment();
                }

                fragment = machinesFragment;
                break;

            case ADD_MACHINE:
                if (addNewMachineFragment == null) {
                    addNewMachineFragment = new AddNewMachineFragment();
                }

                fragment = addNewMachineFragment;
                break;
            case ADD_MACHINE_MANUAL:
                if(addManualMachineFragment == null) {
                    addManualMachineFragment = new AddManualMachineFragment();
                }

                fragment = addManualMachineFragment;
                break;
            case ADD_BEACON_TO_MACHINE:
                if(addBeaconsToMachineFragment==null){
                    addBeaconsToMachineFragment = new AddBeaconsToMachineFragment();
                }

                fragment = addBeaconsToMachineFragment;
                break;
            case MACHINE:
                if(machineFragment==null){
                    machineFragment = new MachineFragment();
                }

                fragment = machineFragment;
                break;
        }

        if(fragment == null) {
            return;
        }

        fragment.setActivity(context);
        if(bundle != null) {
            fragment.setArguments(bundle);
        }

        if(menu != null) {
            menu.clear();
            fragment.createActionBarMenu(menu);
        }

        fragmentTransaction.replace(R.id.mainView, fragment);
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
                        new SecondaryDrawerItem().withTag(FragmentType.MACHINES_VIEW).withName(R.string.menu_machineView)
                )
                .withOnDrawerItemClickListener(this)
                .build();

        switchFragment(FragmentType.BEACON_SEARCH);
    }

    public void startMonitoring(IBeaconListView view) {
        if (beaconTools == null) {
            beaconTools = new BeaconTools(context, view);
        } else {
            beaconTools.addView(view);
        }
    }

    public void stopMonitoring(IBeaconListView view) {
        if(beaconTools == null) {
            return;
        }

        beaconTools.removeView(view);
    }

    public void unbind(){
        beaconTools.unbind();
    }

    public enum FragmentType
    {
        BEACON_SEARCH,
        MACHINES_VIEW,
        ADD_MACHINE,
        ADD_MACHINE_MANUAL,
        ADD_BEACON_TO_MACHINE,
        MACHINE,
    }
}
