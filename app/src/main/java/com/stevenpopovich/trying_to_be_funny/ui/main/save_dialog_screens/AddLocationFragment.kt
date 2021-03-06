package com.stevenpopovich.trying_to_be_funny.ui.main.save_dialog_screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.stevenpopovich.trying_to_be_funny.R
import com.stevenpopovich.trying_to_be_funny.SetService
import com.stevenpopovich.trying_to_be_funny.toPlace
import kotlinx.android.synthetic.main.add_location.*
import kotlinx.coroutines.runBlocking

class AddLocationFragment(
    private val setService: SetService
) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpLocationAutoCompleteFragment()

        save_set.setOnClickListener {
            runBlocking {
                setService.saveStaticSetAsync().await()
            }
            parentFragment!!.fragmentManager!!.popBackStackImmediate()
        }
    }

    private fun setUpLocationAutoCompleteFragment() {
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.places_autocomplete) as AutocompleteSupportFragment

        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                SetService.setLocation = place.toPlace()
                save_set.isEnabled = (place.toPlace() != null)
            }

            override fun onError(status: Status) {
                if (status.statusCode != Status.RESULT_CANCELED.statusCode)
                    throw Exception("Autocomplete error occurred: $status")
            }
        })
    }
}