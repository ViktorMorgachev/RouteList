package com.sedi.routelist.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.sedi.routelist.databinding.RouteListFragmentBinding
import com.sedi.routelist.models.ExtraNames
import com.sedi.routelist.models.Notice
import com.sedi.routelist.presenters.ISaveListener


class NoticeFragment : Fragment() {

    //Data
    lateinit var binding: RouteListFragmentBinding
    lateinit var saveListener: ISaveListener
    private var notice: Notice? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = DataBindingUtil.inflate(
            inflater,
            com.sedi.routelist.R.layout.route_list_fragment, container, false
        )

        if (notice != null) {
            binding.routeNotice = notice
        } else {
            binding.routeNotice = Notice()
        }

        binding.executePendingBindings()
        binding.btnSave.setOnClickListener {
            if (notice != null)
                saveListener.onSave(notice!!)
        }

        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notice = parseArguments()
    }

    private fun parseArguments(): Notice? {
        return arguments?.getParcelable<Notice>(ExtraNames.KeysField.KEY_EXTRA_NOTICE)
    }

    fun addCallback(clickListener: ISaveListener) {
        saveListener = clickListener
    }

    companion object {
        fun newInstance(notice: Notice, saveClickListener: ISaveListener) = NoticeFragment().apply {
            arguments = Bundle().apply {
                putParcelable(
                    ExtraNames.KeysField.KEY_EXTRA_NOTICE,
                    notice
                )
            }
            addCallback(saveClickListener)
        }
    }

}