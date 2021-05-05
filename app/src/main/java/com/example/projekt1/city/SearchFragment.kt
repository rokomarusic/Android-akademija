package com.example.projekt1.city

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projekt1.city.adapter.LocationAdapter
import com.example.projekt1.databinding.FragmentItemListBinding
import com.example.projekt1.models.LocationResponse
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

        //model2.selectFavouritesDB(requireContext())
        //model2.selectRecentDB(requireContext())
        model2.selectLocationDB(requireContext())

        if (model2.locationResponses.value?.isEmpty() == false) {
            binding.recent.visibility = View.GONE
            binding.tvRecent.visibility = View.GONE
        }



        binding.list.layoutManager = LinearLayoutManager(requireContext())
        model2.locationResponses.observe(viewLifecycleOwner, Observer {
            val adapter = LocationAdapter(requireContext(), it, model2)
            binding.list.adapter = adapter
        })


        binding.recent.layoutManager = LinearLayoutManager(requireContext())
        model2.recentDB.observe(viewLifecycleOwner, Observer {
            val adapter = LocationAdapter(requireContext(), it, model2)
            binding.recent.adapter = adapter
        })

        model2.allResponsesDB.observe(viewLifecycleOwner, {

            val favs = it?.filter { it1 -> it1.isFavourite }

            val recents = it?.filter { it1 -> it1.recent > 0 }

            model2.locationResponsesDB.value = favs as ArrayList<LocationResponse>?

            model2.recentDB.value = recents as ArrayList<LocationResponse>?

            val names = it?.map { it1 ->
                it1.title
            }
            val list = ArrayList<String>()
            if (names != null) {
                list.addAll(names)
            }

            println("list123 " + list)
            val adapter = context?.resources?.let {
                ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.select_dialog_item,
                    list
                )
            }

            binding.actv.setAdapter(adapter)

            binding.actv.threshold = 1
        })


        binding.actv.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.recent.visibility = View.GONE
                binding.tvRecent.visibility = View.GONE
                model2.getResponses(binding.actv.text.toString(), context)
                //model2.selectLocationDB(requireContext())
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
        binding.recent.adapter?.notifyDataSetChanged()
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