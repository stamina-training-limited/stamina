package com.limited.training.stamina.Util

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
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
import java.lang.Math.*

open class MapController : Fragment(), OnMapReadyCallback {
    enum class DrawMarker{
        NO, START, END
    }
    internal var trackLocation = false
    lateinit var mGoogleMap: GoogleMap
    lateinit var cordsDAO: CoordenadaDAO
    internal var mFusedLocationClient: FusedLocationProviderClient? = null
    internal var drawMarker : DrawMarker = DrawMarker.NO
    var prevLoc: LatLng? = null
    var mCurrentZoom: Float = -1.0F

    fun distance(x1: Location, x2: LatLng?): Double {
        if(x2 == null){
            return 0.0
        }
        //Distancia euclidea
        return sqrt( pow(x2.latitude-x1.latitude, 2.0) + pow(x2.longitude-x1.longitude, 2.0) )
    }

    fun updateMarker(pos: LatLng, title: String): Marker? {
        val markerOptions = MarkerOptions()
        markerOptions.title(title)
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
        markerOptions.position(pos)
        return mGoogleMap.addMarker(markerOptions)
    }

    fun updateCamera(pos: LatLng) {
        mCurrentZoom = if(mCurrentZoom < 0) RecordFragment.STARTING_ZOOM
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
                Log.i("MapController", "Location: " + location.latitude + " " + location.longitude)
                val currLoc = LatLng(location.latitude, location.longitude)
                Log.i("MapController","TrackLocation var value " + trackLocation)
                if (trackLocation) {
                    if(prevLoc == null){
                        prevLoc = currLoc
                    }
                    if(distance(location, prevLoc) > 0.5 || true){
                        Log.i("MapController","passing if ")
                        lifecycleScope.launch {
                            cordsDAO.insert(Coordenada(location.longitude,location.latitude))
                            Log.i("MapController","Inserting")
                        }
                        mGoogleMap.addPolyline(
                            PolylineOptions()
                                .add(prevLoc,currLoc)
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

                    updateMarker(currLoc,title)
                    drawMarker = DrawMarker.NO
                }
                updateCamera(currLoc)

                prevLoc = currLoc

                val startButton : Button = view?.findViewById<Button>(R.id.mapStart_btn)!!
                if(startButton.visibility == View.INVISIBLE) startButton.visibility = View.VISIBLE
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        mGoogleMap.mapType = RecordFragment.MAP_TYPE

        val mLocationRequest : LocationRequest = LocationRequest.create()
        mLocationRequest.interval = RecordFragment.UPDATE_INTERVAL //interval in milliseconds
        mLocationRequest.fastestInterval =
            RecordFragment.FASTEST_UPDATE_INTERVAL //interval in milliseconds
        mLocationRequest.priority = RecordFragment.PRIORITY

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
                            RecordFragment.MY_PERMISSIONS_REQUEST_LOCATION
                        )
                    }
                    .create()
                    .show()


            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    RecordFragment.MY_PERMISSIONS_REQUEST_LOCATION
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
            RecordFragment.MY_PERMISSIONS_REQUEST_LOCATION -> {
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
                            RecordFragment.UPDATE_INTERVAL //interval in milliseconds
                        mLocationRequest.fastestInterval =
                            RecordFragment.FASTEST_UPDATE_INTERVAL //interval in milliseconds
                        mLocationRequest.priority = RecordFragment.PRIORITY

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
}