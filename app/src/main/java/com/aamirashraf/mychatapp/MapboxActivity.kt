package com.aamirashraf.mychatapp

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.type.LatLng
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createCircleAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPolylineAnnotationManager
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.locationcomponent.location

class MapboxActivity : AppCompatActivity(),PermissionsListener,LocationListener{
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

        setContentView(R.layout.activity_mapbox)
//        Mapbox.getInstance(this,getString(R.string.mapbox_access_token))
        mapView = findViewById(R.id.mapView)
        map=mapView.getMapboxMap()
//        map.addOnMapClickListener { point ->
////            Toast.makeText(this, point.latitude().toString(), Toast.LENGTH_LONG).show()
//            lat=point.latitude()
//            lon=point.longitude()
//
//            false // Return false instead of true
//        }



//        Plugin.Mapbox.getInstance(applicationContext,getString(R.string.mapbox_access_token))

//        mapView?.getMapboxMap()?.loadStyleUri(Style.MAPBOX_STREETS)




//        val marker = MarkerViewOptions()
//        map.addOnMapClickListener()
        /*

         */



        mapView.getMapboxMap().loadStyleUri(
            Style.TRAFFIC_DAY,
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
            Log.d(TAG,"${lat.toString()}")
            Log.d(TAG,"${lon.toString()}")
//            addAnnotationToMap(lat,lon)
            addAnnotationToMap()
            false // Return false instead of true
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
    private fun addAnnotationToMap(latt:Double, longg:Double) {
        bitmapFromDrawableRes(
            this@MapboxActivity,
            com.mapbox.maps.R.drawable.mapbox_compass_icon
        )?.let {
            val annotationApi = mapView?.annotations
            //line annotation
           val polylineAnnotationManager = annotationApi?.createPolylineAnnotationManager(mapView)
// Define a list of geographic coordinates to be connected.
            val points = mutableListOf(
                Point.fromLngLat(longg,latt),
                Point.fromLngLat(longitude, latitude)
            )
// Set options for the resulting line layer.
            val polylineAnnotationOptions: PolylineAnnotationOptions = PolylineAnnotationOptions()
                .withPoints(points)
                // Style the line that will be added to the map.
                .withLineColor("#ee4e8b")
                .withLineWidth(5.0)
// Add the resulting line to the map.
            polylineAnnotationManager?.create(polylineAnnotationOptions)
        }
    }
    private fun addAnnotationToMap() {
        bitmapFromDrawableRes(
            this@MapboxActivity,
            com.mapbox.maps.R.drawable.mapbox_compass_icon
        )?.let {
            val annotationApi = mapView?.annotations

            val circleAnnotationManager = annotationApi?.createCircleAnnotationManager(mapView)
// Set options for the resulting circle layer.
            val circleAnnotationOptions: CircleAnnotationOptions = CircleAnnotationOptions()
                // Define a geographic coordinate.
                .withPoint(Point.fromLngLat(lon, lat))
                // Style the circle that will be added to the map.
                .withCircleRadius(30.0)
                .withCircleColor("#ee4e8b")
                .withCircleStrokeWidth(2.0)
                .withCircleStrokeColor("#ffffff")
// Add the resulting circle to the map.
            circleAnnotationManager?.create(circleAnnotationOptions)

        }
    }
    private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
        convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

    private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
        if (sourceDrawable == null) {
            return null
        }
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else {
// copying drawable object to not manipulate on the same reference
            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }

}

//private fun MapView.addOnMapClickListener(onMapClickListener: (point: Point) -> Unit) {
//
//}



