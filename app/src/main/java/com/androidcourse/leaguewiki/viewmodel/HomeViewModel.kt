package com.androidcourse.leaguewiki.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.androidcourse.leaguewiki.data.ChampionsRepository
import com.androidcourse.leaguewiki.model.ChampionInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val championsRepository: ChampionsRepository
) : ViewModel() {

    private val _research: MutableStateFlow<String> = MutableStateFlow("")
    val research: StateFlow<String>
        get() = _research

    init {
        updateChamps()
    }

    fun updateChamps() {
        viewModelScope.launch {
            championsRepository.onAppForeground()
        }
    }

    val champions: LiveData<List<ChampionInfo>?> = championsRepository.championsList.asLiveData()

    fun setResearch(value: String) {
        _research.value = value
    }

    fun setFavorite(idChamp: String, isFavorite: Boolean) {
        viewModelScope.launch {
            championsRepository.setFavorite(idChamp, isFavorite)
        }
    }

}
