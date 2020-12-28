package com.rinftech.marketee.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class StringListToGsonConverter {
    @TypeConverter
    fun restoreList(listOfString: String): ArrayList<String>? {
        return Gson().fromJson<ArrayList<String>>(
            listOfString,
            object : TypeToken<ArrayList<String>>() {}.type
        )
    }

    @TypeConverter
    fun saveListOfString(listOfString: List<String>): String {
        return Gson().toJson(listOfString)
    }
}