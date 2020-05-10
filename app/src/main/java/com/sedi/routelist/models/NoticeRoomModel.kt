package com.sedi.routelist.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class NoticeRoomModel {
    @PrimaryKey()
    var primaryKey: Int = 0

    @ColumnInfo(name = "fio")
    var fio: String = ""
    var date: String = ""
    var phoneNumber: String = ""
    var reason: String = ""
    var exitTime: String = ""
    var resetingTime: String = ""
    var residenceAdress: String = ""
    var destinationAdress: String = ""
}