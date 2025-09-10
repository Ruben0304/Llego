package com.llego.multiplatform.data.network

import com.apollographql.apollo.ApolloClient


object GraphQLClient {
    
    val apolloClient: ApolloClient = ApolloClient.Builder()
        .serverUrl("https://llegobackend-production.up.railway.app/graphql")
        .build()
}