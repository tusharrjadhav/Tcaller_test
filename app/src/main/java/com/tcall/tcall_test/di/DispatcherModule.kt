package com.tcall.tcall_test.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier


@InstallIn(SingletonComponent::class)
@Module
class DispatcherModule {

    /**
     * Provides default dispatcher
     *
     * @return
     */
    @DefaultDispatcher
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    /**
     * Provides io dispatcher
     *
     * @return
     */
    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    /**
     * Provides main dispatcher
     *
     * @return
     */
    @MainDispatcher
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}

/**
 * Default dispatcher
 *
 * @constructor Create Default dispatcher
 */
@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher

/**
 * Io dispatcher
 *
 * @constructor Create Io dispatcher
 */
@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

/**
 * Main dispatcher
 *
 * @constructor Create Main dispatcher
 */
@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher