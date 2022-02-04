package com.androidcourse.leaguewiki.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.androidcourse.leaguewiki.Constants
import com.androidcourse.leaguewiki.items.championListItem
import com.androidcourse.leaguewiki.items.searchBarItem
import com.androidcourse.leaguewiki.viewmodel.HomeViewModel
import com.mikepenz.fastadapter.GenericItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : RecyclerFragment() {

    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).supportActionBar?.title = "LeagueWiki"
        lifecycleScope.launch {
            viewModel.champions.collect {
                refreshScreen()
            }
        }

        lifecycleScope.launch {
            viewModel.research.collect {
                refreshScreen()
            }
        }
    }

    override fun getItems(): List<GenericItem> {
        val items = mutableListOf<GenericItem>()
        items += searchBarItem {
            onTextChange = {
                viewModel.setResearch(it)
            }
            hint = "Rechercher"
            identifier = "research".hashCode().toLong()
        }

        viewModel.champions.value?.filter {
            it.name?.contains(viewModel.research.value, ignoreCase = true) == true
        }?.mapTo(items) {
            championListItem {
                name = it.name
                description = it.title
                urlImage =
                    Constants.Server.BASE_URL + Constants.Server.IMAGE_CHAMP_URL.format(it.id)
                identifier = it.name.hashCode().toLong()
                onClickCard = View.OnClickListener { _ ->
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToChampDetailFragment(it.id)
                    )
                }
            }
        }

        return items
    }
}
