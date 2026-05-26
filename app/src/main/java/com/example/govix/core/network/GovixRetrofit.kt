package com.example.govix.core.network

import com.example.govix.BuildConfig
import com.example.govix.core.data.TokenDataStore
import com.example.govix.core.data.remote.GovixAuthApi
import com.example.govix.core.data.remote.GovixHospitalApi
import com.example.govix.core.data.remote.GovixQueueApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GovixRetrofit {
    private val gson: Gson = GsonBuilder().serializeNulls().setLenient().create()
    private fun retrofit(tokenDataStore: TokenDataStore): Retrofit {
        val base = BuildConfig.API_BASE_URL.trimEnd('/') + "/"
        return Retrofit.Builder()
            .baseUrl(base)
            .client(GovixHttpClient.create(tokenDataStore))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    fun authApi(tokenDataStore: TokenDataStore): GovixAuthApi =
        retrofit(tokenDataStore).create(GovixAuthApi::class.java)
    fun hospitalApi(tokenDataStore: TokenDataStore): GovixHospitalApi =
        retrofit(tokenDataStore).create(GovixHospitalApi::class.java)
    fun queueApi(tokenDataStore: TokenDataStore): GovixQueueApi =
        retrofit(tokenDataStore).create(GovixQueueApi::class.java)
}
