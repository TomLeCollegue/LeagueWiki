package com.androidcourse.leaguewiki

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.androidcourse.leaguewiki.data.ChampionsDataSource
import com.androidcourse.leaguewiki.data.ChampionsRepository
import com.androidcourse.leaguewiki.data.RetrofitClient
import com.androidcourse.leaguewiki.fragment.RecyclerFragment
import com.androidcourse.leaguewiki.items.championListItem
import com.androidcourse.leaguewiki.viewmodel.HomeViewModel
import com.androidcourse.leaguewiki.viewmodel.HomeViewModelFactory
import com.mikepenz.fastadapter.GenericItem
import kotlinx.coroutines.launch

class HomeFragment : RecyclerFragment() {

    private val retrofitClient: RetrofitClient by lazy {
        RetrofitClient(Constants.Server.BASE_URL)
    }

    private val championsDataSource: ChampionsDataSource by lazy {
        ChampionsDataSource(retrofitClient)
    }

    private val championsRepository: ChampionsRepository by lazy {
        ChampionsRepository(championsDataSource)
    }

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(championsRepository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.champions.collect {
                refreshScreen()
            }
        }
    }

    override fun getItems(): List<GenericItem> {
        val items = mutableListOf<GenericItem>()
        viewModel.champions.value?.mapTo(items) {
            championListItem {
                name = it.name
                description = it.title
                urlImage =
                    Constants.Server.BASE_URL + Constants.Server.IMAGE_URL.format(it.name, 0)
                identifier = it.name.hashCode().toLong()
            }
        }
        return items
    }
}
