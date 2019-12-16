package com.stevenpopovich.trying_to_be_funny.ui.main.save_dialog_screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.stevenpopovich.trying_to_be_funny.R
import kotlinx.android.synthetic.main.on_finished_recording_container.*

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

        configureDialogToolbar()

        childFragmentManager
            .beginTransaction()
            .replace(R.id.on_finished_recording_container, AddBitsFragment())
            .commit()
    }

    private fun showAreYouSureDialog() {
        com.stevenpopovich.trying_to_be_funny.showAreYouSureDialog(this, fragmentManager!!, context!!)
    }

    private fun configureDialogToolbar() {
        dialog_toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp)
        dialog_toolbar.setNavigationOnClickListener {
            showAreYouSureDialog()
        }
        dialog_toolbar.title = getString(R.string.add_bits)
        dialog_toolbar.setTitleTextColor(ContextCompat.getColor(context!!, R.color.white))
    }
}

fun goBackwardsToFragment(fragmentManager: FragmentManager, fragment: Fragment) {
    val transaction = fragmentManager.beginTransaction()
    transaction.setCustomAnimations(
        R.anim.enter_from_left,
        R.anim.exit_to_right,
        R.anim.enter_from_right,
        R.anim.exit_to_left
    )

    transaction.replace(R.id.on_finished_recording_container, fragment).commit()
}

fun goForwardsToFragment(fragmentManager: FragmentManager, fragment: Fragment) {
    val transaction = fragmentManager.beginTransaction()
    transaction.setCustomAnimations(
        R.anim.enter_from_right,
        R.anim.exit_to_left,
        R.anim.enter_from_left,
        R.anim.exit_to_right
    )

    transaction.replace(R.id.on_finished_recording_container, fragment).commit()
}