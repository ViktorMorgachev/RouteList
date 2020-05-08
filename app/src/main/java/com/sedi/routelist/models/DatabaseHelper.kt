package com.sedi.routelist.models

import android.os.AsyncTask
import com.sedi.routelist.presenters.IResultCalback

fun replaceNotice() {

}

fun convertNoticeItemToRoomModel(notice: Notice) =
    NoticeRoomModel().apply {
        fio = notice.fio
        date = notice.date
        phoneNumber = notice.phoneNumber
        reason = notice.reason
        exitTime = notice.exitTime
        resetingTime = notice.resetingTime
        residenceAdress = notice.residenceAdress
        destinationAdress = notice.destinationAdress
    }

fun convertRoomModelToNotice(noticeRoomModel: NoticeRoomModel) = Notice(
    fio = noticeRoomModel.fio,
    date = noticeRoomModel.date,
    phoneNumber = noticeRoomModel.phoneNumber,
    reason = noticeRoomModel.reason,
    exitTime = noticeRoomModel.exitTime,
    resetingTime = noticeRoomModel.resetingTime,
    residenceAdress = noticeRoomModel.residenceAdress,
    destinationAdress = noticeRoomModel.destinationAdress
)

fun asynkSaveNotice(noticeRoomModel: NoticeRoomModel, resultCallback: IResultCalback) {
    AsyncTask.execute {
        Runnable {

        }
    }
}

fun asynkGetNoticeByIDs(id: Int, resultCallback: IResultCalback) {

}