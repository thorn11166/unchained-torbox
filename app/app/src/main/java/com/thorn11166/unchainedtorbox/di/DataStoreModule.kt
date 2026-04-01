package com.thorn11166.unchainedtorbox.di

import android.content.Context
import com.thorn11166.unchainedtorbox.data.local.ProtoStore
import com.thorn11166.unchainedtorbox.data.local.ProtoStoreImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Provides the datastore injected with Dagger Hilt */
@InstallIn(SingletonComponent::class)
@Module
object DataStoreModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext appContext: Context): ProtoStore {
        return ProtoStoreImpl(appContext)
    }
}
