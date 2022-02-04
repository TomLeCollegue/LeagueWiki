package com.androidcourse.leaguewiki.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidcourse.leaguewiki.data.ChampionsRepository
import com.androidcourse.leaguewiki.model.ChampionDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChampDetailViewModel @Inject constructor(
    private val repository: ChampionsRepository
): ViewModel() {
    val champion: MutableStateFlow<ChampionDetail?> = MutableStateFlow(null)

    fun getChampionDetail(champId: String) {
        viewModelScope.launch {
            repository.getChampionDetail(champId).collect {
                champion.value = it
            }
        }
    }
}