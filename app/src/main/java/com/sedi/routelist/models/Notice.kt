package com.sedi.routelist.models

import android.os.Parcelable
import androidx.core.text.isDigitsOnly
import androidx.room.TypeConverter
import com.huawei.hms.maps.model.LatLng
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
    var residenceAdress: Address = Address(),
    var destinationAdress: Address = Address()
) : Parcelable

@Parcelize
data class Address(var address: String = "", val location: LatLng = LatLng(0.0, 0.0)) : Parcelable

