package com.limited.training.stamina.ui.record



import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.limited.training.stamina.R
import com.limited.training.stamina.Util.room.CoordenadaDAO
import com.limited.training.stamina.Util.room.CoordenadaDB
import com.limited.training.stamina.objects.Coordenada
import kotlinx.coroutines.launch

//import com.limited.training.stamina.databinding.FragmentRecordBinding

class RecordFragment : Fragment(), OnMapReadyCallback {

    lateinit var coordsDB: CoordenadaDB
    lateinit var coordsDAO: CoordenadaDAO
    lateinit var mGoogleMap: GoogleMap
    var mapFrag: SupportMapFragment? = null
    lateinit var mLocationRequest: LocationRequest
    var mPreviousLocation: Location? = null
    var mCurrentLocation: Location? = null
    var mCurrentZoom: Float = -1.0F
    internal var mCurrLocationMarker: Marker? = null
    internal var mFusedLocationClient: FusedLocationProviderClient? = null


    internal var mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.isNotEmpty()) {
                //The last location in the list is the newest
                val location = locationList.last()
                Log.i("MapsActivity", "Location: " + location.latitude + " " + location.longitude)
                mCurrentLocation = location
                if(mPreviousLocation == null){
                    mPreviousLocation = location
                } //Sólo añadimos una nueva línea si se ha cambiado de posición
                if(mCurrentLocation!!.longitude != mPreviousLocation!!.longitude && mCurrentLocation!!.latitude != mPreviousLocation!!.latitude){
                    val coord:Coordenada = Coordenada(mCurrentLocation!!.longitude,mCurrentLocation!!.latitude)
                    lifecycleScope.launch { coordsDAO.insert(coord) }
                    mGoogleMap.addPolyline(
                        PolylineOptions()
                            .add(
                                LatLng(mPreviousLocation!!.latitude, mPreviousLocation!!.longitude),
                                LatLng(mCurrentLocation!!.latitude, mCurrentLocation!!.longitude)
                            )
                            .color(context?.let { ContextCompat.getColor(it,R.color.azul_stamina) }!!)
                            .width(15.0F)
                    )
                    var coords: List<Coordenada>
                    lifecycleScope.launch { coords = coordsDAO.getAll() }
                    mPreviousLocation = mCurrentLocation
                }
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker?.remove()
                }

                //Place current location marker
                val latLng = LatLng(location.latitude, location.longitude)
                val markerOptions = MarkerOptions()
                markerOptions.position(latLng)
                markerOptions.title(getString(R.string.Current_location))
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions)

                //move map camera
                //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11.0F))
                mCurrentZoom = if(mCurrentZoom < 0) STARTING_ZOOM
                else mGoogleMap.cameraPosition.zoom

                mGoogleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(latLng, mCurrentZoom),
                    1000,
                    null
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        coordsDB = CoordenadaDB.getInstance(requireContext())!!
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        //checkLocationPermission()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        coordsDAO = coordsDB.coordenadaDao()
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onStart() {
        super.onStart()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mapFrag = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFrag?.getMapAsync(this)
    }

     override fun onPause() {
        super.onPause()

        //stop location updates when Activity is no longer active
        mFusedLocationClient?.removeLocationUpdates(mLocationCallback)
         coordsDB.close()
    }

    override fun onMapReady (googleMap: GoogleMap) {
        mGoogleMap = googleMap
        mGoogleMap.mapType = GoogleMap.MAP_TYPE_NORMAL


        val mLocationRequest : LocationRequest = LocationRequest.create()
        mLocationRequest.interval = UPDATE_INTERVAL //interval in milliseconds
        mLocationRequest.fastestInterval = FASTEST_UPDATE_INTERVAL //interval in milliseconds
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

    //private var _binding: FragmentRecordBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    //private val binding get() = _binding!!

    //override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    //    super.onViewCreated(view, savedInstanceState)
    //    val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
    //    mapFragment?.getMapAsync(callback)
    //
    //    val startButon: Button = binding.recordStartBtn
    //    startButon!!.setOnClickListener {
    //        val intActivityInProgress: Intent = Intent(activity, ProgressActivity::class.java)
    //        startActivity(intActivityInProgress)
    //    }
    //}

    //override fun onDestroyView() {
    //    super.onDestroyView()
    //    _binding = null
    //}


    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
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
                // No explanation needed, we can request the permission.
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
                        mLocationRequest.interval = UPDATE_INTERVAL //interval in milliseconds
                        mLocationRequest.fastestInterval = FASTEST_UPDATE_INTERVAL //interval in milliseconds
                        mLocationRequest.priority = PRIORITY

                        mFusedLocationClient?.requestLocationUpdates(
                            mLocationRequest,
                            mLocationCallback,
                            Looper.myLooper()!!
                        )
                        mGoogleMap.setMyLocationEnabled(true)
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

    companion object {
        const val MY_PERMISSIONS_REQUEST_LOCATION = 99
        const val UPDATE_INTERVAL = 2500L
        const val FASTEST_UPDATE_INTERVAL = 2500L
        const val PRIORITY = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        const val STARTING_ZOOM = 16.0F
    }
}
