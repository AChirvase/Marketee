package com.rinftech.marketee.framework.local_datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rinftech.marketee.utils.StringListToGsonConverter

@Database(
    entities = [SpecificEntity::class, MarketingCampaignEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(StringListToGsonConverter::class)
abstract class MarketeeRoomDataBase : RoomDatabase() {
    companion object {
        private const val DATABASE_NAME = "marketee.db"

        fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MarketeeRoomDataBase::class.java,
                DATABASE_NAME
            )
                .enableMultiInstanceInvalidation()
                .fallbackToDestructiveMigration()
                .build()
    }

    abstract fun marketeeDao(): MarketeeDao
}