package com.alex.marketee.framework.remote_datasource

import com.alex.marketee.framework.local_datasource.MarketingCampaignEntity
import com.alex.marketee.framework.local_datasource.SpecificEntity
import retrofit2.Response
import retrofit2.http.GET

interface MarketeeService {

    @GET("/b/O193")
    suspend fun getSpecificsList(): List<SpecificEntity>

    @GET("/b/6R6S")
    suspend fun getMarketingCampaignsList(): Response<List<MarketingCampaignEntity>>
}