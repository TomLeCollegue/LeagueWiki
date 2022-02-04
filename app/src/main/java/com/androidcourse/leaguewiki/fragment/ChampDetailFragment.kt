package com.androidcourse.leaguewiki.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.androidcourse.leaguewiki.Constants
import com.androidcourse.leaguewiki.databinding.FragmentChampDetailBinding
import com.androidcourse.leaguewiki.items.championListItem
import com.androidcourse.leaguewiki.viewmodel.ChampDetailViewModel
import com.bumptech.glide.Glide
import com.mikepenz.fastadapter.GenericItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChampDetailFragment : RecyclerFragment() {

    private val viewModel by viewModels<ChampDetailViewModel>()
    private val args: ChampDetailFragmentArgs by navArgs()
    var binding: FragmentChampDetailBinding? = null

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
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = args.idChamp

        val urlImage =
            Constants.Server.BASE_URL + Constants.Server.IMAGE_SPASH_URL.format(args.idChamp, 0)
        binding?.toolbarImageView?.let {
            Glide.with(requireContext()).load(urlImage).into(it)
        }
    }

    override fun getItems(): List<GenericItem> {
        val items = mutableListOf<GenericItem>()
        viewModel.champion.value?.spells?.mapTo(items) {
            championListItem {
                name = it.name
                description = it.description
                urlImage = Constants.Server.BASE_URL + Constants.Server.IMAGE_SPELL_URL.format(it.image)
            }
        }
        return items
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}