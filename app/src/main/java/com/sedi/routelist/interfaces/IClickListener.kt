package com.sedi.routelist.interfaces

import com.sedi.routelist.models.Notice


interface IClickListener {
    fun onSave(notice: Notice, position: Int)
}