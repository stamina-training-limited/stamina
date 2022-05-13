package com.limited.training.stamina.ui.record



import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.limited.training.stamina.R
import com.limited.training.stamina.Util.MapController
import com.limited.training.stamina.Util.room.CoordenadaDB
import com.limited.training.stamina.objects.Coordenada
import kotlinx.coroutines.launch

class RecordFragment : MapController() {
    lateinit var cordsDB: CoordenadaDB
    var mapFrag: SupportMapFragment? = null
    var startBtn: Button? = null;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_maps, container, false)
        startBtn = view?.findViewById(R.id.mapStart_btn)!!
        startBtn!!.setOnClickListener {
            toggleTracking(startBtn!!)
        }
        cordsDB = CoordenadaDB.getInstance(requireContext())!!
        cordsDAO = cordsDB.coordenadaDao()
        return view
    }

    private fun toggleStartButtonText(startBtn : Button){
        if(startBtn.text.toString() == requireContext().getText(R.string.start)) {
            startBtn.text = requireContext().getText(R.string.stop)
            lifecycleScope.launch {
                cordsDAO.deleteAll()
                Log.i("MapsActivity", cordsDAO.getAll().toString())
            }
        } else {
            startBtn.text = requireContext().getText(R.string.start)
            lifecycleScope.launch {
                Log.i("MapsActivity", cordsDAO.getAll().toString())
            }
        }

        Log.i("MapsActivity","TrackLocation var value " + trackLocation)
    }

    private fun toggleTracking(startBtn : Button){
        trackLocation = !trackLocation
        toggleStartButtonText(startBtn)
    }

    override fun onStart() {
        super.onStart()
        startBtn?.visibility = View.INVISIBLE
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mapFrag = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFrag?.getMapAsync(this)
    }

     override fun onPause() {
        super.onPause()
        mFusedLocationClient?.removeLocationUpdates(mLocationCallback)
    }

    override fun onStop() {
        super.onStop()
        CoordenadaDB.closeInstance()
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
