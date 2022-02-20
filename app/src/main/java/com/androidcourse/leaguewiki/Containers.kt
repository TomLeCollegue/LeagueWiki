package com.androidcourse.leaguewiki

import android.content.Context
import com.androidcourse.leaguewiki.data.ChampionsDataSource
import com.androidcourse.leaguewiki.data.ChampionsRepository
import com.androidcourse.leaguewiki.data.DataStoreManager
import com.androidcourse.leaguewiki.data.LocalChampionsDatasource
import com.androidcourse.leaguewiki.data.RetrofitClient
import com.androidcourse.leaguewiki.data.VersionDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
class Containers {

    @Singleton
    @Provides
    fun dataStoreManager(@ApplicationContext appContext: Context): DataStoreManager {
        return DataStoreManager(appContext)
    }

    @Singleton
    @Provides
    fun retrofitClient(): RetrofitClient {
        return RetrofitClient()
    }

    @Singleton
    @Provides
    fun championsDataSource(@ApplicationContext appContext: Context, retrofitClient: RetrofitClient): ChampionsDataSource {
        return ChampionsDataSource(retrofitClient, appContext)
    }

    @Singleton
    @Provides
    fun versionDataSource(@ApplicationContext appContext: Context, retrofitClient: RetrofitClient, dataStoreManager: DataStoreManager): VersionDatasource {
        return VersionDatasource(retrofitClient, appContext,dataStoreManager)
    }

    @Singleton
    @Provides
    fun localChampionDataSource(@ApplicationContext appContext: Context): LocalChampionsDatasource {
        return LocalChampionsDatasource(appContext)
    }

    @Singleton
    @Provides
    fun championsRepository(
        championsDataSource: ChampionsDataSource,
        versionDatasource: VersionDatasource,
        localChampionsDatasource: LocalChampionsDatasource
    ): ChampionsRepository {
        return ChampionsRepository(championsDataSource, versionDatasource, localChampionsDatasource)
    }

}