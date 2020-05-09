package com.sedi.routelist.presenters

import com.sedi.routelist.models.Notice
import kotlinx.android.parcel.Parcelize


interface ISaveListener {
    fun onSave(notice: Notice);
}