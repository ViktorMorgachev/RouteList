package com.sedi.routelist.models.dao

import androidx.room.*
import com.sedi.routelist.models.Notice
import com.sedi.routelist.models.NoticeRoomModel

@Dao
interface NoticeDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(noticeItem: NoticeRoomModel)

    @Delete
    fun delete(noticeItem: NoticeRoomModel)

    @Update
    fun update(noticeItem: NoticeRoomModel)

    @Query("SELECT * FROM NoticeRoomModel")
    fun getAllNotices(): List<NoticeRoomModel>

}