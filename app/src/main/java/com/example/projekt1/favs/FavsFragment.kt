package com.example.projekt1.favs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt1.databinding.FragmentAddBinding
import com.example.projekt1.viewmodel.LocationViewModel


class FavsFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null

    private val binding get() = _binding!!

    private val model: LocationViewModel by activityViewModels()

    private var firstSelection = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddBinding.inflate(inflater, container, false)
        val view = binding.root

        println("dB LOCATION " + model.locationResponsesDB.value)

        if (firstSelection) {
            println(" sto je ovo" + firstSelection)
            model.selectFavouritesDB(requireContext())
            firstSelection = false
        }
        println("selected favourites")
        val adapter = model.locationResponsesDB.value?.let { it1 ->
            model.favLocations.value?.let {
                FavsAdapter(
                    requireContext(),
                    it1, it, model, this
                )
            }
        }

        binding.list.layoutManager = LinearLayoutManager(requireContext())
        binding.list.adapter = adapter

        model.favLocations.observe(viewLifecycleOwner, {
            if (adapter != null) {
                adapter.notifyDataSetChanged()
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.list)

        binding.imgEdit.setOnClickListener {
            if (adapter != null) {
                adapter.reorderEnabled = !adapter.reorderEnabled
                adapter.notifyDataSetChanged()
                model.locationResponsesDB.value = adapter.values
                println("model values " + model.locationResponsesDB.value)
            }
        }

        return view
    }

    fun startDragging(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    private val itemTouchHelper by lazy {
        // 1. Note that I am specifying all 4 directions.
        //    Specifying START and END also allows
        //    more organic dragging than just specifying UP and DOWN.
        val simpleItemTouchCallback =
            object : ItemTouchHelper.SimpleCallback(
                UP or
                        DOWN or
                        START or
                        END, 0
            ) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {

                    val adapter = recyclerView.adapter as FavsAdapter
                    val from = viewHolder.adapterPosition
                    val to = target.adapterPosition
                    // 2. Update the backing model. Custom implementation in
                    //    MainRecyclerViewAdapter. You need to implement
                    //    reordering of the backing model inside the method.
                    adapter.moveItem(from, to)
                    // 3. Tell adapter to render the model update.
                    adapter.notifyItemMoved(from, to)
                    return true
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    // 4. Code block for horizontal swipe.
                    //    ItemTouchHelper handles horizontal swipe as well, but
                    //    it is not relevant with reordering. Ignoring here.
                }
            }
        ItemTouchHelper(simpleItemTouchCallback)
    }


    override fun onResume() {
        super.onResume()
        binding.list.adapter?.notifyDataSetChanged()
    }


}