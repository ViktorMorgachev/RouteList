package com.sedi.routelist.models

import com.huawei.hms.maps.model.LatLng


fun getAddressFromString(adress: String): String {
    val values = adress.split("/")
    if (values.isNotEmpty()) {
        return values[0]
    }
    return ""
}

fun addressTransformation(noticeRoomModel: NoticeRoomModel, notice: Notice) {
    val residenceAdress: String = noticeRoomModel.residenceAdress
    val destinationAdress: String = noticeRoomModel.destinationAdress
    val residenceValues = residenceAdress.split("/")
    val destinationValues = destinationAdress.split("/")

    if (residenceAdress.isNotEmpty()) {
        notice.residenceAdress.address = residenceValues[0]
    }

    if (destinationAdress.isNotEmpty()) {
        notice.destinationAdress.address = destinationValues[0]
    }

    if (destinationValues.size == 3) {
        notice.destinationAdress.location =
            LatLng(destinationValues[1].toDouble(), destinationValues[2].toDouble())
    }

    if (residenceValues.size == 3) {
        notice.residenceAdress.location =
            LatLng(residenceAdress[1].toDouble(), residenceAdress[2].toDouble())
    }

}
