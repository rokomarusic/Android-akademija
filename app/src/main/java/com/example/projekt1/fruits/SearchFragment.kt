package com.example.projekt1.fruits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projekt1.databinding.FragmentItemListBinding
import com.example.projekt1.fruits.adapter.LocationAdapter
import com.example.projekt1.viewmodel.*
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.coroutines.*

/**
 * A fragment representing a list of Items.
 */
class SearchFragment : Fragment() {

    private var _binding: FragmentItemListBinding? = null
    private val binding get() = _binding!!

    private var columnCount = 1
    private val model2: LocationViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentItemListBinding.inflate(inflater)
        val view = binding.root


        /*with(binding.list) {
            binding.list.layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            binding.list.adapter = mainAdapter
        }*/

        binding.list.layoutManager = LinearLayoutManager(requireContext())
        model2.locationResponses.observe(viewLifecycleOwner, Observer {
            val adapter = LocationAdapter(requireContext(), it, model2)
            binding.list.adapter = adapter
        })




        binding.etLocation.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                model2.getResponses(binding.etLocation.text.toString(), context)
                println("responses " + model2.locationResponses.value)

            }
            return@setOnEditorActionListener true;
        }

        /*binding.etLocation.addTextChangedListener {
            if(binding.etLocation.text.isNotEmpty()) {
                model2.getResponses(binding.etLocation.text.toString())
                model2.locationResponses.value?.let { retrieveList(it) }
                println("jasifjaiwj " + model2.locationResponses.value)
            }
        }*/




        return view
    }


    override fun onResume() {
        super.onResume()
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }


}