package com.stevenpopovich.trying_to_be_funny.ui.main.save_dialog_screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.stevenpopovich.trying_to_be_funny.R
import com.stevenpopovich.trying_to_be_funny.slideDownDismiss


class OnFinishRecordingContainerFragment : DialogFragment() {
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
        toolbar.setNavigationIcon(R.drawable.ic_close)
        toolbar.setNavigationOnClickListener {
            slideDownDismiss(fragmentManager!!)
        }
        toolbar.title = getString(R.string.save_set)

        val childFragment = AddBitsFragment()

        childFragmentManager
            .beginTransaction()
            .replace(R.id.on_finished_recording_container, childFragment)
            .commit()
    }
}