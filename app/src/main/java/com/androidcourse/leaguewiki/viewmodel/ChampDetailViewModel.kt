package com.androidcourse.leaguewiki.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.androidcourse.leaguewiki.data.ChampionsRepository
import com.androidcourse.leaguewiki.model.ChampionDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChampDetailViewModel @Inject constructor(
    private val repository: ChampionsRepository
) : ViewModel() {
    val champion: MutableStateFlow<ChampionDetail?> = MutableStateFlow(null)

    val isFavorite = repository.getFavorites().map { list ->
        Log.d("observe", "getFav")
        list.firstOrNull { it.first == champion.value?.id }?.second
    }.asLiveData()

    fun getChampionDetail(champId: String) {
        viewModelScope.launch {
            repository.getChampionDetail(champId).collect {
                champion.value = it
            }
        }
    }

    fun setFavorite(isFavorite: Boolean) {
        viewModelScope.launch {
            champion.value?.let {
                Log.d("observe", "setFav")
                repository.setFavorite(it.id, isFavorite)
            }
        }
    }
}