package com.androidcourse.leaguewiki.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.androidcourse.leaguewiki.Constants
import com.androidcourse.leaguewiki.R
import com.androidcourse.leaguewiki.databinding.FragmentChampDetailBinding
import com.androidcourse.leaguewiki.items.SpaceItem
import com.androidcourse.leaguewiki.items.captionItem
import com.androidcourse.leaguewiki.items.championDetailShimmerItem
import com.androidcourse.leaguewiki.items.emptyScreenItem
import com.androidcourse.leaguewiki.items.horizontalRecyclerItem
import com.androidcourse.leaguewiki.items.refreshItem
import com.androidcourse.leaguewiki.items.sectionTitleItem
import com.androidcourse.leaguewiki.items.skinItem
import com.androidcourse.leaguewiki.items.spaceItem
import com.androidcourse.leaguewiki.items.spellItem
import com.androidcourse.leaguewiki.items.tagItem
import com.androidcourse.leaguewiki.items.titleItem
import com.androidcourse.leaguewiki.model.ChampionDetail
import com.androidcourse.leaguewiki.model.DataResult
import com.androidcourse.leaguewiki.model.KeySpell
import com.androidcourse.leaguewiki.viewmodel.ChampDetailViewModel
import com.bumptech.glide.Glide
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.GenericFastItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChampDetailFragment : Fragment() {

    private val recyclerViewPool = RecyclerView.RecycledViewPool()
    private val viewModel: ChampDetailViewModel by viewModels()
    private val args: ChampDetailFragmentArgs by navArgs()
    private var binding: FragmentChampDetailBinding? = null
    private var menuItem: MenuItem? = null
    private val fastAdapter = GenericFastItemAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChampDetailBinding.inflate(inflater, container, false).apply {
            recyclerView.adapter = fastAdapter
            recyclerView.setRecycledViewPool(recyclerViewPool)
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = args.namechamp
        setHasOptionsMenu(true)

        args.idChamp?.let { viewModel.getChampionDetail(it) }
        viewModel.champion.observe(viewLifecycleOwner) {
            refreshScreen()
        }
        setImageToolBar()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.champ_detail_menu, menu)
        menuItem = menu.findItem(R.id.heart)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.heart) {
            viewModel.toggleFavorite()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getItems(lastVersion: String): List<GenericItem> {
        val items = mutableListOf<GenericItem>()
        val championDetail = viewModel.champion.value?.data
        items += spaceItem {
            spaceRes = R.dimen.spacing_large
        }
        items += horizontalRecyclerItem {
            val tagList = mutableListOf<GenericItem>()
            championDetail?.tags?.forEach {
                tagList += spaceItem {
                    spaceRes = R.dimen.spacing_large
                    orientation = SpaceItem.Orientation.HORIZONTAL
                    identifier = tagList.size.toLong()
                }
                tagList += tagItem {
                    text = it
                    identifier = text.hashCode().toLong()
                }
            }
            tagList += spaceItem {
                spaceRes = R.dimen.spacing_large
                orientation = SpaceItem.Orientation.HORIZONTAL
            }
            viewPool = recyclerViewPool
            itemsList = tagList
            identifier = "tags".hashCode().toLong()
        }

        if (lastVersion != championDetail?.version) {
            items += spaceItem {
                spaceRes = R.dimen.spacing_large
            }

            items += refreshItem {
                onClick = View.OnClickListener {
                    args.idChamp?.let { id -> viewModel.fetchChampion(id) }
                }
                identifier = "refresh".hashCode().toLong()
            }
        }

        items += sectionTitleItem {
            title = getString(R.string.champ_detail_screen_lore_section)
            identifier = title.hashCode().toLong()
        }
        items += titleItem {
            text = championDetail?.title
            identifier = text.hashCode().toLong()
        }

        items += captionItem {
            text = championDetail?.lore
            maxLine = 4
            identifier = text.hashCode().toLong()
            onClick = View.OnClickListener {
                findNavController().navigate(
                    ChampDetailFragmentDirections.actionChampDetailFragmentToDetailBottomSheetFragment(
                        args.idChamp,
                        DetailBottomSheetFragment.InfoToDisplay.LORE
                    )
                )
            }
        }

        items += sectionTitleItem {
            title = getString(R.string.champ_detail_screen_passive_section)
            identifier = title.hashCode().toLong()
        }

        items += spellItem {
            title = championDetail?.passive?.name
            urlImage =
                Constants.Server.BASE_URL + Constants.Server.IMAGE_PASSIVE_URL.format(championDetail?.passive?.image)
            identifier = title.hashCode().toLong()
            onClickCard = View.OnClickListener {
                findNavController().navigate(
                    ChampDetailFragmentDirections.actionChampDetailFragmentToDetailBottomSheetFragment(
                        args.idChamp,
                        DetailBottomSheetFragment.InfoToDisplay.PASSIVE,
                    )
                )
            }
        }

        items += sectionTitleItem {
            title = getString(R.string.champ_detail_screen_abilities_section)
            identifier = title.hashCode().toLong()
        }

        championDetail?.spells?.mapIndexedTo(items) { index, spell ->
            spellItem {
                title = spell.name
                urlImage =
                    Constants.Server.BASE_URL + Constants.Server.IMAGE_SPELL_URL.format(spell.image)
                spellKey = KeySpell.values().find { it.index == index }
                title.hashCode().toLong()
                onClickCard = View.OnClickListener {
                    findNavController().navigate(
                        ChampDetailFragmentDirections.actionChampDetailFragmentToDetailBottomSheetFragment(
                            args.idChamp,
                            DetailBottomSheetFragment.InfoToDisplay.values().first { it.index == index },
                        )
                    )
                }
                identifier = title.hashCode().toLong()
            }
        }

        items += sectionTitleItem {
            title = getString(R.string.champ_detail_screen_skins_section)
            identifier = title.hashCode().toLong()
        }

        items += horizontalRecyclerItem {
            itemsList = getSkinsItems(championDetail)
            viewPool = recyclerViewPool
            isPager = true
            identifier = "skins".hashCode().toLong()
        }

        return items
    }

    private fun getSkinsItems(championDetail: ChampionDetail?): List<GenericItem> {
        val items = mutableListOf<GenericItem>()
        championDetail?.skins?.filter { it.name != Constants.Champions.DEFAULT_SKIN_NAME }?.forEach {
            items += skinItem {
                name = it.name
                urlImage = Constants.Server.BASE_URL + Constants.Server.IMAGE_SPASH_URL.format(
                    args.idChamp,
                    it.num
                )
                identifier = name.hashCode().toLong()
            }
        }

        return items
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun refreshScreen() {
        val drawable = if (viewModel.champion.value?.data?.isFavorite == true) {
            R.drawable.ic_filled_heart
        } else {
            R.drawable.ic_empty_heart
        }
        menuItem?.icon = ContextCompat.getDrawable(requireContext(), drawable)

        when (viewModel.champion.value) {
            is DataResult.Loading, null -> displayShimmerScreen()
            is DataResult.Success -> displayChampionDetail()
            is DataResult.Failure -> displayOfflineScreen()
        }

    }

    private fun displayShimmerScreen() {
        val items = mutableListOf<GenericItem>()
        items += championDetailShimmerItem {
            identifier = "shimmer".hashCode().toLong()
        }
        fastAdapter.setNewList(items)
    }

    private fun displayOfflineScreen() {
        val items = mutableListOf<GenericItem>()
        items += spaceItem {
            spaceRes = R.dimen.spacing_large
        }
        items += emptyScreenItem {
            onClickRefresh = View.OnClickListener {
                args.idChamp?.let { id -> viewModel.fetchChampion(id) }
            }
        }
        fastAdapter.setNewList(items)
    }

    private fun displayChampionDetail() {
        setImageToolBar()
        lifecycleScope.launch {
            fastAdapter.setNewList(getItems(viewModel.lastVersion()))

        }
    }

    private fun setImageToolBar() {
        val urlImage = Constants.Server.BASE_URL + Constants.Server.IMAGE_SPASH_URL.format(args.idChamp, 0)
        binding?.toolbarImageView?.let {
            Glide.with(requireContext()).load(urlImage).into(it)
        }
    }

}