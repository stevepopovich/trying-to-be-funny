package com.stevenpopovich.trying_to_be_funny.ui.main.save_dialog_screens

import android.Manifest
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationServices
import com.stevenpopovich.trying_to_be_funny.R
import com.stevenpopovich.trying_to_be_funny.SetServiceLocalSavingImpl
import com.stevenpopovich.trying_to_be_funny.getNotGrantedPermissions
import com.stevenpopovich.trying_to_be_funny.isLocationEnabled
import kotlinx.android.synthetic.main.add_location.*
import java.util.*

class AddLocationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getNotGrantedPermissions(context!!, activity!!, listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET
            )
        )

        if (isLocationEnabled(context!!)) {
            LocationServices.getFusedLocationProviderClient(context!!).lastLocation.addOnCompleteListener {
                if (it.isSuccessful) {
                    val location = it.result!!
                    val geocoder = Geocoder(context!!, Locale.getDefault())

                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 10)

                    Log.v("addresses", addresses.map { it.getAddressLine(0) }.toString())
                }
            }
        }

        save_set.setOnClickListener {
            SetServiceLocalSavingImpl(context!!).saveStaticSet()
            parentFragment!!.fragmentManager!!.popBackStackImmediate()
        }
    }
}