package com.stevenpopovich.trying_to_be_funny.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stevenpopovich.trying_to_be_funny.SetService
import com.stevenpopovich.trying_to_be_funny.SetServiceLocalSavingImpl
import kotlinx.android.synthetic.main.add_location.*


class AddLocationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.stevenpopovich.trying_to_be_funny.R.layout.add_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        places_autocomplete.setOnPlaceSelectedListener {
            SetService.setLocation = it
            save_set.isEnabled = true
        }

        save_set.setOnClickListener { SetServiceLocalSavingImpl(context!!).saveStaticSet() }
    }
}