package com.androidcourse.leaguewiki.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.androidcourse.leaguewiki.Constants
import com.androidcourse.leaguewiki.R
import com.androidcourse.leaguewiki.databinding.FragmentRecyclerBinding
import com.androidcourse.leaguewiki.extensions.clearTags
import com.androidcourse.leaguewiki.items.*
import com.androidcourse.leaguewiki.model.Passive
import com.androidcourse.leaguewiki.model.Spell
import com.androidcourse.leaguewiki.viewmodel.ChampDetailViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.GenericFastItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailBottomSheetFragment: BottomSheetDialogFragment() {

    private val viewModel by navGraphViewModels<ChampDetailViewModel>(R.id.nav_main)
    var spell: Spell? = null

    val args: DetailBottomSheetFragmentArgs by navArgs()

    var binding: FragmentRecyclerBinding? = null
    val fastAdapter = GenericFastItemAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecyclerBinding.inflate(inflater, container, false).apply {
            recyclerView.adapter = fastAdapter
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.champion.collect { champ ->
                if(!args.isLore) {
                    spell = champ?.spells?.get(args.spellIndex)
                }
                refreshScreen()
            }
        }
    }

    private fun refreshScreen() {
        fastAdapter.setNewList(getItem())
    }

    private fun getItem(): List<GenericItem> {
        return when {
            spell != null -> getSpellItem()
            args.isLore -> getLoreItem()
            else -> listOf()
        }
    }

    private fun getLoreItem(): List<GenericItem> {
        val items = mutableListOf<GenericItem>()
        items += titleSpellItem {
            title = viewModel.champion.value?.title
            urlImage = Constants.Server.BASE_URL + Constants.Server.IMAGE_CHAMP_URL.format(viewModel.champion.value?.id)
        }
        items += captionItem {
            text = viewModel.champion.value?.lore?.clearTags()
        }
        items += spaceItem {
            spaceRes = R.dimen.spacing_large
        }
        return items
    }

    private fun getSpellItem(): List<GenericItem> {
        val items = mutableListOf<GenericItem>()
        items += titleSpellItem {
            title = spell?.name
            urlImage = Constants.Server.BASE_URL + Constants.Server.IMAGE_SPELL_URL.format(spell?.image)
            spellKey = SpellItem.KeySpell.values().first { it.index == args.spellIndex }
        }
        items += captionItem {
            text = spell?.description?.clearTags()
        }
        items += spaceItem {
            spaceRes = R.dimen.spacing_large
        }
        items += statItem {
            icRes = R.drawable.ic_hourglass
            text = spell?.cooldownBurn
        }
        items += spaceItem {
            spaceRes = R.dimen.spacing_large
        }

        if(spell?.cooldownBurn != "0") {
            items += statItem {
                icRes = R.drawable.ic_tear
                text = spell?.costBurn
            }
            items += spaceItem {
                spaceRes = R.dimen.spacing_large
            }
        }
        items += statItem {
            icRes = R.drawable.ic_bow
            text = spell?.rangeBurn
        }
        items += spaceItem {
            spaceRes = R.dimen.spacing_large
        }
        items += spaceItem {
            spaceRes = R.dimen.spacing_large
        }
        return items
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


}