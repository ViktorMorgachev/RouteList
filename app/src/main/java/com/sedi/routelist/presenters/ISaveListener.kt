package com.sedi.routelist.presenters

import com.sedi.routelist.models.Notice

interface ISaveListener {
    fun onSave(notice: Notice);
}