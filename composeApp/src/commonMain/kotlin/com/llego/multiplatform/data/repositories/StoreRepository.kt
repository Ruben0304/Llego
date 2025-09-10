package com.llego.multiplatform.data.repositories

import com.llego.multiplatform.data.model.Store
import com.llego.multiplatform.data.network.GraphQLClient
import com.llego.multiplatform.graphql.GetStoresQuery

class StoreRepository {
    
    private val apolloClient = GraphQLClient.apolloClient
    
    suspend fun getStores(): List<Store> {
        val response = apolloClient.query(GetStoresQuery()).execute()
        
        if (response.hasErrors()) {
            throw Exception("GraphQL errors: ${response.errors?.joinToString { it.message }}")
        }
        
        val data = response.data ?: throw Exception("No data received from GraphQL server")
        
        return data.stores.map { storeData ->
            Store(
                id = storeData.id,
                name = storeData.name,
                etaMinutes = storeData.etaMinutes,
                logoUrl = storeData.logoUrl,
                bannerUrl = storeData.bannerUrl
            )
        }
    }
}