package com.sedi.routelist.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.huawei.hms.framework.common.Utils
import com.sedi.routelist.R
import com.sedi.routelist.commons.*
import com.sedi.routelist.databinding.RouteListFragmentBinding
import com.sedi.routelist.models.Address
import com.sedi.routelist.models.Notice
import com.sedi.routelist.interfaces.IClickListener
import com.sedi.routelist.ui.MainActivity
import com.sedi.routelist.ui.NoticesPagerAdapter
import com.sedi.routelist.ui.RememberData
import kotlinx.android.synthetic.main.route_list_fragment.*


class NoticeFragment : Fragment(),
    DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
    MainActivity.FragmentListener {

    //Data
    lateinit var binding: RouteListFragmentBinding
    lateinit var clickListener: IClickListener
    private var notice: Notice? = null
    private var fragmentListenerCallback: FragmentListenerCallback? = null

    // Logic
    private var pagerAdapter: NoticesPagerAdapter? = null
    private var position: Int? = null
    private var editableView: View? = null // Поле которое редактируется

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

        initListeners()
        initStates()

        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListeners() {
        binding.btnRoute.setOnClickListener {
            fragmentListenerCallback?.showMapActivity(
                notice?.residenceAdress,
                notice?.destinationAdress
            )
        }

        binding.ivShowOnMapDestination.setOnClickListener {
            RememberData.rememberMe(RememberData.KEYS.ADDRESS.value, notice!!.destinationAdress)
            RememberData.rememberMe(RememberData.KEYS.EDITTEXT.value, binding.etDestinationAdress)
            fragmentListenerCallback?.addessFromMap()
        }

        binding.ivShowOnMapResidence.setOnClickListener {
            RememberData.rememberMe(RememberData.KEYS.ADDRESS.value, notice!!.residenceAdress)
            RememberData.rememberMe(RememberData.KEYS.EDITTEXT.value, binding.etResidenceAdress)
            fragmentListenerCallback?.addessFromMap()

        }

        binding.btnSave.setOnClickListener {
            if (notice != null && position != null) {
                initNotice(true)
                clickListener.onSave(notice!!, position!!)
            }
        }


        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                initNotice(checkNetworkConnectivity(requireActivity()))
            }

        }

        binding.etFio.addTextChangedListener(textWatcher)
        binding.etPhone.addTextChangedListener(textWatcher)
        binding.etReason.addTextChangedListener(textWatcher)

        binding.etDate.setOnTouchListener { _, event ->
            val action = event.action
            if (action == MotionEvent.ACTION_DOWN) {
                val text = et_date.text.toString()
                val date = text.split(".")
                if (context != null && date.size == 3)
                    DatePickerDialog(
                        requireContext(),
                        this,
                        date[2].toInt(),
                        date[1].toInt(),
                        date[0].toInt()
                    ).show()
            }
            true
        }
        binding.etExitTime.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val text = et_exit_time.text.toString()
                val time = text.split(":")
                if (context != null && time.size == 2)
                    startTimePickerDialog(time[0].toInt(), time[1].toInt())
                editableView = v
            }
            true
        }
        binding.etResetingTime.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val text = et_reseting_time.text.toString()
                val time = text.split(":")
                if (context != null && time.size == 2)
                    startTimePickerDialog(time[0].toInt(), time[1].toInt())
                editableView = v
            }
            true
        }


    }

    fun initStates() {
        if (checkNetworkConnectivity(requireActivity())) {
            binding.ivShowOnMapDestination.visible()
            binding.ivShowOnMapResidence.visible()

            if (binding.etDestinationAdress.text.isEmpty()) {
                binding.etDestinationAdress.setOnTouchListener { _, _ ->
                    RememberData.rememberMe(
                        RememberData.KEYS.ADDRESS.value,
                        notice!!.destinationAdress
                    )
                    RememberData.rememberMe(
                        RememberData.KEYS.EDITTEXT.value,
                        binding.etDestinationAdress
                    )
                    fragmentListenerCallback?.showSearchAddress()
                    true
                }
            }

            if (binding.etResidenceAdress.text.isEmpty()) {
                binding.etResidenceAdress.setOnTouchListener { _, _ ->
                    RememberData.rememberMe(
                        RememberData.KEYS.ADDRESS.value,
                        notice!!.residenceAdress
                    )
                    RememberData.rememberMe(
                        RememberData.KEYS.EDITTEXT.value,
                        binding.etResidenceAdress
                    )
                    fragmentListenerCallback?.showSearchAddress()
                    true
                }
            }

            if (notice?.destinationAdress?.location != null &&
                notice?.residenceAdress?.location != null
            ) {
                binding.btnRoute.visible(500)
            } else {
                binding.btnRoute.gone(500)
            }

        } else {
            binding.ivShowOnMapDestination.gone()
            binding.ivShowOnMapResidence.gone()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onResume() {
        super.onResume()
        pagerAdapter?.noticeFragmentHelper?.currentNotice = notice!!
        pagerAdapter?.noticeFragmentHelper?.currentPosition = position!!
        notice?.dbKey = position!!
        initStates()
        log("Current position: $position")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notice = parseArguments()

    }


    private fun startTimePickerDialog(hour: Int, minute: Int) {
        TimePickerDialog(
            context,
            this,
            hour,
            minute,
            true
        ).show()
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
        ): NoticeFragment {
            return NoticeFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(
                        ExtraNames.KeysField.KEY_EXTRA_NOTICE,
                        notice
                    )
                }
                configurateFragment(saveClickListener, noticesPagerAdapter, position)
            }
        }

    }

    override fun pastNotice(notice: Notice) {
        try {
            this.notice = notice
            binding.routeNotice = notice
        } catch (e: Exception) {
            log(e)
        }

    }

    override fun initNotice(hasNetwork: Boolean) {

        notice?.fio = et_fio.text.toString()
        notice?.date = et_date.text.toString()
        notice?.destinationAdress?.address = et_destination_adress.text.toString()
        notice?.residenceAdress?.address = et_residence_adress.text.toString()
        notice?.exitTime = et_exit_time.text.toString()
        notice?.reason = et_reason.text.toString()
        notice?.resetingTime = et_reseting_time.text.toString()
        notice?.phoneNumber = et_phone.text.toString()


        if (RememberData.remindMe(RememberData.KEYS.ADDRESS.value) != null
            && RememberData.remindMe(RememberData.KEYS.EDITTEXT.value) != null
        ) {
            val address: Address = RememberData.remindMe(RememberData.KEYS.ADDRESS.value) as Address
            val editText: EditText? =
                RememberData.remindMe(RememberData.KEYS.EDITTEXT.value) as EditText
            // Init location
            if (hasNetwork) {
                if (editText?.tag == "address_residence") {
                    notice?.residenceAdress?.location = address.location
                } else {
                    notice?.destinationAdress?.location = address.location
                }
            } else {
                if (editText?.tag == "address_residence") {
                    notice?.residenceAdress?.location = null
                } else {
                    notice?.destinationAdress?.location = null
                }
            }

        }
        if (notice?.destinationAdress?.location != null &&
            notice?.residenceAdress?.location != null
        ) {
            btn_route.visible(500)
        } else {
            btn_route.gone(500)
        }

        RememberData.forgetAll()

        log("Notice: $notice")
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        try {
            val monthStr = month.toString()
            val dayStr = dayOfMonth.toString()
            val monthResult = when (monthStr.length) {
                2 -> monthStr
                else -> "0$monthStr"
            }
            val dayResult = when (dayStr.length) {
                2 -> dayStr
                else -> "0$dayStr"
            }
            this.notice!!.date = "$dayResult.$monthResult.$year"
            binding.routeNotice?.date = notice!!.date
            binding.etDate.setText(binding.routeNotice?.date)
            initNotice(true)
        } catch (e: Exception) {
            log(e)
        }


    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        try {
            val minuteStr = minute.toString()
            val minuteResult = when (minuteStr.length) {
                2 -> minuteStr
                else -> "0$minute"
            }
            when (editableView) {
                binding.etExitTime -> {
                    this.notice!!.exitTime = "$hourOfDay:${minuteResult}"
                    binding.routeNotice?.exitTime = notice!!.exitTime
                    binding.etExitTime.setText(binding.routeNotice?.exitTime)
                }
                binding.etResetingTime -> {
                    this.notice!!.resetingTime = "$hourOfDay:$minuteResult"
                    binding.routeNotice?.resetingTime = notice!!.resetingTime
                    binding.etResetingTime.setText(binding.routeNotice?.resetingTime)
                }

            }
            initNotice(true)
        } catch (e: Exception) {
            log(e)
        }
    }

    override fun updateUI(hasNetwork: Boolean) {
        if (hasNetwork) {
            iv_show_on_map_destination.visible(500)
            iv_show_on_map_residence.visible(500)
        } else {
            ViewCompat.animate(iv_show_on_map_destination).setDuration(500).alpha(1f).alphaBy(0.0f)
                .withEndAction {
                    iv_show_on_map_destination.alpha = 0.0f
                    iv_show_on_map_destination.gone()
                }.start()
            ViewCompat.animate(iv_show_on_map_residence).setDuration(500).alpha(1f).alphaBy(0.0f)
                .withEndAction {
                    iv_show_on_map_residence.alpha = 0.0f
                    iv_show_on_map_residence.gone()
                }.start()
        }
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (activity is FragmentListenerCallback) {
            fragmentListenerCallback = activity
        }
    }
}

interface FragmentListenerCallback {
    fun showMapActivity(addressFrom: Address?, addressTo: Address?)
    fun addessFromMap()
    fun showSearchAddress()
}