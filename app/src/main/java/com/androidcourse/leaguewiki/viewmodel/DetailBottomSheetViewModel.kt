package com.androidcourse.leaguewiki.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidcourse.leaguewiki.data.ChampionsRepository
import com.androidcourse.leaguewiki.model.ChampionDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailBottomSheetViewModel @Inject constructor(
    private val repository: ChampionsRepository
) : ViewModel() {
    private val _champion: MutableLiveData<ChampionDetail?> = MutableLiveData(null)
    val champion: LiveData<ChampionDetail?> get() = _champion

    fun getChampionDetail(champId: String) {
        viewModelScope.launch {
            repository.championDetailById(champId).collect {
                _champion.value = it
            }
        }
    }
}