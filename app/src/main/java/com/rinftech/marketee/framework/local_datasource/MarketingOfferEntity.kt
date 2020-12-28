package com.rinftech.marketee.framework.local_datasource

import androidx.room.Entity
import androidx.room.TypeConverters
import com.rinftech.marketee.utils.StringListToGsonConverter

@Entity(tableName = "marketing_offer_table", primaryKeys = ["channelName", "offerName"])
data class MarketingOfferEntity(
    var channelName: String,
    var offerName: String,
    var price: String,
    @TypeConverters(StringListToGsonConverter::class)
    var features: ArrayList<String>
)