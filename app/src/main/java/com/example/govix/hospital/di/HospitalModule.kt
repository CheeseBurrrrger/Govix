package com.example.govix.hospital.di

import com.example.govix.data.remote.GovixHospitalApi
import com.example.govix.hospital.data.repository.HospitalRepositoryImpl
import com.example.govix.hospital.domain.repository.HospitalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HospitalModule{
    @Provides
    @Singleton
    fun provideGovixHospitalApi(retrofit: Retrofit): GovixHospitalApi =
        retrofit.create(GovixHospitalApi::class.java)

    @Provides
    @Singleton
    fun provideHospitalRepository(
        api: GovixHospitalApi,
    ): HospitalRepository = HospitalRepositoryImpl(api)
}