package com.stevenpopovich.trying_to_be_funny.ui.main.save_dialog_screens

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.stevenpopovich.trying_to_be_funny.*
import io.reactivex.disposables.CompositeDisposable
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

        jokeStrings = listOf(
            getString(R.string.whats_so_funny),
            getString(R.string.whats_the_deal_with_airline_food),
            getString(R.string.funny_how),
            getString(R.string.maybe_that_hecklers_right),
            getString(R.string.i_dont_think_it_went_so_bad)
        )

        updateViewState()

        add_bits_fab.setOnClickListener {
            bits_input_field.visibility = View.VISIBLE
            add_bits_fab.hide()
            next_button_on_add_bits.visibility = View.GONE
            showKeyboardFrom(context!!, view)
            add_bits_input.requestFocus()
        }

        add_bits_input.setOnFocusChangeListener { view, isFocused ->
            if (!isFocused) {
                bits_input_field.visibility = View.GONE
                add_bits_fab.show()
                next_button_on_add_bits.visibility = View.VISIBLE
            }
        }

        add_bit_button.setOnClickListener {
            addBitChip()
        }

        done_adding_bits_button.setOnClickListener {
            doneAddingBits()
        }

        add_bits_input.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER)
                addBitChip()

            true
        }

        next_button_on_add_bits.setOnClickListener {
            SetService.setBits = bitsInTheSetForSaving

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

    private fun addBitChip() {
        if (add_bits_input.text.isNotEmpty()) {
            bitsInTheSetForSaving.add(add_bits_input.text.toString())
            chip_group_for_bits_in_set.addView(buildChip())
            add_bits_input.text.clear()
            next_button_on_add_bits.isEnabled = true
            updateViewState()
        }
    }

    private fun buildChip(): Chip {
        val chip = Chip(parentFragment?.context)
        chip.isClickable = false
        chip.isCheckable = false
        chip.isFocusable = false
        chip.textSize = 18f
        chip.text = add_bits_input.text.toString()
        chip.textStartPadding = 14f
        chip.isCloseIconVisible = true

        chip.setOnCloseIconClickListener {
            bitsInTheSetForSaving.remove(chip.text.toString())
            chip_group_for_bits_in_set.removeView(chip)
            updateViewState()
        }

        return chip
    }

    private fun doneAddingBits() {
        bits_input_field.visibility = View.GONE
        add_bits_fab.show()
        next_button_on_add_bits.visibility = View.VISIBLE
        hideKeyboardFrom(context!!, view!!)
    }

    private fun updateViewState() {
        if (bitsInTheSetForSaving.isEmpty()) {
            whats_funny_text_view.text = jokeStrings.random()
            add_bits_empty_state.visibility = View.VISIBLE
            next_button_on_add_bits.isEnabled = false
        } else {
            add_bits_empty_state.visibility = View.GONE
            next_button_on_add_bits.isEnabled = true
        }
    }
}