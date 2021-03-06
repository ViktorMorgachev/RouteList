package com.sedi.routelist.models

import android.app.Activity
import com.huawei.hms.maps.model.LatLng
import com.sedi.routelist.MyApplication
import com.sedi.routelist.R
import com.sedi.routelist.commons.asynkExecute
import com.sedi.routelist.presenters.IResultCalback


fun convertNoticeItemToRoomModel(notice: Notice) =
    NoticeRoomModel().apply {
        primaryKey = notice.dbKey
        fio = notice.fio
        date = notice.date
        phoneNumber = notice.phoneNumber
        reason = notice.reason
        exitTime = notice.exitTime
        resetingTime = notice.resetingTime
        residenceAdress =
            "${notice.residenceAdress.address}/${notice.residenceAdress.location?.latitude}/${notice.residenceAdress.location?.longitude}"
        destinationAdress =
            "${notice.destinationAdress.address}/${notice.destinationAdress.location?.latitude}/${notice.destinationAdress.location?.longitude}"
    }

fun convertRoomModelToNotice(noticeRoomModel: NoticeRoomModel): Notice {
    val notice = Notice().apply {
        dbKey = noticeRoomModel.primaryKey
        fio = noticeRoomModel.fio
        date = noticeRoomModel.date
        phoneNumber = noticeRoomModel.phoneNumber
        reason = noticeRoomModel.reason
        exitTime = noticeRoomModel.exitTime
        resetingTime = noticeRoomModel.resetingTime
    }
    addressTransformation(noticeRoomModel, notice)
    return notice
}


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
            resultCallback.onSucces(MyApplication.instance.resources.getString(R.string.sucess_saved))
        } catch (e: Exception) {
            resultCallback.onError(e)
        }
    }
}

fun asynkGetAllNotices(
    activity: Activity,
    resultCallback: IResultCalback,
    db: NoticeItemDatabase
) {
    asynkExecute {
        Thread.currentThread().name = "Database Thread"

        try {
            val notices = db.noticeDAO().getAllNotices()
            val listOfNotices: ArrayList<Notice> = ArrayList()
            notices.forEach {
                listOfNotices.add(convertRoomModelToNotice(it))
            }
            activity.runOnUiThread { resultCallback.onSucces(notices = listOfNotices) }
        } catch (e: Exception) {
            activity.runOnUiThread { resultCallback.onError(e) }

        }

    }

}

fun asynkDeleteNotice(
    resultCallback: IResultCalback,
    db: NoticeItemDatabase,
    noticeRoomModel: NoticeRoomModel
) {
    asynkExecute {
        Thread.currentThread().name = "Database Thread"

        try {
            db.noticeDAO().delete(noticeRoomModel)
            resultCallback.onSucces(MyApplication.instance.resources.getString(R.string.sucess_deleted))
        } catch (e: Exception) {
            resultCallback.onError(e)
        }
    }

}
