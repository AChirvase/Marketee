package com.alex.marketee.framework.local_datasource

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
@JvmSuppressWildcards
interface MarketeeDao {

    @Insert(onConflict = REPLACE)
    suspend fun updateSpecificsList(specificsEntityList: List<SpecificEntity>)

    @Query("SELECT * FROM specific_table")
    fun getSpecificsListLiveData(): LiveData<List<SpecificEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun updateMarketingCampaign(marketingCampaignEntityList: List<MarketingCampaignEntity>)

    @Query("SELECT * FROM marketing_campaign_table")
    fun getMarketingCampaignsListLiveData(): LiveData<List<MarketingCampaignEntity>>

    @Query("SELECT * FROM marketing_campaign_table WHERE channelName LIKE :channelName ")
    fun getMarketingCampaignsByChannelLiveData(channelName: String): LiveData<List<MarketingCampaignEntity>>
}