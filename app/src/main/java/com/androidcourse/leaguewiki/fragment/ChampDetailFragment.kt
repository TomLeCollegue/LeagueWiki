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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import com.androidcourse.leaguewiki.Constants
import com.androidcourse.leaguewiki.R
import com.androidcourse.leaguewiki.databinding.FragmentChampDetailBinding
import com.androidcourse.leaguewiki.extensions.clearTags
import com.androidcourse.leaguewiki.items.*
import com.androidcourse.leaguewiki.viewmodel.ChampDetailViewModel
import com.bumptech.glide.Glide
import com.mikepenz.fastadapter.GenericItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChampDetailFragment : RecyclerFragment() {

    private val recyclerViewPool = RecyclerView.RecycledViewPool()

    private val viewModel by navGraphViewModels<ChampDetailViewModel>(R.id.nav_main) {
        defaultViewModelProviderFactory
    }

    private val args: ChampDetailFragmentArgs by navArgs()
    var binding: FragmentChampDetailBinding? = null
    var menuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        args.idChamp?.let { viewModel.getChampionDetail(it) }
        lifecycleScope.launch {
            viewModel.champion.collect {
                refreshScreen()
            }
        }
    }

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
        (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = args.idChamp
        setHasOptionsMenu(true)

        viewModel.isFavorite.observe(viewLifecycleOwner) {
            refreshScreen()
        }
        val urlImage = Constants.Server.BASE_URL + Constants.Server.IMAGE_SPASH_URL.format(args.idChamp, 0)

        binding?.toolbarImageView?.let {
            Glide.with(requireContext()).load(urlImage).into(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.champ_detail_menu, menu)
        menuItem = menu.findItem(R.id.heart)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.heart) {
            viewModel.setFavorite(!(viewModel.isFavorite.value ?: false))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getItems(): List<GenericItem> {
        val items = mutableListOf<GenericItem>()

        items += spaceItem {
            spaceRes = R.dimen.spacing_large
        }
        items += horizontalRecyclerItem {
            val tagList = mutableListOf<GenericItem>()
            viewModel.champion.value?.tags?.forEach {
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

        items += sectionTitleItem {
            title = "Lore"
            identifier = title.hashCode().toLong()
        }
        items += titleItem {
            text = viewModel.champion.value?.title
            identifier = text.hashCode().toLong()
        }

        items += captionItem {
            text = viewModel.champion.value?.lore
            maxLine = 4
            identifier = text.hashCode().toLong()
            onClick = View.OnClickListener {
                findNavController().navigate(
                    ChampDetailFragmentDirections.actionChampDetailFragmentToDetailBottomSheetFragment(
                        0,
                        true
                    )
                )
            }
        }

        items += sectionTitleItem {
            title = "Passif"
            identifier = title.hashCode().toLong()
        }

        items += spellItem {
            viewModel.champion.value?.passive?.let { passive ->
                title = passive.name
                description = passive.description.clearTags()
                urlImage =
                    Constants.Server.BASE_URL + Constants.Server.IMAGE_PASSIVE_URL.format(passive.image)
                identifier = title.hashCode().toLong()
            }
        }

        items += sectionTitleItem {
            title = "Abilités"
            identifier = title.hashCode().toLong()
        }
        viewModel.champion.value?.spells?.mapIndexedTo(items) { index, spell ->
            spellItem {
                title = spell.name
                urlImage =
                    Constants.Server.BASE_URL + Constants.Server.IMAGE_SPELL_URL.format(spell.image)
                spellKey = SpellItem.KeySpell.values().find { it.index == index }
                title.hashCode().toLong()
                onClickCard = View.OnClickListener {
                    findNavController().navigate(
                        ChampDetailFragmentDirections.actionChampDetailFragmentToDetailBottomSheetFragment(
                            index,
                            false
                        )
                    )
                }
                identifier = title.hashCode().toLong()
            }
        }

        items += sectionTitleItem {
            title = "Skins"
            identifier = title.hashCode().toLong()
        }

        items += horizontalRecyclerItem {
            itemsList = getSkinsItems()
            viewPool = recyclerViewPool
            isPager = true
            identifier = "skins".hashCode().toLong()
        }

        return items
    }

    private fun getSkinsItems(): List<GenericItem> {
        val items = mutableListOf<GenericItem>()
        viewModel.champion.value?.skins?.filter { it.name != "default" }?.forEach {
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

    override fun refreshScreen() {
        super.refreshScreen()
        val drawable = if (viewModel.isFavorite.value == true) R.drawable.ic_filled_heart else R.drawable.ic_empty_heart
        menuItem?.icon = ContextCompat.getDrawable(requireContext(), drawable)
    }

}