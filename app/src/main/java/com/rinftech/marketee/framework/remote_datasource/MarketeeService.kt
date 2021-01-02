package com.rinftech.marketee.framework.remote_datasource

import com.rinftech.marketee.framework.local_datasource.MarketingCampaignEntity
import com.rinftech.marketee.framework.local_datasource.SpecificEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET

interface MarketeeService {

    @GET("/b/O193")
    suspend fun getSpecificsList(): List<SpecificEntity>

    @GET("/b/QSSO")
    suspend fun getMarketingCampaignsList(): Response<List<MarketingCampaignEntity>>
}