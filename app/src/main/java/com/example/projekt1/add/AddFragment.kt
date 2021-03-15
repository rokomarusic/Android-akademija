package com.example.projekt1.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.projekt1.R
import com.example.projekt1.viewmodel.FruitViewModel


class AddFragment : Fragment() {

    private val model: FruitViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        model.initFruits()

        val view = inflater.inflate(R.layout.fragment_add, container, false)
        val btnAdd: Button = view.findViewById(R.id.addFruitBtn)
        val etName: EditText = view.findViewById(R.id.etName)
        val etQuantity: EditText = view.findViewById(R.id.etQuantity)
        val etPrice: EditText = view.findViewById(R.id.etPrice)
        val etColor: EditText = view.findViewById(R.id.etColor)
        val etWeight: EditText = view.findViewById(R.id.etWeight)

        btnAdd.setOnClickListener {

            model.addFruit(etName.text.toString(),
                    etPrice.text.toString().toDoubleOrNull() ?: 0.0,
                    etQuantity.text.toString().toIntOrNull() ?: 0,
                    etColor.text.toString(),
                    etWeight.text.toString().toDoubleOrNull() ?: 0.0)

            etName.text.clear()
            etQuantity.text.clear()
            etPrice.text.clear()
            etColor.text.clear()
            etWeight.text.clear()

        }
        return view
    }


}