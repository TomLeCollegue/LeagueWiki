package com.androidcourse.leaguewiki

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import com.androidcourse.leaguewiki.data.ChampionsDataSource
import com.androidcourse.leaguewiki.data.ChampionsRepository
import com.androidcourse.leaguewiki.data.DataStoreManager
import com.androidcourse.leaguewiki.data.RetrofitClient
import com.androidcourse.leaguewiki.viewmodel.HomeViewModel
import com.google.gson.annotations.Since
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
class Containers {

    @Provides
    fun retrofitClient(): RetrofitClient {
        return RetrofitClient()
    }

    @Provides
    fun favoriteDataSource(@ApplicationContext appContext: Context): DataStoreManager {
        return DataStoreManager(appContext)
    }

    @Provides
    fun championsDataSource(retrofitClient: RetrofitClient): ChampionsDataSource {
        return ChampionsDataSource(retrofitClient)
    }

    @Provides
    fun championsRepository(championsDataSource: ChampionsDataSource, favoriteDataStore: DataStoreManager): ChampionsRepository {
        return ChampionsRepository(championsDataSource, favoriteDataStore)
    }

}