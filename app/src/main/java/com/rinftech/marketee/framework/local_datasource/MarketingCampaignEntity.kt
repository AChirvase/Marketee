package com.rinftech.marketee.framework.local_datasource

import androidx.room.Entity
import androidx.room.TypeConverters
import com.rinftech.marketee.utils.StringListToGsonConverter

@Entity(tableName = "marketing_campaign_table", primaryKeys = ["channelName", "campaignName"])
data class MarketingCampaignEntity(
    var channelName: String,
    var campaignName: String,
    var price: String,
    @TypeConverters(StringListToGsonConverter::class)
    var features: ArrayList<String>
)