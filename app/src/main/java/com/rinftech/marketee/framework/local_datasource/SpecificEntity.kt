package com.rinftech.marketee.framework.local_datasource

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.rinftech.marketee.utils.StringListToGsonConverter

@Entity(tableName = "specific_table")
data class SpecificEntity(
    @PrimaryKey
    var specificName: String,
    @TypeConverters(StringListToGsonConverter::class)
    var channels: ArrayList<String>
)