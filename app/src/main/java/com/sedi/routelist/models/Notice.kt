package com.sedi.routelist.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Notice(
    val fio: String = "",
    val date: String = "",
    val phoneNumber: String = "",
    val reason: String = "",
    val exitTime: String = "",
    val resetingTime: String = "",
    val residenceAdress: String = "",
    val destinationAdress: String = ""
) : Parcelable