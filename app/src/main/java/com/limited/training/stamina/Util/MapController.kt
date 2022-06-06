package com.limited.training.stamina.Util

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.limited.training.stamina.R
import com.limited.training.stamina.Util.room.CoordenadaDAO
import com.limited.training.stamina.objects.Coordenada
import com.limited.training.stamina.ui.record.RecordFragment
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.math.sqrt

open class MapController : Fragment(), OnMapReadyCallback {
    enum class DrawMarker{
        NO, START, END
    }
    internal var trackLocation = false
    internal var currSpeed = 0.0F
    internal var currDistance = 0.0F
    internal var startingTime: Long = 0L
    private lateinit var startBtn: Button
    private lateinit var durationTV: TextView
    private lateinit var distanceTV: TextView
    private lateinit var speedTV: TextView
    lateinit var mGoogleMap: GoogleMap
    lateinit var cordsDAO: CoordenadaDAO
    internal var mFusedLocationClient: FusedLocationProviderClient? = null
    internal var drawMarker : DrawMarker = DrawMarker.NO
    var prevLatLng: LatLng? = null
    private var mCurrentZoom: Float = -1.0F

    fun distance(x1: Location, x2: LatLng?): Double {
        if(x2 == null){
            return 0.0
        }
        //Distancia euclidea
        return sqrt((x2.latitude - x1.latitude).pow(2.0) + (x2.longitude - x1.longitude).pow(2.0))
    }

    fun updateMarker(pos: LatLng, title: String): Marker? {
        val markerOptions = MarkerOptions()
        markerOptions.title(title)
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
        markerOptions.position(pos)
        return mGoogleMap.addMarker(markerOptions)
    }

    fun updateCamera(pos: LatLng) {
        mCurrentZoom = if(mCurrentZoom < 0) STARTING_ZOOM
        else mGoogleMap.cameraPosition.zoom

        mGoogleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(pos, mCurrentZoom),
            1000,
            null
        )
    }

    internal var mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.isNotEmpty()) {
                val location = locationList.last()
                currSpeed = if(location.hasSpeed())
                    Funciones.mpsToKph(location.speed)
                else
                    0.00F
                Log.i("MapController", "Location: $location.latitude $location.longitude Speed: $currSpeed km/h")
                val currLatLng = LatLng(location.latitude, location.longitude)
                Log.i("MapController", "TrackLocation var value $trackLocation")
                if (trackLocation) {
                    if(startingTime == 0L) { //Primera iteración desde que comenzó el tracking
                        currDistance = 0.0F
                        startingTime = location.time
                    }
                    if(prevLatLng != currLatLng) {
                        val distanceResult:FloatArray = FloatArray(2)
                        Location.distanceBetween(prevLatLng!!.latitude,prevLatLng!!.longitude,currLatLng!!.latitude,currLatLng!!.longitude,distanceResult)
                        currDistance += (distanceResult[0] / 1000)
                    }
                    if(::speedTV.isInitialized)
                        speedTV.text = String.format("%.2f km/h",currSpeed)
                    if(::distanceTV.isInitialized)
                        distanceTV.text = String.format("%.4f Km", currDistance)
                    if(::durationTV.isInitialized) {
                        durationTV.text = Funciones.getTimeStringBetweenElapsedMillis(startingTime, location.time)
                    }
                    if(distance(location, prevLatLng) > 0.5 || true){
                        Log.i("MapController","passing if ")
                        lifecycleScope.launch {
                            cordsDAO.insert(Coordenada(location.longitude,location.latitude))
                            Log.i("MapController","Inserting")
                        }
                        mGoogleMap.addPolyline(
                            PolylineOptions()
                                .add(prevLatLng,currLatLng)
                                .color(context?.let { ContextCompat.getColor(it,R.color.azul_stamina) }!!)
                                .width(15.0F)
                        )
                    }
                }

                if(drawMarker != DrawMarker.NO) {
                    val title = if(drawMarker == DrawMarker.START)
                        getString(R.string.Start_location)
                    else
                        getString(R.string.End_location)

                    updateMarker(currLatLng,title)
                    drawMarker = DrawMarker.NO
                }
                updateCamera(currLatLng)
                prevLatLng = currLatLng

                if(::startBtn.isInitialized && startBtn.visibility == View.INVISIBLE) startBtn.visibility = View.VISIBLE
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        mGoogleMap.mapType = MAP_TYPE

        val mLocationRequest : LocationRequest = LocationRequest.create()
        mLocationRequest.interval = UPDATE_INTERVAL //interval in milliseconds
        mLocationRequest.fastestInterval =
            FASTEST_UPDATE_INTERVAL //interval in milliseconds
        mLocationRequest.priority = PRIORITY

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                //Location Permission already granted
                mFusedLocationClient?.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper()!!)
                mGoogleMap.isMyLocationEnabled = true
            } else {
                //Request Location Permission
                checkLocationPermission()
            }
        } else {
            mFusedLocationClient?.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper()!!)
            mGoogleMap.isMyLocationEnabled = true
        }
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.Location_services_needed_title))
                    .setMessage(getString(R.string.Location_services_needed_text))
                    .setPositiveButton(
                        getText(R.string.Accept)
                    ) { _, _ ->
                        //Prompt the user once explanation has been shown
                        requestPermissions(
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            MY_PERMISSIONS_REQUEST_LOCATION
                        )
                    }
                    .create()
                    .show()


            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {

                        val mLocationRequest : LocationRequest = LocationRequest.create()
                        mLocationRequest.interval =
                            UPDATE_INTERVAL //interval in milliseconds
                        mLocationRequest.fastestInterval =
                            FASTEST_UPDATE_INTERVAL //interval in milliseconds
                        mLocationRequest.priority = PRIORITY

                        mFusedLocationClient?.requestLocationUpdates(
                            mLocationRequest,
                            mLocationCallback,
                            Looper.myLooper()!!
                        )
                        mGoogleMap.isMyLocationEnabled = true
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(requireContext(), getText(R.string.No_location_services),
                        Toast.LENGTH_LONG).show()
                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request
    }

    internal fun setViewControls(startButton: Button, durationTextView: TextView, distanceTextView: TextView, speedTextView: TextView){
        startBtn = startButton
        durationTV = durationTextView
        distanceTV = distanceTextView
        speedTV = speedTextView
    }

    internal fun resetValues(){
        trackLocation = false
        currSpeed = 0.0F
        currDistance = 0.0F
        startingTime = 0L
        prevLatLng = null
        mCurrentZoom = -1.0F
        drawMarker = DrawMarker.NO
        mGoogleMap.clear()
    }

    companion object {
        const val MAP_TYPE = GoogleMap.MAP_TYPE_NORMAL
        const val MY_PERMISSIONS_REQUEST_LOCATION = 99
        const val UPDATE_INTERVAL = 3000L
        const val FASTEST_UPDATE_INTERVAL = 3000L
        const val PRIORITY = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        const val STARTING_ZOOM = 18.0F
    }
}