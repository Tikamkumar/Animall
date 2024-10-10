package com.online.animall.presentation.dialog

import android.app.Dialog
import android.content.Context
import com.online.animall.R

class LoadingDialog(private val context: Context) {

    val dialog = Dialog(context).apply {
        setContentView(R.layout.loading_dialog_layout)
        setCancelable(false)
    }

    fun show() {
        if(!dialog.isShowing) dialog.show()
    }

    fun hide() {
        if(dialog.isShowing) dialog.cancel()
    }

}