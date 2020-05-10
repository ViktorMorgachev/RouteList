package com.sedi.routelist.presenters

import com.sedi.routelist.models.Notice


interface IClickListener {
    fun onSave(notice: Notice, position: Int)
}