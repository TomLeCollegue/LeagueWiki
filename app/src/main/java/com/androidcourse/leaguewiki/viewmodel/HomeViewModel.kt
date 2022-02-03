package com.androidcourse.leaguewiki.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.androidcourse.leaguewiki.data.ChampionsRepository
import com.androidcourse.leaguewiki.model.ChampionInfo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    championsRepository: ChampionsRepository
) : ViewModel() {

    val champions: StateFlow<List<ChampionInfo>?> = championsRepository.getChampionsList()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

}

class HomeViewModelFactory(
    private val championsRepository: ChampionsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(
            championsRepository
        ) as T
    }

}