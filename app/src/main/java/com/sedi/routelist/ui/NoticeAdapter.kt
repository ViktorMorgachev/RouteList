package com.sedi.routelist.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.sedi.routelist.R
import com.sedi.routelist.databinding.RouteListFragmentBinding
import com.sedi.routelist.models.Notice
import com.sedi.routelist.presenters.ISaveListener


class NoticeAdapter(
    val context: Context,
    val notices: List<Notice>,
    val saveListener: ISaveListener
) : PagerAdapter() {

    //Data
    lateinit var binding: RouteListFragmentBinding

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int = notices.size


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.route_list_fragment, container, false
        )

        binding.routeNotice = notices[position]
        binding.executePendingBindings()

        binding.btnSave.setOnClickListener {
            saveListener.onSave(notices[position])
        }

        return binding.root
    }

}