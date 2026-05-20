package com.example.govix.core.di

import com.example.govix.profile.data.remote.ProfileApiService
import com.example.govix.profile.data.repository.ProfileRepositoryImpl
import com.example.govix.profile.domain.repository.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import retrofit2.Retrofit
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {

    @Provides
    @Singleton
    fun provideProfileApiService(retrofit: Retrofit): ProfileApiService =
        retrofit.create(ProfileApiService::class.java)

    @Provides
    @Singleton
    fun provideProfileRepository(api: ProfileApiService): ProfileRepository =
        ProfileRepositoryImpl(api)
}