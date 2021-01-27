package com.alex.marketee.data

import androidx.lifecycle.LiveData
import com.alex.marketee.domain.MarketingCampaign
import com.alex.marketee.domain.Specific
import com.alex.marketee.framework.local_datasource.LocalDataSource
import com.alex.marketee.framework.local_datasource.MarketingCampaignEntity
import com.alex.marketee.framework.remote_datasource.NetworkResponseHandler
import com.alex.marketee.framework.remote_datasource.RemoteDataSource
import com.alex.marketee.framework.remote_datasource.Resource
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
        return try {
            val response = remoteDataSource.getMarketingCampaignsList()
            localDataSource.updateMarketingCampaignsList(response!!)
            responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

}