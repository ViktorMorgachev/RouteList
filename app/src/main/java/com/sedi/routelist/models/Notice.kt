package com.sedi.routelist.models

import android.os.Parcelable
import com.sedi.routelist.commons.getCurrentDate
import com.sedi.routelist.commons.getCurrentTime
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Notice(
    var dbKey: Int = 0,
    var fio: String = "",
    var date: String = getCurrentDate(),
    var phoneNumber: String = "",
    var reason: String = "",
    var exitTime: String = getCurrentTime(),
    var resetingTime: String = getCurrentTime(),
    var residenceAdress: String = "",
    var destinationAdress: String = ""
) : Parcelable