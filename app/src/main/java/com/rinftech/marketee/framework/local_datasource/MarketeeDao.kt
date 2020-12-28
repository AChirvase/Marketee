package com.rinftech.marketee.framework.local_datasource

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.rinftech.marketee.domain.MarketingOffer

@Dao
@JvmSuppressWildcards
interface MarketeeDao {

    @Insert(onConflict = REPLACE)
    suspend fun updateSpecificsList(specificsEntityList: List<SpecificEntity>)

    @Query("SELECT * FROM specific_table")
    fun getSpecificsListLiveData(): LiveData<List<SpecificEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun updateMarketingOffer(marketingOfferEntityList: List<MarketingOfferEntity>)

    @Query("SELECT * FROM marketing_offer_table")
    fun getMarketingOffersListLiveData(): LiveData<List<MarketingOfferEntity>>

    @Query("SELECT * FROM marketing_offer_table WHERE channelName LIKE :channelName ")
    fun getMarketingOffersByChannelLiveData(channelName: String): LiveData<List<MarketingOfferEntity>>
}