package com.androidcourse.leaguewiki.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidcourse.leaguewiki.data.ChampionsRepository
import com.androidcourse.leaguewiki.data.VersionDatasource
import com.androidcourse.leaguewiki.model.ChampionDetail
import com.androidcourse.leaguewiki.model.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChampDetailViewModel @Inject constructor(
    private val repository: ChampionsRepository,
    private val versionDatasource: VersionDatasource,
) : ViewModel() {

    private val _champion: MutableLiveData<DataResult<ChampionDetail?>> = MutableLiveData(DataResult.Loading())
    val champion: LiveData<DataResult<ChampionDetail?>> get() = _champion

    private val synchroStateChampionDetail: MutableStateFlow<DataResult<Unit>> = MutableStateFlow(DataResult.Loading())

    fun getChampionDetail(champId: String, fetchNew: Boolean) {
        if (fetchNew) (fetchChampion(champId))
        viewModelScope.launch {
            combine(repository.championDetailById(champId), synchroStateChampionDetail) { champion, state ->
                Log.d("observe", "combine")
                when {
                    champion != null -> DataResult.Success(champion)
                    state is DataResult.Loading -> DataResult.Loading(null)
                    else -> DataResult.Failure()
                }
            }.collect {
                _champion.value = it
            }
        }
    }

    fun fetchChampion(champId: String) {
        viewModelScope.launch {
            synchroStateChampionDetail.value = DataResult.Loading()
            repository.fetchChampionDetail(champId)
            delay(DELAY_STATE)
            synchroStateChampionDetail.value = DataResult.Success(Unit)
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            champion.value?.data?.let {
                repository.setFavorite(it.id, !it.isFavorite)
            }
        }
    }

    suspend fun lastVersion(): String = versionDatasource.getVersion()

    companion object {
        const val DELAY_STATE: Long = 2000
    }
}