package com.tcall.tcall_test.di

import com.tcall.tcall_test.repository.DataRepository
import com.tcall.tcall_test.repository.GetDataRepository
import com.tcall.tcall_test.util.DataResult
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideDataRepository(
        // GetDataRepository is now a concrete class from commonMain
        // It needs its dependencies. NetworkService is provided by NetworkModule.
        networkService: com.tcall.tcall_test.repository.api.NetworkService // From shared (actualized for Android)
    ): DataRepository<DataResult<String>> = com.tcall.tcall_test.repository.GetDataRepository(networkService)
}
