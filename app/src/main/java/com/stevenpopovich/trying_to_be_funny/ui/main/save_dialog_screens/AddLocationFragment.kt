package com.stevenpopovich.trying_to_be_funny.ui.main.save_dialog_screens

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.stevenpopovich.trying_to_be_funny.*
import kotlinx.android.synthetic.main.add_location.*

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

        //dialog_toolbar.title = getString(R.string.add_a_location)

        setUpLocationAutoCompleteFragment()

        save_set.setOnClickListener {
            SetServiceLocalSavingImpl(context!!).saveStaticSet()
            (parentFragment as DialogFragment).slideDownDismiss(fragmentManager!!)
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
                Log.i(TAG, "An error occurred: $status")
            }
        })
    }
}