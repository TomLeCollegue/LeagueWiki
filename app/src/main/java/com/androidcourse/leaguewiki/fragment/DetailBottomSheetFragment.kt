package com.androidcourse.leaguewiki.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.androidcourse.leaguewiki.Constants
import com.androidcourse.leaguewiki.R
import com.androidcourse.leaguewiki.databinding.FragmentRecyclerBinding
import com.androidcourse.leaguewiki.extensions.clearTags
import com.androidcourse.leaguewiki.items.captionItem
import com.androidcourse.leaguewiki.items.spaceItem
import com.androidcourse.leaguewiki.items.statItem
import com.androidcourse.leaguewiki.items.titleSpellItem
import com.androidcourse.leaguewiki.items.videoItem
import com.androidcourse.leaguewiki.model.KeySpell
import com.androidcourse.leaguewiki.viewmodel.DetailBottomSheetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.GenericFastItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailBottomSheetFragment : BottomSheetDialogFragment() {

    private val viewModel: DetailBottomSheetViewModel by viewModels()

    private val args: DetailBottomSheetFragmentArgs by navArgs()

    private var binding: FragmentRecyclerBinding? = null
    private val fastAdapter = GenericFastItemAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        args.champId?.let { viewModel.getChampionDetail(it) }
        binding = FragmentRecyclerBinding.inflate(inflater, container, false).apply {
            recyclerView.adapter = fastAdapter
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.champion.observe(viewLifecycleOwner) {
                refreshScreen()
            }
        }
    }

    private fun refreshScreen() {
        fastAdapter.setNewList(getItem())
    }

    private fun getItem(): List<GenericItem> {
        return when (args.infoToDisplay) {
            InfoToDisplay.Q, InfoToDisplay.Z, InfoToDisplay.E, InfoToDisplay.R -> getSpellItem(args.infoToDisplay)
            InfoToDisplay.LORE -> getLoreItem()
            InfoToDisplay.PASSIVE -> getPassiveItem()
        }
    }

    private fun getPassiveItem(): List<GenericItem> {
        val items = mutableListOf<GenericItem>()
        val passive = viewModel.champion.value?.passive
        items += titleSpellItem {
            title = passive?.name
            urlImage =
                Constants.Server.BASE_URL + Constants.Server.IMAGE_PASSIVE_URL.format(passive?.image)
            identifier = "passive.title".hashCode().toLong()
        }
        items += captionItem {
            text = passive?.description?.clearTags()
            identifier = "passive.description".hashCode().toLong()
        }
        items += spaceItem {
            spaceRes = R.dimen.spacing_large
        }
        items += videoItem {
            val key = ("0000" + viewModel.champion.value?.key).takeLast(4)
            urlVideo = Constants.Server.VIDEO_URL_PASSIVE.format(
                key,
                key,
                InfoToDisplay.PASSIVE.keySpell
            )
            identifier = "passive.video".hashCode().toLong()
        }
        items += spaceItem {
            spaceRes = R.dimen.spacing_large
        }
        return items
    }

    private fun getLoreItem(): List<GenericItem> {
        val items = mutableListOf<GenericItem>()
        items += titleSpellItem {
            title = viewModel.champion.value?.title
            urlImage =
                Constants.Server.BASE_URL + Constants.Server.IMAGE_CHAMP_URL.format(viewModel.champion.value?.id)
            identifier = "lore.title".hashCode().toLong()
        }
        items += captionItem {
            text = viewModel.champion.value?.lore?.clearTags()
            identifier = "lore.description".hashCode().toLong()
        }
        items += spaceItem {
            spaceRes = R.dimen.spacing_large
        }
        return items
    }

    private fun getSpellItem(infoToDisplay: InfoToDisplay): List<GenericItem> {
        val spell = viewModel.champion.value?.spells?.get(infoToDisplay.index!!)
        val items = mutableListOf<GenericItem>()
        items += titleSpellItem {
            title = spell?.name
            urlImage =
                Constants.Server.BASE_URL + Constants.Server.IMAGE_SPELL_URL.format(spell?.image)
            spellKey = KeySpell.values().first { it.index == infoToDisplay.index }
            identifier = "spell.title".hashCode().toLong()
        }
        items += captionItem {
            text = spell?.description?.clearTags()
            identifier = "spell.description".hashCode().toLong()
        }
        items += spaceItem {
            spaceRes = R.dimen.spacing_large
        }
        items += statItem {
            icRes = R.drawable.ic_hourglass
            text = spell?.cooldownBurn
            identifier = "spell.cd".hashCode().toLong()
        }
        items += spaceItem {
            spaceRes = R.dimen.spacing_large
        }

        if (spell?.costBurn != "0") {
            items += statItem {
                icRes = R.drawable.ic_tear
                text = spell?.costBurn
                identifier = "spell.cost".hashCode().toLong()
            }
            items += spaceItem {
                spaceRes = R.dimen.spacing_large
            }
        }
        items += statItem {
            icRes = R.drawable.ic_bow
            text = spell?.rangeBurn
            identifier = "spell.range".hashCode().toLong()
        }
        items += spaceItem {
            spaceRes = R.dimen.spacing_large
        }

        items += videoItem {
            val key = ("0000" + viewModel.champion.value?.key).takeLast(4)
            urlVideo = Constants.Server.VIDEO_URL_PASSIVE.format(
                key,
                key,
                infoToDisplay.keySpell
            )
            identifier = "spell.video".hashCode().toLong()
        }

        items += spaceItem {
            spaceRes = R.dimen.spacing_huge
        }
        return items
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    enum class InfoToDisplay(val index: Int? = null, val keySpell: String? = null) {
        LORE,
        PASSIVE(null, "P"),
        Q(0, "Q"),
        Z(1, "W"),
        E(2, "E"),
        R(3, "R"),
    }

}