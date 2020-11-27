package com.sedi.routelist.adapters

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.sedi.routelist.R
import com.sedi.routelist.commons.gone
import com.sedi.routelist.commons.rotate0
import com.sedi.routelist.commons.rotate180
import com.sedi.routelist.commons.visible
import com.sedi.routelist.enums.RouteType

class ItemRoadType(context: Context, viewGroup: ViewGroup, listener: ISelectedTypeListener) {

    private var tvTittle: TextView = viewGroup.findViewById(R.id.tv_route_type)
    private var llWalk: LinearLayout = viewGroup.findViewById(R.id.ll_walk)
    private var llDrive: LinearLayout = viewGroup.findViewById(R.id.ll_drive)
    private var llBicycle: LinearLayout = viewGroup.findViewById(R.id.ll_bicycle)
    private var llTypes: LinearLayout = viewGroup.findViewById(R.id.ll_root_types)
    private var ivArrow: ImageView = viewGroup.findViewById(R.id.iv_arrow)

    init {
        llBicycle.setOnClickListener {
            listener.onTypeSelected(RouteType.Bicycle)
        }
        llDrive.setOnClickListener {
            listener.onTypeSelected(RouteType.Drive)
        }
        llWalk.setOnClickListener {
            listener.onTypeSelected(RouteType.Walking)
        }
        ivArrow.setOnClickListener {
            if (ivArrow.drawable == ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.ic_arrow_drop_up,
                    context.theme
                )
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
        llTypes.visible()
    }

    fun hideTypes() {
        ivArrow.rotate0()
        llTypes.gone()
    }


    interface ISelectedTypeListener {
        fun onTypeSelected(type: RouteType)
    }

}