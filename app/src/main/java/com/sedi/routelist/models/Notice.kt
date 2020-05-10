package com.sedi.routelist.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Notice(
    var fio: String = "",
    var date: String = "",
    var phoneNumber: String = "",
    var reason: String = "",
    var exitTime: String = "",
    var resetingTime: String = "",
    var residenceAdress: String = "",
    var destinationAdress: String = ""
) : Parcelable