package com.sedi.routelist.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.sedi.routelist.databinding.RouteListFragmentBinding
import com.sedi.routelist.models.Notice
import com.sedi.routelist.presenters.ISaveListener


class NoticeFragment : Fragment() {

    //Data
    lateinit var binding: RouteListFragmentBinding
    lateinit var saveListener: ISaveListener
    lateinit var notice: Notice

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            com.sedi.routelist.R.layout.route_list_fragment, container, false
        )

        notice = Notice()
        binding.routeNotice = notice
        binding.executePendingBindings()

        binding.btnSave.setOnClickListener {
            saveListener.onSave(notice)
        }

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ISaveListener) {
            saveListener = context
        }
    }
}