package com.example.govix.queue.di

import com.example.govix.core.data.remote.GovixQueueApi
import com.example.govix.queue.data.repository.QueueRepositoryImpl
import com.example.govix.queue.domain.repository.QueueRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object QueueModule {
    @Provides
    @Singleton
    fun provideGovixQueueApi(retrofit: Retrofit): GovixQueueApi =
        retrofit.create(GovixQueueApi::class.java)
    @Provides
    @Singleton
    fun provideQueueRepository(
        api: GovixQueueApi,
    ): QueueRepository = QueueRepositoryImpl(api)
}