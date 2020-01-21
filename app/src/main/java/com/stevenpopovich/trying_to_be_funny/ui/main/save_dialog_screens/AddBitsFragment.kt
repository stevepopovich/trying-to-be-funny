package com.stevenpopovich.trying_to_be_funny.ui.main.save_dialog_screens

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.stevenpopovich.trying_to_be_funny.R
import com.stevenpopovich.trying_to_be_funny.SetService
import com.stevenpopovich.trying_to_be_funny.SetServiceLocalSavingImpl
import com.stevenpopovich.trying_to_be_funny.hideKeyboardFrom
import kotlinx.android.synthetic.main.add_bits.*

class AddBitsFragment(
    private val setService: SetService
) : Fragment() {
    private lateinit var jokeStrings: List<String>

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

        configureViewClickListeners()

        makeSoftInputEnterButtonCloseInput()

        setUpAddBitsAutocomplete()
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

    private fun configureViewClickListeners() {
        start_adding_bits_fab_button.setOnClickListener {
            add_bits_container.visibility = View.VISIBLE
            start_adding_bits_fab_button.hide()
            next_button_on_add_bits.visibility = View.GONE
            add_bit_edit_text.requestFocus()
        }

        add_bit_button.setOnClickListener {
            addBitChipToBitsInSet()
        }

        done_adding_bits_button.setOnClickListener {
            closeInputAndShowInitialViewButtons()
        }

        next_button_on_add_bits.setOnClickListener {
            SetService.setBits = bitsInTheSetForSaving

            goForwardsToFragment(fragmentManager!!, AddLocationFragment(
                SetServiceLocalSavingImpl(SetServiceLocalSavingImpl.getStandardDatabase(context!!))
            ))
        }
    }

    private fun makeSoftInputEnterButtonCloseInput() {
        add_bit_edit_text.setOnKeyListener { _, keyCode, _ ->
            when (keyCode) {
                KeyEvent.KEYCODE_ENTER -> {
                    closeInputAndShowInitialViewButtons()
                    true
                }
                else -> false
            }
        }
    }

    private fun addBitChipToBitsInSet() {
        if (add_bit_edit_text.text.isNotEmpty()) {
            bitsInTheSetForSaving.add(add_bit_edit_text.text.toString())
            chip_group_for_bits_in_set.addView(buildBitChip())
            add_bit_edit_text.text.clear()
            next_button_on_add_bits.isEnabled = true
            updateViewStateBasedOnBitsInSet()
        }
    }

    private fun updateViewStateBasedOnBitsInSet() {
        if (bitsInTheSetForSaving.isEmpty()) showEmptyState() else showBits()
    }

    private fun showEmptyState() {
        setWhatsSoFunnyRandomText()
        add_bits_card.visibility = View.GONE
        add_bits_empty_state.visibility = View.VISIBLE
        next_button_on_add_bits.isEnabled = false
    }

    private fun showBits() {
        add_bits_card.visibility = View.VISIBLE
        add_bits_empty_state.visibility = View.GONE
        next_button_on_add_bits.isEnabled = true
    }

    private fun closeInputAndShowInitialViewButtons() {
        add_bits_container.visibility = View.GONE
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

            text = add_bit_edit_text.text.toString()
        }

        chip.setOnCloseIconClickListener {
            bitsInTheSetForSaving.remove(chip.text.toString())
            chip_group_for_bits_in_set.removeView(chip)
            updateViewStateBasedOnBitsInSet()
        }

        return chip
    }

    private fun setUpAddBitsAutocomplete() {
        setService.getAllBits().observeForever {
            val bits = it.map { it.bit }
            val adapter = ArrayAdapter(
                context!!,
                android.R.layout.simple_list_item_1,
                bits
            )
            add_bit_edit_text.setAdapter(adapter)
        }
    }
}