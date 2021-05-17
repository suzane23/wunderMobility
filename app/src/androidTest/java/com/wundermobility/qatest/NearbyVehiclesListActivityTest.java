package com.wundermobility.qatest;


import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class NearbyVehiclesListActivityTest {

    @Rule
    public ActivityTestRule<NearbyVehiclesListActivity> activityTestRule = new ActivityTestRule<NearbyVehiclesListActivity>(NearbyVehiclesListActivity.class);
    private NearbyVehiclesListActivity nearbyVehiclesListActivity = null;

    @Before
    public void setUp() throws InterruptedException {
        nearbyVehiclesListActivity = activityTestRule.getActivity();

    }

    @Test
    public void test() throws InterruptedException {
        Thread.sleep(5000);

        List<Vehicle> vehicleList = NearbyVehiclesListActivity.Companion.getVehiclesList();

        int vehicleIndex;
        int max = vehicleList.size() -1 , min = 0;
        vehicleIndex = (int) ((Math.random() * (max - min)));


        onView(withId(R.id.lstNearbyVehicles)).perform(RecyclerViewActions.actionOnItemAtPosition(vehicleIndex,click()));

        Thread.sleep(5000);

        VehicleDetailsDialogFragment vehicleDetailsDialogFragment = null;

        List<Fragment> fragmentList = nearbyVehiclesListActivity.getSupportFragmentManager().getFragments();

        for (int i = 0 ; i < fragmentList.size() ; ++i) {
            if (fragmentList.get(i) instanceof VehicleDetailsDialogFragment) {
                vehicleDetailsDialogFragment = (VehicleDetailsDialogFragment) fragmentList.get(i);
            }
        }

        assertNotNull(vehicleDetailsDialogFragment);

        String type = getText(vehicleDetailsDialogFragment, R.id.txtNearbyVehicleListItemType);
        String name = getText(vehicleDetailsDialogFragment, R.id.txtNearbyVehicleListItemName);
        String desc = getText(vehicleDetailsDialogFragment, R.id.txtNearbyVehicleListItemDescription);
        String price = getText(vehicleDetailsDialogFragment, R.id.txtNearbyVehicleListItemPrice);
        String fuel = getText(vehicleDetailsDialogFragment, R.id.txtNearbyVehicleListItemFuelLevel);

        assertEquals(vehicleList.get(vehicleIndex).getType(), type);
        assertEquals(vehicleList.get(vehicleIndex).getName(), name);
        assertEquals(vehicleList.get(vehicleIndex).getDescription(), desc);
        assertEquals(vehicleList.get(vehicleIndex).getPrice(), price);
        assertEquals(vehicleList.get(vehicleIndex).getFuelLevel(), fuel);

    }

    @After
    public void tearDown() {
        nearbyVehiclesListActivity = null;

    }

    private String getText(VehicleDetailsDialogFragment vehicleDetailsDialogFragment, int iD) {

        TextView textView = vehicleDetailsDialogFragment.getView().findViewById(iD);
        String text = textView.getText().toString();
        return text;
    }



}

