package com.tcall.tcall_test.di

import com.tcall.tcall_test.repository.DataRepository // From shared module
import com.tcall.tcall_test.use_cases.GetDataUseCase // From shared module
import com.tcall.tcall_test.util.DataResult // From shared module
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideGetDataUseCase(
        dataRepository: DataRepository<DataResult<String>> // Provided by RepositoryModule
    ): GetDataUseCase = GetDataUseCase(dataRepository)
}
