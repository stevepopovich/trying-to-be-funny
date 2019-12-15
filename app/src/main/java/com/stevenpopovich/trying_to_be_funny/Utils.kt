package com.stevenpopovich.trying_to_be_funny

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


fun Fragment.slideDownDismiss(fragmentManager: FragmentManager) {
    fragmentManager
        .beginTransaction()
        .setCustomAnimations(R.anim.slide_up_animation, R.anim.slide_down_animation)
        .remove(this).commit()
}