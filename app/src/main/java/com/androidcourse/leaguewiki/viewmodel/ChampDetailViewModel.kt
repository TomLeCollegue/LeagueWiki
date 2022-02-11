package com.androidcourse.leaguewiki.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.androidcourse.leaguewiki.data.ChampionsRepository
import com.androidcourse.leaguewiki.model.ChampionDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChampDetailViewModel @Inject constructor(
    private val repository: ChampionsRepository
) : ViewModel() {

    private val _champion: MutableStateFlow<ChampionDetail?> = MutableStateFlow(null)
    val champion: StateFlow<ChampionDetail?>
        get() = _champion


    val isFavorite = champion.combine(repository.favorites) { champion, favorites ->
        favorites.firstOrNull { it.first == champion?.id }?.second
    }.asLiveData()

    fun getChampionDetail(champId: String) {
        viewModelScope.launch {
            repository.getChampionDetail(champId).collect {
                _champion.value = null
                _champion.value = it
            }
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