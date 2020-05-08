package com.sedi.routelist.models.dao

import androidx.room.*
import com.sedi.routelist.models.Notice
import com.sedi.routelist.models.NoticeRoomModel

@Dao
interface NoticeDao {

    @Query("SELECT * FROM NoticeRoomModel WHERE primaryKey = :key")
    fun getNoticeByKey(key: Int): Notice

    @Insert
    fun insert(noticeItem: NoticeRoomModel)

    @Delete
    fun delete(noticeItem: NoticeRoomModel)

    @Update
    fun update(noticeItem: NoticeRoomModel)

}