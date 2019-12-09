package com.stevenpopovich.trying_to_be_funny.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.stevenpopovich.trying_to_be_funny.R


class OnFinishRecordingFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.on_finished_recording_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<Toolbar>(R.id.dialog_toolbar)
        toolbar.setNavigationIcon(R.drawable.common_google_signin_btn_text_light_normal);
        toolbar.setNavigationOnClickListener {
            dismiss()
        }
        toolbar.setTitle(R.string.save_set)

        val childFragment = WouldYouLikeToSaveFragment()
        val transaction = childFragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_left,
            R.anim.enter_from_left,
            R.anim.exit_to_right
        )

        transaction.replace(R.id.on_finished_recording_container, childFragment).commit()
    }
}