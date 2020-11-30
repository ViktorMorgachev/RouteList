package com.sedi.routelist.adapters

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.sedi.routelist.R
import com.sedi.routelist.commons.*
import com.sedi.routelist.enums.RouteType

class ItemRoadType(context: Context, viewGroup: ViewGroup, listener: ISelectedTypeListener) {

    private var tvTittle: TextView = viewGroup.findViewById(R.id.tv_route_type)
    private var llTypes: LinearLayout = viewGroup.findViewById(R.id.ll_root_types)
    private var llWalk: LinearLayout = viewGroup.findViewById<LinearLayout>(R.id.ll_walk).apply {
        setOnClickListener {
            listener.onTypeSelected(RouteType.Walking)
        }
    }
    private var llDrive: LinearLayout = viewGroup.findViewById<LinearLayout>(R.id.ll_drive).apply {
        setOnClickListener {
            listener.onTypeSelected(RouteType.Drive)
        }
    }
    private var llBicycle: LinearLayout =
        viewGroup.findViewById<LinearLayout>(R.id.ll_bicycle).apply {
            setOnClickListener {
                listener.onTypeSelected(RouteType.Bicycle)
            }
        }
    private var ivArrow: ImageView = viewGroup.findViewById<ImageView>(R.id.iv_arrow).apply {
        setOnClickListener {
            log("Arrow rotation: $rotation")
            if (rotation == 180f
            ) {
                showTypes()
            } else {
                hideTypes()
            }
        }
    }


    fun changeTittle(context: Context, routeType: RouteType) {
        tvTittle.text = context.resources.getString(routeType.textID)
        showAllElements()
        when (routeType) {
            RouteType.Walking -> llWalk.gone()
            RouteType.Drive -> llDrive.gone()
            RouteType.Bicycle -> llBicycle.gone()
        }
    }


    private fun showAllElements() {
        llWalk.visible()
        llDrive.visible()
        llBicycle.visible()
    }

    fun showTypes() {
        ivArrow.rotate180()
        llTypes.visible(500)
    }

    fun hideTypes() {
        ivArrow.rotate0()
        llTypes.gone(500)
    }


    interface ISelectedTypeListener {
        fun onTypeSelected(type: RouteType)
    }

}