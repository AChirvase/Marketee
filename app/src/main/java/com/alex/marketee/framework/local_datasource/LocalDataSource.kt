package com.alex.marketee.framework.local_datasource

import androidx.lifecycle.LiveData
import com.alex.marketee.domain.MarketingCampaign
import com.alex.marketee.domain.Specific

interface LocalDataSource {
    suspend fun updateSpecificsList(specificsList: List<Specific>)
    suspend fun updateMarketingCampaignsList(marketingCampaignEntityList: List<MarketingCampaignEntity>)
    fun getSpecificsListLiveData(): LiveData<List<Specific>>
    fun getMarketingCampaignsListLiveData(): LiveData<List<MarketingCampaign>>
}

class LocalDataSourceImpl(private val marketeeDao: MarketeeDao) :
    LocalDataSource {
    override suspend fun updateSpecificsList(specificsList: List<Specific>) =
        marketeeDao.updateSpecificsList(Mapper.toEntity(specificsList))

    override suspend fun updateMarketingCampaignsList(marketingCampaignEntityList: List<MarketingCampaignEntity>) =
        marketeeDao.updateMarketingCampaign(marketingCampaignEntityList)

    override fun getSpecificsListLiveData() =
        Mapper.toDomainLiveDataSpecificsList(marketeeDao.getSpecificsListLiveData())

    override fun getMarketingCampaignsListLiveData(): LiveData<List<MarketingCampaign>> =
        Mapper.toDomainLiveDataMarketingCampaignsList(marketeeDao.getMarketingCampaignsListLiveData())

}