package com.example.maomakis.ui.view.main

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.maomakis.R
import com.example.maomakis.databinding.FragmentMapPickerBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.MapEventsOverlay
import java.util.Locale

class MapPickerFragment : Fragment() {

    private var _binding: FragmentMapPickerBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapView: MapView
    private var currentMarker: Marker? = null
    private var selectedAddress: String? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val fine = result[Manifest.permission.ACCESS_FINE_LOCATION] == true
            val coarse = result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            if (fine || coarse) {
                centerOnMyLocation()
            } else {
                Toast.makeText(requireContext(), "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // OSMDroid requiere user agent
        val prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(requireContext())
        Configuration.getInstance().load(requireContext(), prefs)
        Configuration.getInstance().userAgentValue = requireContext().packageName

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        mapView = binding.osmdroidMap
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)
        mapView.controller.setZoom(12.0)
        val lima = GeoPoint(-12.0464, -77.0428)
        mapView.controller.setCenter(lima)

        // Overlay para capturar taps
        val eventsReceiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                p?.let { setMarkerAndAddress(it) }
                return true
            }
            override fun longPressHelper(p: GeoPoint?): Boolean = false
        }
        mapView.overlays.add(MapEventsOverlay(eventsReceiver))

        binding.confirmButton.setOnClickListener {
            val addr = selectedAddress
            if (addr.isNullOrBlank()) {
                Toast.makeText(requireContext(), "Selecciona una ubicación", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val result = Bundle().apply { putString("address", addr) }
            setFragmentResult("map_pick_result", result)
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        if (hasLocationPermission()) {
            centerOnMyLocation()
        } else {
            requestLocationPermission()
        }
    }

    private fun setMarkerAndAddress(point: GeoPoint) {
        currentMarker?.let { mapView.overlays.remove(it) }
        currentMarker = Marker(mapView).apply {
            position = point
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            title = "Ubicación seleccionada"
        }
        mapView.overlays.add(currentMarker)
        mapView.invalidate()

        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses = try { geocoder.getFromLocation(point.latitude, point.longitude, 1) } catch (e: Exception) { emptyList() }
        selectedAddress = if (!addresses.isNullOrEmpty()) {
            val a = addresses[0]
            listOfNotNull(a.thoroughfare, a.subThoroughfare, a.locality, a.adminArea, a.countryName)
                .joinToString(", ")
        } else {
            "${point.latitude}, ${point.longitude}"
        }
        binding.addressPreview.text = selectedAddress
    }

    private fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    private fun requestLocationPermission() {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun centerOnMyLocation() {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val here = GeoPoint(it.latitude, it.longitude)
                    mapView.controller.setZoom(16.0)
                    mapView.controller.setCenter(here)
                    setMarkerAndAddress(here)
                }
            }
        } catch (_: SecurityException) { /* ignored */ }
    }

    override fun onResume() {
        super.onResume()
        binding.osmdroidMap.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.osmdroidMap.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
