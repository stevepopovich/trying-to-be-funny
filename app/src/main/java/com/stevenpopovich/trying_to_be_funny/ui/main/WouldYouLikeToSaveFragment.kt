package com.stevenpopovich.trying_to_be_funny.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.stevenpopovich.trying_to_be_funny.R
import kotlinx.android.synthetic.main.do_you_want_to_save.*

class WouldYouLikeToSaveFragment : Fragment() {
    private val nextFragment = AddBitsFragment()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.do_you_want_to_save, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        yes_I_want_to_save.setOnClickListener {
            transitionToNextFragment()
        }

        no_I_dont_want_to_save.setOnClickListener {
            (parentFragment as DialogFragment).dismiss()
        }
    }

    private fun transitionToNextFragment() {
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