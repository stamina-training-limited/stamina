package com.limited.training.stamina.ui.record



import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.limited.training.stamina.R
import com.limited.training.stamina.Util.MapController
import com.limited.training.stamina.Util.room.CoordenadaDB
import kotlinx.coroutines.launch

class RecordFragment : MapController() {
    lateinit var cordsDB: CoordenadaDB
    var mapFrag: SupportMapFragment? = null
    var startBtn: Button? = null
    var stopBtn: Button? = null
    var resumeBtn: Button? = null
    var finishBtn: Button? = null
    var progressDurationTv: TextView? = null
    var progressDistanceTv: TextView? = null
    var progressSpeedTv: TextView? = null
    var recordingLayout: ConstraintLayout? = null
    var recordingStopLayout: ConstraintLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_maps, container, false)
        recordingLayout = view?.findViewById(R.id.recording_layout)!!
        recordingStopLayout = view?.findViewById(R.id.recording_stop_layout)
        startBtn = view?.findViewById(R.id.mapStart_btn)!!
        stopBtn = view?.findViewById(R.id.routeProgressStop_btn)!!
        resumeBtn = view?.findViewById(R.id.routeProgressResume_btn)!!
        finishBtn = view?.findViewById(R.id.routeProgressFinish_btn)!!
        progressDurationTv = view?.findViewById(R.id.routeProgressDurationTime_tv)
        progressDistanceTv = view?.findViewById(R.id.routeProgressDistanceKm_tv)
        progressSpeedTv = view?.findViewById(R.id.routeProgressSpeedKm_tv)
        setViewControls(startBtn!!, progressDurationTv!!, progressDistanceTv!!, progressSpeedTv!!)

        startBtn!!.setOnClickListener {
            startRecording(startBtn!!, recordingLayout!!)
        }

        stopBtn!!.setOnClickListener{
            stopRecording(recordingLayout!!, recordingStopLayout!!)
        }

        resumeBtn!!.setOnClickListener{
            resumeRecording(recordingStopLayout!!, recordingLayout!!)
        }

        finishBtn!!.setOnClickListener{
            finishRecording(recordingStopLayout!!, startBtn!!)
        }

        cordsDB = CoordenadaDB.getInstance(requireContext())!!
        cordsDAO = cordsDB.coordenadaDao()
        return view
    }

    private fun startRecording(startBtn : Button, recodingLy: ConstraintLayout){
        trackLocation = true
        drawMarker = DrawMarker.START
        startBtn.visibility = View.GONE
        recodingLy.visibility = View.VISIBLE
        lifecycleScope.launch {
            Log.i("MapsActivity", cordsDAO.getAll().toString())
        }
    }

    private fun stopRecording(recodingLy: ConstraintLayout, recodingStopLy: ConstraintLayout){
        trackLocation = false
        recodingLy.visibility = View.GONE
        recodingStopLy.visibility = View.VISIBLE
    }

    private fun resumeRecording(recodingStopLy: ConstraintLayout, recodingLy: ConstraintLayout){
        trackLocation = true
        recodingStopLy.visibility = View.GONE
        recodingLy.visibility = View.VISIBLE
        lifecycleScope.launch {
            Log.i("MapsActivity", cordsDAO.getAll().toString())
        }
    }

    private fun finishRecording(recodingStopLy: ConstraintLayout, startBtn : Button){
        drawMarker = DrawMarker.END
        resetValues()
        recodingStopLy.visibility = View.GONE
        startBtn.visibility = View.VISIBLE

        lifecycleScope.launch {
            Log.i("MapsActivity", cordsDAO.getAll().toString())
            cordsDAO.deleteAll()
        }

        Toast.makeText(activity, "Se ha finalizado la actividad", Toast.LENGTH_SHORT).show()
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
}
