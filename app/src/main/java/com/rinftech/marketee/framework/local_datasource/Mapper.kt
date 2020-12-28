package com.rinftech.marketee.framework.local_datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.rinftech.marketee.domain.MarketingOffer
import com.rinftech.marketee.domain.Specific

class Mapper {
    companion object {

        fun toDomain(specificEntity: SpecificEntity) =
            Specific(specificEntity.specificName, specificEntity.channels)

        fun toDomainLiveDataSpecificsList(specificEntityList: LiveData<List<SpecificEntity>>): LiveData<List<Specific>> {
            return Transformations.map(specificEntityList) {
                it.map { entity ->
                    Specific(entity.specificName, entity.channels)
                }
            }
        }

        fun toDomain(specificEntityList: List<SpecificEntity>): List<Specific> =
            specificEntityList.map { Specific(it.specificName, it.channels) }


        fun toDomainLiveDataMarketingOffersList(marketingOffersList: LiveData<List<MarketingOfferEntity>>): LiveData<List<MarketingOffer>> {
            return Transformations.map(marketingOffersList) {
                it.map { entity ->
                    MarketingOffer(
                        entity.channelName,
                        entity.offerName,
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