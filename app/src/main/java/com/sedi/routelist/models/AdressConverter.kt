package com.sedi.routelist.models


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

    if (residenceAdress.isNotEmpty()){
        notice.residenceAdress.address = residenceValues[0]
    }

    if (destinationAdress.isNotEmpty()){
        notice.destinationAdress.address = destinationValues[0]
    }

    if (destinationValues.size == 3){
        notice.destinationAdress.location.latitude = destinationValues[1].toDouble()
        notice.destinationAdress.location.longitude = destinationValues[2].toDouble()
    }

    if (residenceValues.size == 3){
        notice.residenceAdress.location.latitude = residenceValues[1].toDouble()
        notice.residenceAdress.location.longitude = residenceValues[2].toDouble()
    }

}
