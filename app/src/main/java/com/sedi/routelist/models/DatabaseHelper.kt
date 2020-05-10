package com.sedi.routelist.models

import com.sedi.routelist.commons.asynkExecute
import com.sedi.routelist.presenters.IResultCalback


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

fun asynkInsertNotice(
    noticeRoomModel: NoticeRoomModel,
    resultCallback: IResultCalback,
    db: NoticeItemDatabase, key: Int
) {
    asynkExecute {
        Thread.currentThread().name = "Database Thread"
        noticeRoomModel.primaryKey = key
        try {
            db.noticeDAO().insert(noticeRoomModel)
            resultCallback.onSucces("Успешно сохранено")
        } catch (e: Exception) {
            resultCallback.onError(e)
        }
    }
}

fun asynkGetAllNotices(
    resultCallback: IResultCalback,
    db: NoticeItemDatabase
) {
    asynkExecute {
        Thread.currentThread().name = "Database Thread"

        try {
            val notices = db.noticeDAO().getAllNotices()
            resultCallback.onSucces(notices = notices)
        } catch (e: Exception) {
            resultCallback.onError(e)
        }
    }

}

fun asynkDeleteNotice(resultCallback: IResultCalback, db: NoticeItemDatabase) {
    asynkExecute {
        Thread.currentThread().name = "Database Thread"

        try {
            val notices = db.noticeDAO().getAllNotices()
            resultCallback.onSucces(notices = notices)
        } catch (e: Exception) {
            resultCallback.onError(e)
        }
    }

}
