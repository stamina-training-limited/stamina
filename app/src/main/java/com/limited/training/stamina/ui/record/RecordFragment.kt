package com.limited.training.stamina.ui.record



import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.Navigator
import com.google.android.material.snackbar.Snackbar
import com.limited.training.stamina.R
import com.limited.training.stamina.activities.ProgressActivity
import com.limited.training.stamina.databinding.FragmentRecordBinding

class RecordFragment : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val fic = LatLng(43.33372, -8.40945)
        googleMap.addMarker(MarkerOptions().position(fic).title("Estás en FIC"))
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(fic))
        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(fic, 17f),
            4000,
            null
        )
    }

    private var _binding: FragmentRecordBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRecordBinding.inflate(inflater, container, false)
        val root: View = binding.root

        checkLocationService(requireContext(), root)


        val dashboardViewModel =
            ViewModelProvider(this).get(RecordViewModel::class.java)


//        return inflater.inflate(R.layout.fragment_record, container, false)
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        val startButon: Button = binding.recordStartBtn
        startButon!!.setOnClickListener {
            val intActivityInProgress: Intent = Intent(activity, ProgressActivity::class.java)
            startActivity(intActivityInProgress)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun checkLocationService(context : Context, root : View){
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
                activity?.let {
                    Snackbar.make(
                        it.findViewById(android.R.id.content),
                        getString(R.string.No_location_services), Snackbar.LENGTH_LONG).show()
                };


            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
            }
        }

}
