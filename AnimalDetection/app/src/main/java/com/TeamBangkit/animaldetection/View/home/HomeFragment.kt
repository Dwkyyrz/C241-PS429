package com.TeamBangkit.animaldetection.View.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.TeamBangkit.animaldetection.Animal
import com.TeamBangkit.animaldetection.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var dashboardViewModel: HomeViewModel
    private lateinit var rvAnimal: RecyclerView
    private lateinit var listAnimalAdapter: ListAnimalAdapter
    private val list = ArrayList<Animal>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        rvAnimal = binding.rvAnimal
        rvAnimal.layoutManager = GridLayoutManager(context, 2)
        listAnimalAdapter = ListAnimalAdapter(list)
        rvAnimal.adapter = listAnimalAdapter

        homeViewModel.animals.observe(viewLifecycleOwner) { animals ->
            if (animals != null) {
                list.clear()
                list.addAll(animals)
                listAnimalAdapter.notifyDataSetChanged()
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}