package com.TeamBangkit.animaldetection.View.History

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.TeamBangkit.animaldetection.Data.DB.ClassificationHistory
import com.TeamBangkit.animaldetection.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var rvAnimal: RecyclerView
    private lateinit var listHistoryAdapter: ListHistoryAdapter
    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        rvAnimal = binding.rvAnimal
        rvAnimal.layoutManager = LinearLayoutManager(context)

        listHistoryAdapter = ListHistoryAdapter(arrayListOf()) { history ->
            deleteHistoryItem(history)
        }
        rvAnimal.adapter = listHistoryAdapter

        historyViewModel.history.observe(viewLifecycleOwner) { history ->
            listHistoryAdapter.updateData(ArrayList(history))
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvAnimal = binding.rvAnimal
        rvAnimal.layoutManager = LinearLayoutManager(context)
        listHistoryAdapter = ListHistoryAdapter(ArrayList()) { history ->
            deleteHistoryItem(history)
        }
        rvAnimal.adapter = listHistoryAdapter

        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        observeViewModel()
    }

    private fun observeViewModel() {
        historyViewModel.history.observe(viewLifecycleOwner) { history ->
            listHistoryAdapter.updateData(ArrayList(history))
        }
    }

    override fun onResume() {
        super.onResume()
        historyViewModel.fetchHistory()
    }

    private fun deleteHistoryItem(history: ClassificationHistory) {
        historyViewModel.deleteHistory(history)
        listHistoryAdapter.removeItem(history)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}