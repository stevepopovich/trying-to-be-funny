package com.stevenpopovich.trying_to_be_funny.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.stevenpopovich.trying_to_be_funny.R
import com.stevenpopovich.trying_to_be_funny.SetService
import kotlinx.android.synthetic.main.add_bits.*

class AddBitsFragment : Fragment() {
    private val nextFragment = AddLocationFragment()

    private val bits = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_bits, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        add_bit_button.setOnClickListener {
            val chip = Chip(parentFragment?.context)
            chip.isClickable = false
            chip.isCheckable = false
            chip.isFocusable = false
            chip.text = add_bits_input.text.toString()
            bits.add(add_bits_input.text.toString())
            chip.textStartPadding = 14f
            chip_group.addView(chip)
            add_bits_input.text.clear()
        }

        next_button.setOnClickListener {
            SetService.setJokes = bits

            val transaction = fragmentManager!!.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
            transaction.replace(R.id.on_finished_recording_container, nextFragment).commit()
        }

        cancel_button.setOnClickListener {
            // TODO ask are you sure and then close the modal
        }
    }

    private fun addBitsToService() {

    }
}