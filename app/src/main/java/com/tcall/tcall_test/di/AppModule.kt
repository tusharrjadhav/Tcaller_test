package com.tcall.tcall_test.di

import com.tcall.tcall_test.repository.DataRepository
import com.tcall.tcall_test.repository.GetDataRepository
import com.tcall.tcall_test.repository.api.NetworkService
import com.tcall.tcall_test.use_cases.GetDataUseCase
import com.tcall.tcall_test.util.DataResult
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.CoroutineDispatcher

@InstallIn(ViewModelComponent::class)
@Module
class AppModule {

    @Provides
    fun provideDataUseCase(
        repository: DataRepository<DataResult<String>>,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): GetDataUseCase = GetDataUseCase(repository, ioDispatcher)

    @Provides
    fun provideDataRepository(
        networkService: NetworkService,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): DataRepository<DataResult<String>> = GetDataRepository(networkService, ioDispatcher)

}