package com.rinftech.marketee.framework.remote_datasource

import com.rinftech.marketee.framework.local_datasource.MarketingOfferEntity
import com.rinftech.marketee.framework.local_datasource.SpecificEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET

interface MarketeeService {

    @GET("/b/O193")
    suspend fun getSpecificsList(): List<SpecificEntity>

    @GET("/b/CXGM")
    suspend fun getMarketingOffersList(): Response<List<MarketingOfferEntity>>
}