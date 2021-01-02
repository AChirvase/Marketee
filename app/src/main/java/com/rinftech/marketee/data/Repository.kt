package com.rinftech.marketee.data

import androidx.lifecycle.LiveData
import com.rinftech.marketee.domain.MarketingCampaign
import com.rinftech.marketee.domain.Specific
import com.rinftech.marketee.framework.local_datasource.LocalDataSource
import com.rinftech.marketee.framework.remote_datasource.RemoteDataSource
import com.rinftech.marketee.framework.local_datasource.MarketingCampaignEntity
import com.rinftech.marketee.framework.local_datasource.SpecificEntity
import com.rinftech.marketee.framework.remote_datasource.NetworkResponseHandler
import com.rinftech.marketee.framework.remote_datasource.Resource
import org.koin.core.KoinComponent


class Repository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val responseHandler: NetworkResponseHandler
) : KoinComponent {

    fun getSpecificsListLiveData() =
        localDataSource.getSpecificsListLiveData()

    suspend fun loadSpecificsList(): Resource<List<Specific>> {
        return try {
            val response = remoteDataSource.getSpecificsList()
            localDataSource.updateSpecificsList(response)
            responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    fun getMarketingCampaignListLiveData(): LiveData<List<MarketingCampaign>> =
        localDataSource.getMarketingCampaignsListLiveData()

    suspend fun loadMarketingCampaignsList(): Resource<List<MarketingCampaignEntity>> {
        //update from server with the latest data in background (using coroutine)
        return try {
            val response = remoteDataSource.getMarketingCampaignsList()
            localDataSource.updateMarketingCampaignsList(response!!)
            responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

}