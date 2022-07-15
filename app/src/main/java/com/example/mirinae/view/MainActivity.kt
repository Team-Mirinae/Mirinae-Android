package com.example.mirinae.view


import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mirinae.R
import com.example.mirinae.databinding.ActivityMainBinding
import com.example.mirinae.viewmodel.MainModel
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil

class MainActivity : AppCompatActivity(), MapView.MapViewEventListener {

    private lateinit var bind : ActivityMainBinding

    private val model : MainModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val mapView = MapView(this)
        val map = bind.Map


        bind.lifecycleOwner = this
        bind.model = model

        // 권한 체크
        if (!checkLocationService())
            model.preference(applicationContext, this)

        // 현재 위치 추적
        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading
        // 현재 위치를 기준으로 원 그리기 (5km)
        mapView.setCurrentLocationRadius(5000)

        map.addView(mapView)
    }

    override fun onMapViewInitialized(p0: MapView?) {
        TODO("현재 위치로 이동 및 설정")


    }

    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {
        TODO("마커 생성 및 메모 창")
    }

    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {}
    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {}

    private fun checkLocationService(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 2000) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"권한이 승인되었습니다.",Toast.LENGTH_SHORT).show()
        }
    }
}