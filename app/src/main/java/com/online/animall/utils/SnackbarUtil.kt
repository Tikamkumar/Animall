package com.online.animall.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

object SnackbarUtil {

    fun success(view: View, message: String = "Progress Successfully...") {
       Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    fun error(view: View, message: String = "Something went wrong...") {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }
}