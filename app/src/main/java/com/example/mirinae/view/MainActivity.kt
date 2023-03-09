package com.example.mirinae.view


import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.mirinae.R
import com.example.mirinae.databinding.ActivityMainBinding
import com.example.mirinae.module.User
import com.example.mirinae.module.data.request.SaveRestaurantReq
import com.example.mirinae.view.marker.BalloonAdapter
import com.example.mirinae.view.marker.MarkerEventListener
import com.example.mirinae.viewmodel.MainModel
import kotlinx.coroutines.*
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MainActivity : AppCompatActivity(), MapView.MapViewEventListener, MapView.CurrentLocationEventListener {

    private lateinit var bind : ActivityMainBinding
    private val model : MainModel by viewModels()
    private val eventListener = MarkerEventListener(this)
    private lateinit var mapView : MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main)
        bind.lifecycleOwner = this
        bind.model = model
        mapView = MapView(this)

        if (checkLocationService())
            model.preference(applicationContext, this)

        model.refresh()

        val location = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val userNowLocation = location.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        val center = MapPoint.mapPointWithGeoCoord(userNowLocation!!.latitude, userNowLocation.longitude)

        mapView.setMapCenterPoint(center, true)
        mapView.setMapViewEventListener(this)

        mapView.setCalloutBalloonAdapter(BalloonAdapter(layoutInflater))
        mapView.setPOIItemEventListener(eventListener)

        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving

        bind.Map.addView(mapView)

        observe()
    }

    override fun onMapViewInitialized(p0: MapView?) {}

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

            val save = SaveRestaurantReq(title, content, point.mapPointGeoCoord.latitude, point.mapPointGeoCoord.longitude)
            model.saveRestaurant(save)
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

    override fun onCurrentLocationUpdate(p0: MapView?, p1: MapPoint?, p2: Float) {

    }

    override fun onCurrentLocationDeviceHeadingUpdate(p0: MapView?, p1: Float) {}
    override fun onCurrentLocationUpdateFailed(p0: MapView?) {}
    override fun onCurrentLocationUpdateCancelled(p0: MapView?) {}

    fun observe () {
        model.markers.observe(this, Observer {
            for(i in it) {
                val marker = MapPOIItem()
                marker.itemName = i.title + "-" + i.content
                marker.mapPoint = MapPoint.mapPointWithGeoCoord(i.coordinate.latitude, i.coordinate.longitude)
                marker.markerType = MapPOIItem.MarkerType.CustomImage
                marker.customImageResourceId = R.drawable.marker

                mapView.addPOIItem(marker)
            }
        })
    }
}
