package com.sedi.routelist.presenters

import android.content.Context
import android.location.Location
import android.os.Looper
import com.huawei.hms.location.*
import com.sedi.routelist.contracts.ILocation

object LocationPresenter : ILocation {


    private lateinit var settingsClient: SettingsClient
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var mLocationRequest: LocationRequest? = null
    private lateinit var mLocationCallback: LocationCallback
    private lateinit var currentLocation: Location

    override fun initLocation(context: Context) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        settingsClient = LocationServices.getSettingsClient(context)
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 10000
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val locations =
                    locationResult.locations
                if (locations.isNotEmpty()) {
                    for (location in locations) {
                        currentLocation = location
                    }
                }
            }

            override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                val flag = locationAvailability.isLocationAvailable
            }
        }
    }

    override fun requestLocationUpdate() {
        fusedLocationProviderClient?.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.getMainLooper()
        )?.addOnSuccessListener {
            //Processing when the API call is successful.
        }
            ?.addOnFailureListener {
                //Processing when the API call fails.
            }
    }

    override fun requestRemoveLocationUpdate() {
        fusedLocationProviderClient!!.removeLocationUpdates(mLocationCallback)
            .addOnSuccessListener {
                //Location updates are stopped successfully.
            }
            .addOnFailureListener {
                //Failed to stop location updates.
            }
    }

    override fun getLastKnownLocation(): Location? {
        var lastLocation : Location? = null
        val task =
            fusedLocationProviderClient!!.lastLocation
                .addOnSuccessListener { location ->
                    lastLocation = location
                    return@addOnSuccessListener
                }
                .addOnFailureListener {
                    return@addOnFailureListener
                }
        return lastLocation
    }

}