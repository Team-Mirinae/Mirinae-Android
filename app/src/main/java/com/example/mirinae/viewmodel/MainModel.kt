package com.example.mirinae.viewmodel

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel

class MainModel : ViewModel() {

    private val requestCode = 2000

    fun preference(context:Context, activity:Activity) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                val dialog = AlertDialog.Builder(context)
                dialog.setMessage("앱을 이용하시려면 위치 권한을 허용 해 주세요.")
                dialog.setPositiveButton("확인", DialogInterface.OnClickListener { _, _ ->
                    ActivityCompat.requestPermissions(activity,
                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), requestCode)
                })
                dialog.show()

            } else {
                ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), requestCode)
            }
        }
    }


}