package com.androidcourse.leaguewiki.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidcourse.leaguewiki.data.ChampionsRepository
import com.androidcourse.leaguewiki.data.VersionDatasource
import com.androidcourse.leaguewiki.model.ChampionDetail
import com.androidcourse.leaguewiki.model.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChampDetailViewModel @Inject constructor(
    private val repository: ChampionsRepository,
    private val versionDatasource: VersionDatasource,
) : ViewModel() {

    private val _champion: MutableLiveData<DataResult<ChampionDetail?>> = MutableLiveData(DataResult.Loading())
    val champion: LiveData<DataResult<ChampionDetail?>>
        get() = _champion

    suspend fun lastVersion(): String = versionDatasource.getVersion()

    fun getChampionDetail(champId: String, fetchNew: Boolean) {
        if (fetchNew) (fetchChampion(champId))
        viewModelScope.launch {
            repository.championDetailById(champId).collect {
                _champion.value = it
            }
        }
    }

    fun fetchChampion(champId: String) {
        viewModelScope.launch {
            repository.fetchChampionDetail(champId)
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            champion.value?.data?.let {
                repository.setFavorite(it.id, !it.isFavorite)
            }
        }
    }
}