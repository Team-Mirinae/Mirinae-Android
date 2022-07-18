package com.example.mirinae.view.marker

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.Window
import android.widget.TextView
import com.example.mirinae.R
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MarkerEventListener (val context : Context) : MapView.POIItemEventListener {
    private val items = arrayOf("마커 수정", "마커 삭제")

    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {}
    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {}
    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {}

    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?, p2: MapPOIItem.CalloutBalloonButtonType?) {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle(p1!!.itemName.split("-")[0])
        dialog.setItems(items) { dia, i ->
            val log = Dialog(context)
            log.requestWindowFeature(Window.FEATURE_NO_TITLE)
            if (i == 0) {
                log.setContentView(R.layout.dialog_edit)
                log.show()

                val acp = log.findViewById<TextView>(R.id.edit)
                val cancel = log.findViewById<TextView>(R.id.edit_cancel)


                acp.setOnClickListener {
                    val editedTitle = log.findViewById<TextView>(R.id.edit_restaurant_title).text.toString()
                    val editedContent = log.findViewById<TextView>(R.id.edit_restaurant_content).text.toString()

                    val item = MapPOIItem()
                    item.itemName = "${editedTitle}-${editedContent}"
                    item.markerType = MapPOIItem.MarkerType.CustomImage
                    item.customImageResourceId = R.drawable.marker
                    item.mapPoint = MapPoint.mapPointWithGeoCoord(p1.mapPoint.mapPointGeoCoord.latitude, p1.mapPoint.mapPointGeoCoord.longitude)

                    p0!!.removePOIItem(p1)
                    p0.addPOIItem(item)
                    log.dismiss()
                    dia.dismiss()
                }
                cancel.setOnClickListener {
                    log.dismiss()
                    dia.dismiss()
                }
            } else if (i == 1) {
                log.setContentView(R.layout.dialog_delete)
                log.show()

                val yes = log.findViewById<TextView>(R.id.yes)
                val no = log.findViewById<TextView>(R.id.no)

                yes.setOnClickListener {
                    p0!!.removePOIItem(p1!!)
                    log.dismiss()
                    dia.dismiss()
                }

                no.setOnClickListener {
                    log.dismiss()
                    dia.dismiss()
                }
            }
        }.show()
    }
}