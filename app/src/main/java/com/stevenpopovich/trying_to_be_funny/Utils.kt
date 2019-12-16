package com.stevenpopovich.trying_to_be_funny

import android.app.Activity
import android.content.Context
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun hideKeyboardFrom(context: Context, view: View) {
    val inputMethodManager: InputMethodManager =
        context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun showKeyboardFrom(context: Context, view: View) {
    val inputMethodManager: InputMethodManager =
        context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun setUpFragmentBackButtonAction(mainFragmentView: View, action: () -> Any) {
    mainFragmentView.isFocusableInTouchMode = true
    mainFragmentView.requestFocus()
    mainFragmentView.setOnKeyListener { _, keyCode, _ ->
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            action()

            true
        } else false
    }
}

fun showAreYouSureDialog(fragment: Fragment, fragmentManager: FragmentManager, context: Context) {
    val dialogBuilder = AlertDialog.Builder(context)

    dialogBuilder
        .setMessage(fragment.getString(R.string.saving_set_dialog_title))
        .setPositiveButton(fragment.getString(R.string.discard)) { dialog, _ ->
            dialog.cancel()
            fragmentManager.popBackStackImmediate()
        }
        .setNegativeButton(fragment.getString(R.string.cancel)) { dialog, _ ->
            dialog.cancel()
        }
        .create()
        .show()
}