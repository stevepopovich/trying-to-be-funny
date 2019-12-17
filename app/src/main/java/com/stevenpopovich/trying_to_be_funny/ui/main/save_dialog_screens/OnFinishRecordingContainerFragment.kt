package com.stevenpopovich.trying_to_be_funny.ui.main.save_dialog_screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.stevenpopovich.trying_to_be_funny.R
import com.stevenpopovich.trying_to_be_funny.SetService
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

    override fun onDestroy() {
        super.onDestroy()
        SetService.clearStaticSet()
    }

    private fun configureDialogToolbar() {
        dialog_toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp)
        dialog_toolbar.setNavigationOnClickListener {
            showAreYouSureDialog(this, fragmentManager!!, context!!)
        }
        dialog_toolbar.title = getString(R.string.save_set)
        dialog_toolbar.setTitleTextColor(ContextCompat.getColor(context!!, R.color.white))
    }
}

fun showAreYouSureDialog(fragment: Fragment, fragmentManager: FragmentManager, context: Context) {
    val dialogBuilder = AlertDialog.Builder(context)

    dialogBuilder
        .setMessage(fragment.getString(R.string.saving_set_dialog_title))
        .setPositiveButton(fragment.getString(R.string.discard)) { dialog, _ ->
            dialog.cancel()
            SetService.clearStaticSet()
            fragmentManager.popBackStackImmediate()
        }
        .setNegativeButton(fragment.getString(R.string.cancel)) { dialog, _ ->
            dialog.cancel()
        }
        .create()
        .show()
}

fun goForwardsToFragment(fragmentManager: FragmentManager, fragment: Fragment) {
    val transaction = fragmentManager.beginTransaction()
    transaction.setCustomAnimations(
        R.anim.enter_from_right,
        R.anim.exit_to_left,
        R.anim.enter_from_left,
        R.anim.exit_to_right
    )

    transaction
        .replace(R.id.on_finished_recording_container, fragment)
        .commit()
}