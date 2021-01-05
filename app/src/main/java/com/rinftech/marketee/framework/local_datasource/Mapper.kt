package com.rinftech.marketee.framework.local_datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.rinftech.marketee.domain.MarketingCampaign
import com.rinftech.marketee.domain.Specific

class Mapper {
    companion object {

        fun toDomainLiveDataSpecificsList(specificEntityList: LiveData<List<SpecificEntity>>): LiveData<List<Specific>> {
            return Transformations.map(specificEntityList) {
                it.map { entity ->
                    Specific(entity.specificName, entity.channels)
                }
            }
        }

        fun toDomain(specificEntityList: List<SpecificEntity>): List<Specific> =
            specificEntityList.map { Specific(it.specificName, it.channels) }


        fun toDomainLiveDataMarketingCampaignsList(marketingCampaignsList: LiveData<List<MarketingCampaignEntity>>): LiveData<List<MarketingCampaign>> {
            return Transformations.map(marketingCampaignsList) {
                it.map { entity ->
                    MarketingCampaign(
                        entity.channelName,
                        entity.campaignName,
                        entity.price,
                        entity.features
                    )
                }
            }
        }

        fun toEntity(specificsList: List<Specific>): List<SpecificEntity> =
            specificsList.map { SpecificEntity(it.specificName, it.channelsNamesList) }


    }
}