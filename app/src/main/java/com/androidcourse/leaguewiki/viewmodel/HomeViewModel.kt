package com.androidcourse.leaguewiki.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidcourse.leaguewiki.data.ChampionsRepository
import com.androidcourse.leaguewiki.model.ChampionInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    championsRepository: ChampionsRepository
) : ViewModel() {

    private val _research: MutableStateFlow<String> = MutableStateFlow("")
    val research: StateFlow<String>
        get() = _research

    val champions: StateFlow<List<ChampionInfo>?> = championsRepository.getChampionsList()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun setResearch(value: String) {
        _research.value = value
    }

}
