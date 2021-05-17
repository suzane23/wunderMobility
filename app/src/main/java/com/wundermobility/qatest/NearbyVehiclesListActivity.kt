package com.wundermobility.qatest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_nearby_vehicles_list.*

class NearbyVehiclesListActivity : AppCompatActivity() {
    private val vehicles = mutableListOf<Vehicle>()

    companion object {
        val vehiclesList = arrayListOf(
            Vehicle(
                id = 1L,
                name = "KickScooter D1",
                description = "Fastest KickScooter available in the market.",
                position = LatLng(51.514656, 7.467411),
                type = "Kick Scooter",
                fuelLevel = "23%",
                price = "1.45$ / min",
                image = R.drawable.ic_blue_motorcycle
            ),
            Vehicle(
                id = 2L,
                name = "Electric Bike B1",
                description = "Amazing Electric Bike!",
                position = LatLng(51.508818, 7.450705),
                type = "Electric Bike",
                fuelLevel = "84%",
                price = "0.75$ / hour",
                image = R.drawable.ic_orange_bike
            ),
            Vehicle(
                id = 3L,
                name = "Electric Car C1",
                description = "Power is there from the beginning!",
                position = LatLng(51.510760, 7.464505),
                type = "Electric Car",
                fuelLevel = "100%",
                price = "127.50 / day",
                image = R.drawable.ic_purple_car
            ),
            Vehicle(
                id = 4L,
                name = "Electric Bike B4",
                description = "Go green with this e-bike.",
                position = LatLng(51.530465, 7.446047),
                type = "Electric Bike",
                fuelLevel = "46%",
                price = "0.55$ / hour",
                image = R.drawable.ic_orange_bike
            ),
            Vehicle(
                id = 5L,
                name = "Electric Car C5 SPORT",
                description = "Electric super sport car!",
                position = LatLng(51.495430, 7.477099),
                type = "Electric Car",
                fuelLevel = "100%",
                price = "1275.00 / day",
                image = R.drawable.ic_purple_car
            ),
            Vehicle(
                id = 6L,
                name = "KickScooter D6",
                description = "Most comfortable KickScooter available in the market.",
                position = LatLng(51.493049, 7.451923),
                type = "Kick Scooter",
                fuelLevel = "34%",
                price = "6.43$ / min",
                image = R.drawable.ic_blue_motorcycle
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nearby_vehicles_list)

        vehicles.addAll(vehiclesList)

//        intent.getParcelableArrayListExtra<Vehicle>(MapFragment.NEARBY_VEHICLES_LIST)?.let {
//            vehicles.addAll(it)
//        }

        lstNearbyVehicles.layoutManager = LinearLayoutManager(this)
        lstNearbyVehicles.adapter = NearbyVehiclesListAdapter(vehicles) { vehicle ->
            VehicleDetailsDialogFragment().show(supportFragmentManager, vehicle)
        }
    }
}