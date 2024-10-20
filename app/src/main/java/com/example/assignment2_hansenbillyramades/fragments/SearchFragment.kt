package com.example.assignment2_hansenbillyramades.fragments

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment2_hansenbillyramades.data.DiaryDatabase
import com.example.assignment2_hansenbillyramades.data.DiaryEntity
import com.example.assignment2_hansenbillyramades.ItemDiaryListener
import com.example.assignment2_hansenbillyramades.R
import com.example.assignment2_hansenbillyramades.activity.DiaryDetailActivity
import com.example.assignment2_hansenbillyramades.adapters.ItemDiaryAdapter
import com.example.assignment2_hansenbillyramades.databinding.FragmentSearchBinding
import kotlinx.coroutines.launch

class SearchFragment : Fragment(), ItemDiaryListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: DiaryDatabase
    private lateinit var adapter: ItemDiaryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = DiaryDatabase.getDatabase(requireContext())
        setupRecyclerView()
        loadDiaries()

        binding.svSearchDiary.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(search: String?): Boolean {
                if (search != null) {
                    searchDiary(search)
                }
                return true
            }

            override fun onQueryTextChange(newSearch: String?): Boolean {
                if (newSearch != null) {
                    searchDiary(newSearch)
                }
                return true
            }
        })
    }

    private fun setupRecyclerView() {
        adapter = ItemDiaryAdapter(emptyList(), this, false)
        binding.rvDiary.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDiary.adapter = adapter
    }

    private fun loadDiaries() {
        lifecycleScope.launch {
            val diaryList = db.diaryDao().getDiary()
            adapter.updateData(diaryList)
        }
    }

    private fun searchDiary(query: String) {
        if (query.isNotEmpty()) {
            lifecycleScope.launch {
                val filteredDiaries = db.diaryDao().searchByName(query)
                adapter.updateData(filteredDiaries)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(diary: DiaryEntity) {
        val intent = Intent(requireContext(), DiaryDetailActivity::class.java).apply {
            putExtra("diary", diary)
        }
        startActivity(
            intent, ActivityOptions.makeCustomAnimation(
                requireContext(),
                R.anim.slide_right_enter, R.anim.slide_right_exit
            ).toBundle()
        )
    }

    override fun onDelete(diary: DiaryEntity) {
        //TODO: Implement delete functionality
    }

    override fun onEdit(diary: DiaryEntity) {
        //TODO: Implement edit functionality
    }
}
