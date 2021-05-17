package com.wundermobility.qatest;


import android.util.Log;
import android.widget.TextView;

import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import junit.framework.AssertionFailedError;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(AndroidJUnit4.class)
public class LoginFragmentTest {

    private List<Vehicle> vehicles = null;


    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    private MainActivity mainActivity = null;
    private MapFragmentViewModel viewModel = null;

    @Before
    public void setUp() {
        mainActivity = activityTestRule.getActivity();
        FragmentContainerView fragmentContainerView = mainActivity.findViewById(R.id.navMain);
        viewModel = new ViewModelProvider(mainActivity).get(MapFragmentViewModel.class);
        assertNotNull(fragmentContainerView);
    }

    @Test
    public void verifyPageElements(){

        onView(withId(R.id.txtLogin)).check(matches(isDisplayed()));
        onView(withId(R.id.txtLogin)).check(matches(withText(R.string.text_view_login_label)));
        onView(withId(R.id.edtLoginEmail)).check(matches(isDisplayed()));
        onView(withId(R.id.edtLoginPassword)).check(matches(isDisplayed()));
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()));
        onView(withId(R.id.btnLogin)).check(matches(withText(R.string.button_login_text)));
    }

    @Test
    public void emptyLoginCredentials() throws InterruptedException {
        onView(withId(R.id.btnLogin)).perform(click());

        waitUntil(R.id.progressBarLogin);
        onView(withId(R.id.txtLoginError)).check(matches(withText(R.string.text_view_login_invalid_credentials_error)));

    }

    @Test
    public void wrongLoginCredentials() throws InterruptedException {

        onView(withId(R.id.edtLoginEmail)).perform(typeText("qatest"), closeSoftKeyboard());
        onView(withId(R.id.edtLoginPassword)).perform(typeText("1234"), closeSoftKeyboard());
        onView(withId(R.id.btnLogin)).perform(click());

        waitUntil(R.id.progressBarLogin);

        onView(withId(R.id.txtLoginError)).check(matches(withText(R.string.text_view_login_invalid_credentials_error)));
    }

    @Test
    public void loginAndVehicleList() throws InterruptedException {

        onView(withId(R.id.edtLoginEmail)).perform(typeText("qatest@wundermobility.com"), closeSoftKeyboard());
        onView(withId(R.id.edtLoginPassword)).perform(typeText("12345678"), closeSoftKeyboard());
        onView(withId(R.id.btnLogin)).perform(click());

        //waitUntil(R.id.progressBarLogin);
        Thread.sleep(3000);
        onView(withId(R.id.progressBarLogin)).check(doesNotExist());
        onView(withId(R.id.mapView)).check(matches(isDisplayed()));
        onView(withId(R.id.btnMapNearbyVehicles)).check(matches(isDisplayed()));
        onView(withId(R.id.btnMapLocateVehicle)).check(matches(not(isEnabled())));

        onView(withId(R.id.btnMapNearbyVehicles)).perform(click());

        Thread.sleep(3000);

        assertNotNull(R.id.lstNearbyVehicles);


    }

    @Test
    public void loginAndMapInteraction() throws InterruptedException, UiObjectNotFoundException {
        onView(withId(R.id.edtLoginEmail)).perform(typeText("qatest@wundermobility.com"), closeSoftKeyboard());
        onView(withId(R.id.edtLoginPassword)).perform(typeText("12345678"), closeSoftKeyboard());
        onView(withId(R.id.btnLogin)).perform(click());

        //waitUntil(R.id.progressBarLogin);
        Thread.sleep(3000);
        onView(withId(R.id.progressBarLogin)).check(doesNotExist());
        onView(withId(R.id.mapView)).check(matches(isDisplayed()));
        onView(withId(R.id.btnMapNearbyVehicles)).check(matches(isDisplayed()));
        onView(withId(R.id.btnMapLocateVehicle)).check(matches(not(isEnabled())));

        vehicles = viewModel.getVehicles();
        int vehicleIndex = 3;

        // Get vehicle details @vehicleIndex
        locateVehicle(vehicleIndex);

        Thread.sleep(2000);

        clickSelectedVehicle();

        Thread.sleep(2000);

        clickRentVehicleAndVerify(vehicleIndex);

        onView(withId(R.id.btnCardVehicleClose)).perform(click());

        Thread.sleep(2000);

        locateVehicle(0);
        Thread.sleep(3000);
        clickSelectedVehicle();

        Thread.sleep(5000);

        goToRentedVehicleAndEndRent();
        getRidesCountAndVerifyCount(vehicleIndex);

        locateVehicle(4);
        Thread.sleep(3000);
        clickSelectedVehicle();

        Thread.sleep(3000);

        locateVehicle(2);
        Thread.sleep(3000);
        clickSelectedVehicle();

        Thread.sleep(3000);

        locateVehicle(vehicleIndex);
        Thread.sleep(2000);
        clickSelectedVehicle();

        clickRentVehicleAndVerify(vehicleIndex);

        onView(withId(R.id.btnCardVehicleClose)).perform(click());

        Thread.sleep(2000);

        locateVehicle(5);
        Thread.sleep(3000);
        clickSelectedVehicle();

        Thread.sleep(5000);

        onView(withId(R.id.btnMapLocateVehicle)).check(matches(isEnabled()));

        goToRentedVehicleAndEndRent();
        getRidesCountAndVerifyCount(vehicleIndex);


    }

    private void getRidesCountAndVerifyCount(int vehicleindex) {

        int finalRides = vehicles.get(vehicleindex).getRides();
        System.out.println("finalRides  = " + finalRides);

        String vehicleRides = getText(R.id.txtCardVehicleRides);
        System.out.println("vehicleRides  = " + vehicleRides);

        String expected = String.format( activityTestRule.getActivity().getString(R.string.button_card_vehicle_rides_text), finalRides);
        System.out.println("expected  = " + expected);

        assertEquals(expected, vehicleRides);

    }

    private void goToRentedVehicleAndEndRent() throws InterruptedException {

        onView(withId(R.id.btnCardVehicleGoToRentedVehicle)).check(matches(isDisplayed()));
        onView(withId(R.id.btnCardVehicleGoToRentedVehicle)).perform(click());

        Thread.sleep(3000);


        onView(withId(R.id.btnCardVehicleEndRent)).check(matches(isDisplayed()));
        onView(withId(R.id.btnCardVehicleEndRent)).perform(click());

        Thread.sleep(2000);

    }

    private void clickRentVehicleAndVerify(int index) throws InterruptedException {

        String vehicleName = vehicles.get(index).getName();
        String vehicleType = vehicles.get(index).getType();
        String vehicleDescription = vehicles.get(index).getDescription();
        String vehicleFuelLevel = vehicles.get(index).getFuelLevel();
        String vehiclePrice = vehicles.get(index).getPrice();

        onView(withId(R.id.layoutCardVehicleData)).check(matches(isDisplayed()));
        onView(withId(R.id.btnCardVehicleRentVehicle)).check(matches(isDisplayed()));
        onView(withId(R.id.btnCardVehicleRentVehicle)).perform(click());

        onView(withId(R.id.progressBar)).check(matches(isDisplayed()));

        Thread.sleep(2000);

        onView(withId(R.id.btnCardVehicleEndRent)).check(matches(isDisplayed()));

        onView(withId(R.id.txtCardVehicleDescription)).check(matches(isDisplayed()));
        onView(withId(R.id.txtCardVehicleType)).check(matches(isDisplayed()));
        onView(withId(R.id.txtCardVehicleName)).check(matches(isDisplayed()));
        onView(withId(R.id.txtCardVehicleFuelLevel)).check(matches(isDisplayed()));
        onView(withId(R.id.txtCardVehiclePrice)).check(matches(isDisplayed()));

        // Assertions on text

        String vehicleDescDisplayed = getText(R.id.txtCardVehicleDescription);
        assertEquals(vehicleDescription, vehicleDescDisplayed);

        String vehicleTypeDisplayed = getText(R.id.txtCardVehicleType);
        assertEquals(vehicleType, vehicleTypeDisplayed);

        String vehicleNameDisplayed = getText(R.id.txtCardVehicleName);
        assertEquals(vehicleName, vehicleNameDisplayed);

        String vehicleFuelDisplayed = getText(R.id.txtCardVehicleFuelLevel);
        assertEquals(vehicleFuelLevel, vehicleFuelDisplayed);

        String vehiclePriceDisplayed = getText(R.id.txtCardVehiclePrice);
        assertEquals(vehiclePrice, vehiclePriceDisplayed);

    }

    private void clickSelectedVehicle() throws UiObjectNotFoundException {

        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().enabled(true));
        marker.click();
    }

    private String getText(int iD) {

        TextView textView = activityTestRule.getActivity().findViewById(iD);
        textView.setText(R.string.button_login_text);

        String text = textView.getText().toString();
        return text;
    }

    private void locateVehicle(int index) {
        mainActivity.runOnUiThread(() -> {
            viewModel.locateVehicle(vehicles.get(index), MapFragmentViewModel.DEFAULT_MAP_ZOOM_LEVEL);
        });

    }

    @After
    public void tearDown() {
        mainActivity = null;

    }

    private void waitUntil(int iD) {
        try {
            while (true) {
                onView(withId(R.id.progressBarLogin)).check(matches(isDisplayed()));
            }

        }catch (AssertionFailedError error) {
            Log.d("", "");
        }
    }
}