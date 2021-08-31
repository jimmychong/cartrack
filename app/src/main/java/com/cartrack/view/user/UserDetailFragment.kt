package com.cartrack.view.user

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.cartrack.BUNDLE_USER_DATA
import com.cartrack.R
import com.cartrack.base.MVVMFragment
import com.cartrack.databinding.FragmentUserDetailBinding
import com.cartrack.model.user.UserInfo
import com.cartrack.viewmodel.user.UserDetailViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar


class UserDetailFragment : MVVMFragment<UserDetailViewModel, FragmentUserDetailBinding>(),
    OnMapReadyCallback {
    var userInfo: UserInfo? = null
    private var map: GoogleMap? = null
    private var userLocation: LatLng? = null
    private var currentLocation = MutableLiveData<LatLng>()
    private var currentMarker: Marker? = null
    private var requestPermissionSb : Snackbar ?= null
    private var permReqLuncher =  registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if(!permissions.containsValue(false)) {
            requestPermissionSb?.dismiss()
            getLocation()
        }else{
            requestPermissionSb?.show()
        }
    }

    companion object {
        fun newInstance(user: UserInfo): UserDetailFragment {
            return UserDetailFragment().apply {
                val bundle = Bundle()
                bundle.putSerializable(BUNDLE_USER_DATA, user)
                arguments = bundle
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        userInfo = (arguments?.getSerializable(BUNDLE_USER_DATA) as? UserInfo)
        userInfo?.address?.geo?.let {
            if (it.lat?.toDoubleOrNull() != null && it.lng?.toDoubleOrNull() != null) {
                userLocation = LatLng(it.lat.toDouble(), it.lng.toDouble())
            }
        }

        (activity as UserActivity).showBackButton(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.userInfo.set(userInfo)

        binding.map.onCreate(savedInstanceState)
        binding.map.getMapAsync(this)

        requestPermissionSb = Snackbar.make(
            binding.root,
            getString(R.string.label_text_request_location),
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(R.string.label_setting) {
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also {
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val uri = Uri.fromParts("package", context?.packageName, null)
                    it.data = uri
                    startActivity(it)
                }
            }


        getLocation()

    }

    override fun onResume() {
        binding.map.onResume()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.map.onDestroy()
        requestPermissionSb?.dismiss()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.map.onLowMemory()
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_user_detail
    }

    override fun getViewModelInstance(): UserDetailViewModel {
        return UserDetailViewModel()
    }

    override fun setBindingData() {
        binding.viewModel = viewModel
        binding.view = this
    }

    fun mapMarkerLocation(v: View) {
        if (v.id == R.id.btnCurrentLocation) {
            currentLocation.value?.let {
                map?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        it, 12.0f
                    )
                )
            } ?: run {
                getLocation()
            }
        } else if (v.id == R.id.btnUserLocation) {
            userLocation?.let {
                map?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        it, 12.0f
                    )
                )
            } ?: run {
                locationError()
            }
        }
    }

    private fun locationError() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("No Location")
            .setPositiveButton(R.string.button_confirm) { dialog, _ ->
            }
            .setCancelable(false)
            .show()
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0

        userLocation?.let {
            map?.addMarker(MarkerOptions().position(it).title("${userInfo?.username}"))
        }

        currentLocation.observe(this) { latlng ->
            currentMarker?.remove()
            currentMarker =
                map?.addMarker(MarkerOptions().position(latlng).title("Current"))
        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permReqLuncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        } else {
            val mFusedLocationClient =
                LocationServices.getFusedLocationProviderClient(requireContext())

            mFusedLocationClient.lastLocation.addOnCompleteListener {

                try {
                    currentLocation.value = LatLng(it.result.latitude, it.result.longitude)
                } catch (ex: Exception) {
                    Log.d("DEBUG", "ERROR $ex")
                }
            }
        }
    }



}