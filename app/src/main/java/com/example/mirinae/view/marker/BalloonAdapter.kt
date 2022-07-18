package com.example.mirinae.view.marker

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.mirinae.R
import net.daum.mf.map.api.CalloutBalloonAdapter
import net.daum.mf.map.api.MapPOIItem
import java.util.zip.Inflater

class BalloonAdapter (inflater: LayoutInflater) : CalloutBalloonAdapter{

    private val mCalloutBalloon : View = inflater.inflate(R.layout.balloon, null)
    private val title : TextView = mCalloutBalloon.findViewById(R.id.blo_title)
    private val content : TextView = mCalloutBalloon.findViewById(R.id.blo_content)

    override fun getCalloutBalloon(p0: MapPOIItem?): View {
        // 마커 클릭시
        val text = stringSplit(p0!!.itemName.split("-"))

        title.text = text[0]
        content.text = text[1]

        return mCalloutBalloon
    }

    override fun getPressedCalloutBalloon(p0: MapPOIItem?): View {
        // 말풍선 클릭시
        return mCalloutBalloon
    }

    private fun stringSplit(array : List<String>) : List<String> {
        var string = ""

        if (array[0].length > 14 && array[1].length > 20) {
            string = stringAdder(string, 14, array[0].toCharArray())
            string += "-"
            string = stringAdder(string, 20, array[1].toCharArray())

        } else if (array[0].length > 14 && array[1].length <= 20) {
            string = stringAdder(string, 14, array[0].toCharArray())
            string += "-${array[1]}"

        }else if (array[1].length > 20 && array[0].length <= 14) {
            string += "${array[0]}-"
            string = stringAdder(string, 20, array[1].toCharArray())
        }else
            return array

        return string.split("-")
    }

    private fun stringAdder (convert : String, length : Int, tmp : CharArray) : String {
        var string = convert
        Log.e("처음 길이", string.length.toString())
        for (i in tmp) {
            if (string.isNotEmpty() && string.length % length == 0)
                string += "\n"
            string += i
        }
        return string
    }
}