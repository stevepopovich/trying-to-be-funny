package com.stevenpopovich.trying_to_be_funny.ui.main.save_dialog_screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat.getColor
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
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp)
        toolbar.setNavigationOnClickListener {
            showAreYouSureDialog()
        }
        toolbar.title = getString(R.string.save_set)
        toolbar.setTitleTextColor(getColor(context!!, R.color.white))

        val childFragment = AddBitsFragment()

        childFragmentManager
            .beginTransaction()
            .replace(R.id.on_finished_recording_container, childFragment)
            .commit()
    }

    private fun showAreYouSureDialog() {
        val dialogBuilder = AlertDialog.Builder(context!!)

        dialogBuilder
            .setMessage(getString(R.string.saving_set_dialog_title))
            .setPositiveButton(getString(R.string.discard)) { dialog, _ ->
                dialog.cancel()
                slideDownDismiss(fragmentManager!!)

            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }
            .create()
            .show()
    }
}