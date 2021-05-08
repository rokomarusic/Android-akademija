package com.example.projekt1.city

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projekt1.city.adapter.LocationAdapter
import com.example.projekt1.databinding.FragmentItemListBinding
import com.example.projekt1.viewmodel.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.coroutines.*
import java.util.*


/**
 * A fragment representing a list of Items.
 */
class SearchFragment : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var _binding: FragmentItemListBinding? = null
    private val binding get() = _binding!!

    private var columnCount = 1
    private val model2: LocationViewModel by activityViewModels()
    private var latt: Double = 0.0
    private var long: Double = 0.0

    private var firstSelection = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ConstraintLayout {


        _binding = FragmentItemListBinding.inflate(inflater)
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this.requireActivity())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLocation()
            println("LOCATION ALLOWED")
        } else {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                44
            )
            println("LOCATION DENIED")
        }


        /*with(binding.list) {
            binding.list.layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            binding.list.adapter = mainAdapter
        }*/

        model2.selectFavouritesDB(requireContext())
        model2.selectRecentDB(requireContext())
        //model2.selectLocationDB(requireContext())

        if (model2.locationResponses.value?.isEmpty() == false) {
            binding.recent.visibility = View.GONE
            binding.tvRecent.visibility = View.GONE
        }



        binding.list.layoutManager = LinearLayoutManager(requireContext())
        model2.locations.observe(viewLifecycleOwner, Observer {
            val adapter = model2.locationResponses.value?.let { it1 ->
                LocationAdapter(
                    requireContext(),
                    it1, it, model2, 0.0, 0.0
                )
            }
            binding.list.adapter = adapter



            binding.recent.visibility = View.GONE
            binding.tvRecent.visibility = View.GONE
        })


        binding.recent.layoutManager = LinearLayoutManager(requireContext())
        model2.recentLocations.observe(viewLifecycleOwner, Observer {
            val adapter = model2.recentDB.value?.let { it1 ->
                LocationAdapter(
                    requireContext(),
                    it1, it, model2, latt, long
                )
            }
            binding.recent.adapter = adapter
        })


        /*model2.allResponsesDB.observe(viewLifecycleOwner, {

            val favs = it?.filter { it1 -> it1.isFavourite }

            val recents = it?.filter { it1 -> it1.recent > 0 }

            if (firstSelection) {

                model2.locationResponsesDB.value = favs as ArrayList<LocationResponse>?
                firstSelection = false
            }

            model2.recentDB.value = recents as ArrayList<LocationResponse>?


        })*/


        model2.hints.observe(viewLifecycleOwner, {
            println("observing " + it.map { it1 -> it1.title })
            val adapterActv = context?.resources?.let {
                ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.select_dialog_item,
                    model2.hints.value?.map { it -> it.title } as ArrayList<String>
                )
            }

            binding.actv.setAdapter(adapterActv)
        })



        binding.actv.threshold = 3

        var thresholdReached: Boolean = false
        binding.actv.addTextChangedListener {
            println("binding text " + thresholdReached + " " + binding.actv.text + " " + binding.actv.enoughToFilter())
            if (!binding.actv.enoughToFilter()) {
                thresholdReached = false
            }
            if (binding.actv.enoughToFilter()) {
                if (!thresholdReached) {
                    println("ovdje")
                    model2.getHints(binding.actv.text.toString(), context)
                    thresholdReached = true
                } else {
                    model2.hints.value?.filter { it -> it.title.startsWith(binding.actv.text.toString()) }
                }
            }

        }

        binding.actv.setOnItemClickListener { parent, view, position, id ->
            val item = parent.getItemAtPosition(position)
            println("itemA" + item + "A")
            model2.getLocations(item.toString(), requireContext())

        }

        binding.actv.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                model2.getLocations(v.text.toString(), requireContext())

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




        return binding.root
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationClient.lastLocation.addOnCompleteListener {
            val location = it.getResult()
            if (location != null) {
                this.latt = location.latitude
                this.long = location.longitude
            }
        }
    }


    override fun onResume() {
        super.onResume()
        binding.recent.adapter?.notifyDataSetChanged()
        binding.list.adapter?.notifyDataSetChanged()
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