package com.androidcourse.leaguewiki.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.androidcourse.leaguewiki.Constants
import com.androidcourse.leaguewiki.R
import com.androidcourse.leaguewiki.databinding.FragmentHomeBinding
import com.androidcourse.leaguewiki.items.ChampionListItem
import com.androidcourse.leaguewiki.items.championListItem
import com.androidcourse.leaguewiki.items.titleItem
import com.androidcourse.leaguewiki.model.ChampionInfo
import com.androidcourse.leaguewiki.viewmodel.HomeViewModel
import com.mikepenz.fastadapter.GenericItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : RecyclerFragment() {

    private val viewModel by viewModels<HomeViewModel>()

    var binding: FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            recyclerView.adapter = fastAdapter
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = "LeagueWiki"
        setHasOptionsMenu(true)

        lifecycleScope.launch {
            viewModel.research.collect {
                refreshScreen()
            }
        }
        viewModel.champions.observe(viewLifecycleOwner) {
            Log.d("observe", "refresh fragment")
            refreshScreen()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        val searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView
        searchView.queryHint = "Rechercher"
        searchView.setQuery(viewModel.research.value, false)
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(string: String?): Boolean {
                viewModel.setResearch(string ?: "")
                return false
            }

        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun getItems(): List<GenericItem> {
        val items = mutableListOf<GenericItem>()

        viewModel.champions.value?.filter {
            it.isFavorite && it.name?.contains(viewModel.research.value, ignoreCase = true) == true
        }?.let {
            if (!it.isNullOrEmpty()) {
                items += titleItem {
                    text = "Champions Favoris"
                    identifier = text.hashCode().toLong()
                }
                it.mapTo(items) { champ ->
                    getChampionItem(true, champ)
                }
                items += titleItem {
                    text = "Tous les champions"
                    identifier = text.hashCode().toLong()
                }
            }
        }

        viewModel.champions.value?.filter {
            it.name?.contains(viewModel.research.value, ignoreCase = true) == true
        }?.mapTo(items) { champ ->
            getChampionItem(false, champ)
        }

        return items
    }

    private fun getChampionItem(isFav: Boolean, champ: ChampionInfo): ChampionListItem {
        return championListItem {
            name = champ.name
            description = champ.title
            urlImage =
                Constants.Server.BASE_URL + Constants.Server.IMAGE_CHAMP_URL.format(champ.id)
            identifier = "${champ.name}$isFav".hashCode().toLong()
            onClickCard = View.OnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToChampDetailFragment(champ.id)
                )
            }
            onFavoriteClick = View.OnClickListener {
                champ.id?.let { it1 -> viewModel.setFavorite(it1, !champ.isFavorite) }
            }
            isFavorite = champ.isFavorite
        }
    }
}
