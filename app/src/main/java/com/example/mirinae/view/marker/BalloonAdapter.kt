package com.example.mirinae.view.marker

import android.view.View
import net.daum.mf.map.api.CalloutBalloonAdapter
import net.daum.mf.map.api.MapPOIItem
import java.util.zip.Inflater

class BalloonAdapter (inflater: Inflater) : CalloutBalloonAdapter{
    override fun getCalloutBalloon(p0: MapPOIItem?): View {
        // 마커 클릭시
        TODO()
    }

    override fun getPressedCalloutBalloon(p0: MapPOIItem?): View {
        // 말풍선 클릭시
        TODO()
    }
}