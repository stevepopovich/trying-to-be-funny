package com.stevenpopovich.trying_to_be_funny.ui.main.save_dialog_screens

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.stevenpopovich.trying_to_be_funny.R
import com.stevenpopovich.trying_to_be_funny.SetService
import com.stevenpopovich.trying_to_be_funny.hideKeyboardFrom
import com.stevenpopovich.trying_to_be_funny.showKeyboardFrom
import kotlinx.android.synthetic.main.add_bits.*

class AddBitsFragment : Fragment() {
    private lateinit var jokeStrings: List<String>

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

        setUpJokeStringsForEmptyState()

        setWhatsSoFunnyRandomText()

        configureViewClickListeners(view)

        makeSoftInputEnterButtonCloseInput()
    }

    private fun setUpJokeStringsForEmptyState() {
        jokeStrings = listOf(
            getString(R.string.whats_so_funny),
            getString(R.string.whats_the_deal_with_airline_food),
            getString(R.string.funny_how),
            getString(R.string.maybe_that_hecklers_right),
            getString(R.string.i_dont_think_it_went_so_bad)
        )
    }

    private fun setWhatsSoFunnyRandomText() {
        whats_funny_text_view.text = jokeStrings.random()
    }

    private fun configureViewClickListeners(view: View) {
        start_adding_bits_fab_button.setOnClickListener {
            add_bits_input_container.visibility = View.VISIBLE
            start_adding_bits_fab_button.hide()
            next_button_on_add_bits.visibility = View.GONE
            showKeyboardFrom(context!!, view)
            add_bits_input.requestFocus()
        }

        add_bit_button.setOnClickListener {
            addBitChipToBitsInSet()
        }

        done_adding_bits_button.setOnClickListener {
            closeInputAndShowInitialViewButtons()
        }

        next_button_on_add_bits.setOnClickListener {
            SetService.setBits = bitsInTheSetForSaving

            moveToAddLocationScreen()
        }
    }

    private fun makeSoftInputEnterButtonCloseInput() {
        add_bits_input.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER)
                closeInputAndShowInitialViewButtons()

            false
        }
    }

    private fun addBitChipToBitsInSet() {
        if (add_bits_input.text.isNotEmpty()) {
            bitsInTheSetForSaving.add(add_bits_input.text.toString())
            chip_group_for_bits_in_set.addView(buildBitChip())
            add_bits_input.text.clear()
            next_button_on_add_bits.isEnabled = true
            updateViewStateBasedOnBitsInSet()
        }
    }

    private fun updateViewStateBasedOnBitsInSet() {
        if (bitsInTheSetForSaving.isEmpty()) {
            setWhatsSoFunnyRandomText()
            add_bits_empty_state.visibility = View.VISIBLE
            next_button_on_add_bits.isEnabled = false
        } else {
            add_bits_empty_state.visibility = View.GONE
            next_button_on_add_bits.isEnabled = true
        }
    }

    private fun closeInputAndShowInitialViewButtons() {
        add_bits_input_container.visibility = View.GONE
        start_adding_bits_fab_button.show()
        next_button_on_add_bits.visibility = View.VISIBLE
        hideKeyboardFrom(context!!, view!!)
    }

    private fun buildBitChip(): Chip {
        val chip = Chip(parentFragment?.context).apply {
            isClickable = false
            isCheckable = false
            isFocusable = false
            textSize = 18f
            textStartPadding = 14f
            isCloseIconVisible = true

            text = add_bits_input.text.toString()
        }

        chip.setOnCloseIconClickListener {
            bitsInTheSetForSaving.remove(chip.text.toString())
            chip_group_for_bits_in_set.removeView(chip)
            updateViewStateBasedOnBitsInSet()
        }

        return chip
    }

    private fun moveToAddLocationScreen() {
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