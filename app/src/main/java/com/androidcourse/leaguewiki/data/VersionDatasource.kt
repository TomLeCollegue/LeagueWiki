package com.androidcourse.leaguewiki.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.androidcourse.leaguewiki.Constants
import com.androidcourse.leaguewiki.api.ApiVersionService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VersionDatasource @Inject constructor(
    retrofitClient: RetrofitClient,
    @ApplicationContext val context: Context,
    private val dataStore: DataStoreManager
) {

    private val apiVersionService = retrofitClient.retrofit.create(ApiVersionService::class.java)

    suspend fun fetchLastVersion(): String? {
        try {
            val result = apiVersionService.getVersions()
            return if (result.isSuccessful) {
                result.body()?.firstOrNull()
            } else {
                Log.d("Log", "error fetching champs")
                null
            }
        } catch (e: Exception) {
            Log.d("Log", "error fetching champs")
        }
        return null
    }

    suspend fun setNewVersion(version: String) {
        val versionPref = stringPreferencesKey(Constants.Config.VERSION_PREF)
        dataStore.dataStore.edit {
            it[versionPref] = version
        }
    }

    suspend fun getVersion(): String {
        val versionPref = stringPreferencesKey(Constants.Config.VERSION_PREF)
        return dataStore.dataStore.data.first()[versionPref] ?: Constants.Config.DEFAULT_VERSION
    }

}