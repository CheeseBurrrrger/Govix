package com.example.govix.core.network

import com.example.govix.BuildConfig
import com.example.govix.core.data.TokenDataStore
import com.example.govix.data.remote.MajadigiAuthApi
import com.example.govix.data.remote.MajadigiHospitalApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MajadigiRetrofit {

    private val gson: Gson = GsonBuilder().serializeNulls().setLenient().create()

    private fun retrofit(tokenDataStore: TokenDataStore): Retrofit {
        val base = BuildConfig.API_BASE_URL.trimEnd('/') + "/"
        return Retrofit.Builder()
            .baseUrl(base)
            .client(MajadigiHttpClient.create(tokenDataStore))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun authApi(tokenDataStore: TokenDataStore): MajadigiAuthApi =
        retrofit(tokenDataStore).create(MajadigiAuthApi::class.java)

    fun hospitalApi(tokenDataStore: TokenDataStore): MajadigiHospitalApi =
        retrofit(tokenDataStore).create(MajadigiHospitalApi::class.java)
}
