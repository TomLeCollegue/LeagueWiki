package com.androidcourse.leaguewiki

import com.androidcourse.leaguewiki.data.ChampionsDataSource
import com.androidcourse.leaguewiki.data.ChampionsRepository
import com.androidcourse.leaguewiki.data.RetrofitClient
import com.androidcourse.leaguewiki.viewmodel.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityComponent::class)
class Containers {

    @Provides
    fun retrofitClient(): RetrofitClient {
        return RetrofitClient()
    }

    @Provides
    fun championsDataSource(): ChampionsDataSource {
        return ChampionsDataSource(retrofitClient())
    }

    @Provides
    fun championsRepository(): ChampionsRepository {
        return ChampionsRepository(championsDataSource())
    }

}

@Module
@InstallIn(ActivityRetainedComponent::class)
class ViewModelContainers {


}