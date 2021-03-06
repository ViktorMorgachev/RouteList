package com.sedi.routelist.presenters

import com.sedi.routelist.models.Notice
import com.sedi.routelist.models.NoticeRoomModel

interface IResultCalback {
    fun onError(exception: Exception)
    fun onSucces(answer: String = "", notices: List<Notice> = arrayListOf())

}