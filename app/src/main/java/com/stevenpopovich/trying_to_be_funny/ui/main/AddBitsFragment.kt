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

    private val bitsInTheSetForSaving = mutableListOf<String>()

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
            bitsInTheSetForSaving.add(add_bits_input.text.toString())
            chip_group_for_bits_in_set.addView(buildChip())
            add_bits_input.text.clear()
            next_button_on_add_bits.isEnabled = true
        }

        next_button_on_add_bits.setOnClickListener {
            SetService.setbits = bitsInTheSetForSaving

            val transaction = fragmentManager!!.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )

            transaction.replace(R.id.on_finished_recording_container, nextFragment).commit()
        }
    }

    private fun buildChip(): Chip {
        val chip = Chip(parentFragment?.context)
        chip.isClickable = false
        chip.isCheckable = false
        chip.isFocusable = false
        chip.text = add_bits_input.text.toString()
        chip.textStartPadding = 14f
        chip.isCloseIconVisible = true

        chip.setOnCloseIconClickListener{
            bitsInTheSetForSaving.remove(chip.text.toString())
            chip_group_for_bits_in_set.removeView(chip)
        }

        return chip
    }
}