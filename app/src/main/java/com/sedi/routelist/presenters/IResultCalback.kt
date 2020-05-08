package com.sedi.routelist.presenters

import com.sedi.routelist.models.NoticeRoomModel

interface IResultCalback {
    fun onError(exception: Exception)
    fun onSingleComplete(
        data: NoticeRoomModel? = null
    )

    fun onSucces()

}