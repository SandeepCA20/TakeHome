package com.android.takehome.utils

import android.app.AlertDialog
import android.content.Context

fun showCustomDialog(context: Context, title: String, message: String){
    val alertDialog = AlertDialog.Builder(context)

    alertDialog.apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton("Close") { _, _ ->

        }
    }.create().show()
}