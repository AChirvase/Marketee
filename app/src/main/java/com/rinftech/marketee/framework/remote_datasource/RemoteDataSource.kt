package com.rinftech.marketee.framework.remote_datasource

import com.rinftech.marketee.domain.Specific
import com.rinftech.marketee.framework.local_datasource.Mapper
import com.rinftech.marketee.framework.local_datasource.MarketingCampaignEntity

interface RemoteDataSource {
    suspend fun getSpecificsList(): List<Specific>
    suspend fun getMarketingCampaignsList(): List<MarketingCampaignEntity>?
}

class RemoteDataSourceImpl(private val service: MarketeeService) :
    RemoteDataSource {

    override suspend fun getSpecificsList(): List<Specific> =
        Mapper.toDomain(service.getSpecificsList())


    override suspend fun getMarketingCampaignsList(): List<MarketingCampaignEntity>? =
        service.getMarketingCampaignsList().body()

}