package com.stevenpopovich.trying_to_be_funny.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.add_bits.*




class AddBitsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.stevenpopovich.trying_to_be_funny.R.layout.add_bits, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        add_bit_button.setOnClickListener {
            val chip = Chip(parentFragment?.context)
            chip.isClickable = false
            chip.isCheckable = false
            chip.isFocusable = false
            chip.text = add_bits_input.text.toString()
            chip.textStartPadding = 14f
            chip_group.addView(chip)
            add_bits_input.text.clear()
        }

    }
}