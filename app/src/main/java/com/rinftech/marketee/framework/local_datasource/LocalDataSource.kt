package com.rinftech.marketee.framework.local_datasource

import androidx.lifecycle.LiveData
import com.rinftech.marketee.domain.MarketingOffer
import com.rinftech.marketee.domain.Specific

interface LocalDataSource {
    suspend fun updateSpecificsList(specificsList: List<Specific>)
    suspend fun updateMarketingOffersList(marketingOfferEntityList: List<MarketingOfferEntity>)
    fun getSpecificsListLiveData(): LiveData<List<Specific>>
    fun getMarketingOffersListLiveData(): LiveData<List<MarketingOffer>>
}

class LocalDataSourceImpl(private val marketeeDao: MarketeeDao) :
    LocalDataSource {
    override suspend fun updateSpecificsList(specificsList: List<Specific>) =
        marketeeDao.updateSpecificsList(Mapper.toEntity(specificsList))

    override suspend fun updateMarketingOffersList(marketingOfferEntityList: List<MarketingOfferEntity>) =
        marketeeDao.updateMarketingOffer(marketingOfferEntityList)

    override fun getSpecificsListLiveData() =
        Mapper.toDomainLiveDataSpecificsList(marketeeDao.getSpecificsListLiveData())

    override fun getMarketingOffersListLiveData(): LiveData<List<MarketingOffer>> =
        Mapper.toDomainLiveDataMarketingOffersList(marketeeDao.getMarketingOffersListLiveData())

}