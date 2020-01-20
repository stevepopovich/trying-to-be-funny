package com.stevenpopovich.trying_to_be_funny

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

fun hideKeyboardFrom(context: Context, view: View) {
    val inputMethodManager: InputMethodManager =
        context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun askForNotGrantedPermissions(context: Context, activity: Activity, permissionsToGet: List<String>) {
    val permissionsNotGranted = mutableListOf<String>()

    permissionsToGet.forEach {
        if (ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED)
            permissionsNotGranted.add(it)
    }

    if (permissionsNotGranted.isNotEmpty())
        ActivityCompat.requestPermissions(activity, permissionsNotGranted.toTypedArray() , 0)
}

fun hasAllPermissions(context: Context, vararg permissions: String): Boolean {
    permissions.forEach {
        if (ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED)
            return false
    }

    return true
}