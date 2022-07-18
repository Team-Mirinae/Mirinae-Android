package com.example.mirinae.view


import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import com.example.mirinae.R
import com.example.mirinae.databinding.ActivityMainBinding
import com.example.mirinae.viewmodel.MainModel
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.mirinae.view.marker.BalloonAdapter
import net.daum.mf.map.api.MapPOIItem

class MainActivity : AppCompatActivity(), MapView.MapViewEventListener {

    private lateinit var bind : ActivityMainBinding
    private val model : MainModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val mapView = MapView(this)

        bind.lifecycleOwner = this
        bind.model = model

        // 권한 체크
        if (checkLocationService())
            model.preference(applicationContext, this)

        // 현재 위치 추적
        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving
        // 현재 위치를 기준으로 원 그리기 (5km)
        mapView.setMapViewEventListener(this)
        mapView.setCalloutBalloonAdapter(BalloonAdapter(layoutInflater))
        bind.Map.addView(mapView)
//        model.refresh()

    }

    override fun onMapViewInitialized(p0: MapView?) {

    }

    override fun onMapViewSingleTapped(mapView : MapView?, mPoint: MapPoint?) {
        Log.e("Touch","터치 입력됨")

        val point = MapPoint.mapPointWithGeoCoord(mPoint!!.mapPointGeoCoord.latitude, mPoint.mapPointGeoCoord.longitude)

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog)
        dialog.show()

        val accept : TextView = dialog.findViewById(R.id.acp)
        val cancel : TextView = dialog.findViewById(R.id.cancel)

        accept.setOnClickListener {
            val title = dialog.findViewById<TextView>(R.id.restaurant_title).text.toString()
            val content = dialog.findViewById<TextView>(R.id.restaurant_content).text.toString()

            val marker = MapPOIItem()
            marker.itemName = "${title}-${content}"
            marker.mapPoint = point
            marker.markerType = MapPOIItem.MarkerType.CustomImage
            marker.customImageResourceId = R.drawable.marker

            mapView!!.addPOIItem(marker)

            // model.saveRestaurant(title, content, point.mapPointGeoCoord.latitude, point.mapPointGeoCoord.longitude)
            dialog.dismiss()
        }
        cancel.setOnClickListener{ dialog.dismiss() }
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 2000) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"권한이 승인되었습니다.",Toast.LENGTH_SHORT).show()
        }
    }
}