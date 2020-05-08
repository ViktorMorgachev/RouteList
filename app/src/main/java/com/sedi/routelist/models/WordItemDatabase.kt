package com.sedi.routelist.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sedi.routelist.models.dao.NoticeDao

@Database(entities = arrayOf(NoticeRoomModel::class), exportSchema = false, version = 1)
abstract class NoticeItemDatabase : RoomDatabase() {

    abstract fun noticeDAO(): NoticeDao

    companion object {
        private val DB_NAME = "notice_item_db"
        @Volatile
        private var instance: NoticeItemDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, NoticeItemDatabase::class.java, DB_NAME).build()
    }

}