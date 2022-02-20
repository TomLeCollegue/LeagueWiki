package com.androidcourse.leaguewiki.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidcourse.leaguewiki.data.ChampionsRepository
import com.androidcourse.leaguewiki.model.ChampionDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChampDetailViewModel @Inject constructor(
    private val repository: ChampionsRepository
) : ViewModel() {

    private val _champion: MutableStateFlow<ChampionDetail?> = MutableStateFlow(null)
    val champion: StateFlow<ChampionDetail?>
        get() = _champion

    fun getChampionDetail(champId: String, fetchNew: Boolean) {
        if(fetchNew)(
            fetchChampion(champId)
        )
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

    fun setFavorite(isFavorite: Boolean) {
        viewModelScope.launch {
            champion.value?.let {
                repository.setFavorite(it.id, isFavorite)
            }
        }
    }
}