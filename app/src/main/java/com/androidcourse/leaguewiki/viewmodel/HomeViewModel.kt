package com.androidcourse.leaguewiki.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.androidcourse.leaguewiki.data.ChampionsRepository
import com.androidcourse.leaguewiki.model.ChampionInfo
import com.androidcourse.leaguewiki.model.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val championsRepository: ChampionsRepository
) : ViewModel() {

    private val _research: MutableStateFlow<String> = MutableStateFlow("")
    val research: StateFlow<String>
        get() = _research

    private val synchroStateChampionList: MutableStateFlow<DataResult<Unit>> = MutableStateFlow(DataResult.Loading(null))

    init {
        updateChamps()
    }

    fun updateChamps() {
        viewModelScope.launch {
            synchroStateChampionList.value = DataResult.Loading()
            championsRepository.onAppForeground()
            delay(DELAY_STATE)
            synchroStateChampionList.value = DataResult.Success(Unit)
        }
    }

    val champions: LiveData<DataResult<List<ChampionInfo>?>> = combine(
        championsRepository.championsList,
        synchroStateChampionList
    ) { list, state ->
        when {
            !list.isNullOrEmpty() -> DataResult.Success(list.sortedBy { it.name })
            list.isNullOrEmpty() && state is DataResult.Loading -> DataResult.Loading(null)
            else -> DataResult.Failure()
        }
    }.asLiveData()

    fun setResearch(value: String) {
        _research.value = value
    }

    fun setFavorite(idChamp: String, isFavorite: Boolean) {
        viewModelScope.launch {
            championsRepository.setFavorite(idChamp, isFavorite)
        }
    }

    companion object {
        const val DELAY_STATE: Long = 2000
    }

}
