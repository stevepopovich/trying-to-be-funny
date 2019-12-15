package com.stevenpopovich.trying_to_be_funny

import android.app.Activity
import android.content.Context
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
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

fun setUpFragmentBackButtonAction(mainFragmentView: View, fragmentManager: FragmentManager, action: () -> Any) {
    mainFragmentView.isFocusableInTouchMode = true
    mainFragmentView.requestFocus()
    mainFragmentView.setOnKeyListener { _, keyCode, _ ->
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            action()

            true
        } else false
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