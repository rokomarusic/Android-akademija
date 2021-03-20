package com.example.projekt1.fruits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt1.R
import com.example.projekt1.databinding.FragmentItemBinding
import com.example.projekt1.viewmodel.FruitViewModel

/**
 * A fragment representing a list of Items.
 */
class FruitFragment2 : Fragment() {

    private var _binding: FragmentItemBinding? = null
    private val binding get() = _binding!!

    private var columnCount = 1
    private val model: FruitViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        _binding = FragmentItemBinding.inflate(inflater)
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)


        val fruitAdapter = model.fruits.value?.let { MyFruitRecyclerViewAdapter(it) }


        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = fruitAdapter
            }
        }

        model.fruits.observe(viewLifecycleOwner, Observer {
            fruitAdapter?.notifyItemInserted(it.size - 1)
        })
        return view
    }

    override fun onResume() {
        super.onResume()

        println("RESUMED FRUIT FRAGMENT")
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
                FruitFragment2().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }
    }
}