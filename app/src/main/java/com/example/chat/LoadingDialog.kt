package com.example.chat

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate

class LoadingDialog(val myActivity: Activity) {
    var activity: Activity
    var dialog: AlertDialog? = null

    init {
        this.activity = myActivity
    }

    fun startLoadingDialog() {

        val builder: AlertDialog.Builder? = activity.let { AlertDialog.Builder(it) }

        val inflater: LayoutInflater = activity.layoutInflater

        builder?.setView(inflater.inflate(R.layout.custom_dialog, null))
        builder?.setCancelable(false)

        dialog = builder?.create()
        dialog?.show()
    }

    fun dismissDialog() {
        dialog?.dismiss()
    }

}