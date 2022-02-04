package com.androidcourse.leaguewiki.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.androidcourse.leaguewiki.databinding.FragmentRecyclerBinding
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.GenericFastItemAdapter

abstract class RecyclerFragment : Fragment() {

    private var binding: FragmentRecyclerBinding? = null
    protected val fastAdapter = GenericFastItemAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecyclerBinding.inflate(inflater, container, false).apply {
            recyclerView.adapter = fastAdapter
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshScreen()
    }

    open fun refreshScreen() {
        fastAdapter.setNewList(getItems())
    }

    abstract fun getItems(): List<GenericItem>

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}