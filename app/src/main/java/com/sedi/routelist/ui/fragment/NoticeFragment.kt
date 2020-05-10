package com.sedi.routelist.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.sedi.routelist.R
import com.sedi.routelist.commons.LOG_LEVEL
import com.sedi.routelist.commons.log
import com.sedi.routelist.databinding.RouteListFragmentBinding
import com.sedi.routelist.commons.ExtraNames
import com.sedi.routelist.models.Notice
import com.sedi.routelist.presenters.IClickListener
import com.sedi.routelist.ui.MainActivity
import com.sedi.routelist.ui.NoticesPagerAdapter
import kotlinx.android.synthetic.main.route_list_fragment.*


class NoticeFragment : Fragment(), MainActivity.PastNoticeCallback {

    //Data
    lateinit var binding: RouteListFragmentBinding
    lateinit var clickListener: IClickListener
    private var notice: Notice? = null

    // Logic
    private lateinit var pagerAdapter: NoticesPagerAdapter
    private var position: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.route_list_fragment, container, false
        )

        if (notice == null) {
            notice = Notice()
        }

        binding.routeNotice = notice

        binding.executePendingBindings()
        binding.btnSave.setOnClickListener {
            if (notice != null && position != null) {
                initNotice()
                clickListener.onSave(notice!!, position!!)
            }

        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        pagerAdapter.noticeFragmentHelper.currentNotice = notice!!
        pagerAdapter.noticeFragmentHelper.currentPosition = position!!
    }

    private fun initNotice() {
        notice?.fio = et_fio.text.toString()
        notice?.date = et_date.text.toString()
        notice?.destinationAdress = et_destination_adress.text.toString()
        notice?.exitTime = et_exit_time.text.toString()
        notice?.reason = et_reason.text.toString()
        notice?.resetingTime = et_reseting_time.text.toString()
        notice?.phoneNumber = et_phone.text.toString()
        notice?.residenceAdress = et_residence_adress.text.toString()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notice = parseArguments()
    }


    private fun parseArguments(): Notice? {
        return arguments?.getParcelable<Notice>(ExtraNames.KeysField.KEY_EXTRA_NOTICE)
    }

    fun configurateFragment(
        clickListener: IClickListener,
        noticesPagerAdapter: NoticesPagerAdapter,
        position: Int
    ) {
        this.position = position
        this.pagerAdapter = noticesPagerAdapter
        this.clickListener = clickListener
    }

    companion object {
        fun instance(
            notice: Notice,
            saveClickListener: IClickListener,
            noticesPagerAdapter: NoticesPagerAdapter,
            position: Int
        ) =
            NoticeFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(
                        ExtraNames.KeysField.KEY_EXTRA_NOTICE,
                        notice
                    )
                }
                configurateFragment(saveClickListener, noticesPagerAdapter, position)
            }
    }

    override fun pastNotice(notice: Notice) {
        try {
            this.notice = notice
        } catch (e: Exception) {
            e.message?.let { log(LOG_LEVEL.ERROR, it) }
        }

    }

}