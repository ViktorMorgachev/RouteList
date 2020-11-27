package com.sedi.routelist.interfaces

import com.sedi.routelist.models.Notice

interface IResultCalback {
    fun onError(exception: Exception)
    fun onSucces(answer: String = "", notices: List<Notice> = arrayListOf())

}