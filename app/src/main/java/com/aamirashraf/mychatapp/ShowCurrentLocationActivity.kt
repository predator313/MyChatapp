package com.aamirashraf.mychatapp


import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.locationcomponent.location


class ShowCurrentLocationActivity : AppCompatActivity(),PermissionsListener,LocationListener {
    private lateinit var mapView: MapView
    private lateinit var map: MapboxMap
    private lateinit var permissionManager:PermissionsManager
    private lateinit var originLocation:Location
    private var locationEngine:LocationEngine?=null
    private lateinit var locationManager: LocationManager
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private  var latitude:Double=28.0
    private var longitude:Double=77.0
    private var lat:Double=0.0
    private var lon:Double=0.0

    //may be coroutines help here to draw the lines successfully
    private var TAG="MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_show_current_location)
//        Mapbox.getInstance(this,getString(R.string.mapbox_access_token))
        mapView = findViewById(R.id.mapView)
        map=mapView.getMapboxMap()
////
//        val intent=Intent(this,MapboxActivity::class.java)
//        intent.putExtra("lat",originLocation.latitude)
//        startActivity(intent)



        mapView.getMapboxMap().loadStyleUri(
            Style.SATELLITE_STREETS,
            // After the style is loaded, initialize the Location component.
            object : Style.OnStyleLoaded {
                override fun onStyleLoaded(style: Style) {
                    mapView.location.updateSettings {
                        enabled = true
                        pulsingEnabled = true
//                        addAnnotationToMap()
                    }
//                    addAnnotationToMap()
                }
            }
        )


        map.addOnMapClickListener { point ->
//            Toast.makeText(this, point.latitude().toString(), Toast.LENGTH_LONG).show()
            lat=point.latitude()
            lon=point.longitude()
//            Point
//            Log.e(TAG,"${lat.toString()}")
//            Log.e(TAG,"${lon.toString()}")
//            val intent=Intent(this,MapboxActivity::class.java)
            Log.d(TAG,"hello")
            Log.d(TAG,"hi")
//            val extras = Bundle()
//            extras.putDouble("lat", lat)
//            extras.putDouble("lon", lon)
//            intent.putExtras(extras)
//            startActivity(intent)
//            intent.putExtra("lat",lat)
//            intent.putExtra("lon",lon)
//            addAnnotationToMap(lat,lon)
            addAnnotationToMap()
//            Intent(this,MainActivity::class.java).also {
//                it.putExtra("lat",lat)
//                it.putExtra("lon",lon)
//                startActivity(it)
//            }
            false // Return true for only one clickable instance
        }
        // Get the system location service
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager


        // Check for location permission
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Request location updates

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0.toFloat(),
                this
            )
        } else {
            // Request location permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }

    }



    override fun onStart() {
        super.onStart()
        mapView?.onStart()

    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }


    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        TODO("Not yet implemented")
    }

    override fun onPermissionResult(granted: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onLocationChanged(location: Location) {
        var latitudee = location.latitude
        var longitudee = location.longitude


        // Do something with the latitude and longitude values
//        Log.d(TAG, "Latitude: $latitude, Longitude: $longitude")
        latitude=latitudee
        longitude=longitudee
    }
    //
    private fun addAnnotationToMap() {
//
        Toast.makeText(this,"$lon $lat",Toast.LENGTH_SHORT).show()

    }

}
