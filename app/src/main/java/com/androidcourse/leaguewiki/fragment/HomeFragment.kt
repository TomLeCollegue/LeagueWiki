package com.androidcourse.leaguewiki.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
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
import com.androidcourse.leaguewiki.model.DataResult
import com.androidcourse.leaguewiki.viewmodel.HomeViewModel
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.GenericFastItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel>()

    var binding: FragmentHomeBinding? = null
    private val fastAdapter = GenericFastItemAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            emptyLayout.root.isVisible = false
            recyclerView.adapter = fastAdapter
            shimmerLayout.shimmerFrame.startShimmer()
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.home_screen_title_toolbar)
        setHasOptionsMenu(true)

        lifecycleScope.launch {
            viewModel.research.collect {
                refreshScreen()
            }
        }

        viewModel.champions.observe(viewLifecycleOwner) {
            refreshScreen()
        }

        binding?.emptyLayout?.refreshButton?.setOnClickListener {
            viewModel.updateChamps()
        }
    }

    private fun refreshScreen() {
        when(viewModel.champions.value) {
            is DataResult.Loading, null -> displayShimmer()
            is DataResult.Success -> displayListChamp()
            is DataResult.Failure -> displayOfflineScreen()
        }
    }

    private fun displayOfflineScreen() {
        binding?.emptyLayout?.root?.isVisible = true
        binding?.recyclerView?.isVisible = false
        binding?.shimmerLayout?.shimmerFrame?.stopShimmer()
        binding?.shimmerLayout?.root?.isVisible = false
    }

    private fun displayListChamp() {
        binding?.emptyLayout?.root?.isVisible = false
        binding?.recyclerView?.isVisible = true
        binding?.shimmerLayout?.shimmerFrame?.stopShimmer()
        binding?.shimmerLayout?.root?.isVisible = false
        fastAdapter.setNewList(getItems())
    }

    private fun displayShimmer() {
        binding?.emptyLayout?.root?.isVisible = false
        binding?.recyclerView?.isVisible = false
        binding?.shimmerLayout?.shimmerFrame?.startShimmer()
        binding?.shimmerLayout?.root?.isVisible = true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        val searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView
        searchView.queryHint = getString(R.string.home_screen_reseach)
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

    private fun getItems(): List<GenericItem> {
        val items = mutableListOf<GenericItem>()

        viewModel.champions.value?.data?.filter {
            it.isFavorite && it.name.contains(viewModel.research.value, ignoreCase = true)
        }?.let {
            if (!it.isNullOrEmpty()) {
                items += titleItem {
                    text = getString(R.string.home_screen_fav_champ_section)
                    identifier = text.hashCode().toLong()
                }
                it.mapTo(items) { champ ->
                    getChampionItem(true, champ)
                }
                items += titleItem {
                    text = getString(R.string.home_screen_all_champ_section)
                    identifier = text.hashCode().toLong()
                }
            }
        }

        viewModel.champions.value?.data?.filter {
            it.name.contains(viewModel.research.value, ignoreCase = true)
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
                    HomeFragmentDirections.actionHomeFragmentToChampDetailFragment(champ.id, champ.name)
                )
            }
            onFavoriteClick = View.OnClickListener {
                champ.id.let { it1 -> viewModel.setFavorite(it1, !champ.isFavorite) }
            }
            isFavorite = champ.isFavorite
        }
    }
}
