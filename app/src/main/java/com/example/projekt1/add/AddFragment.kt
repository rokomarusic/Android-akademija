package com.example.projekt1.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.projekt1.R
import com.example.projekt1.databinding.FragmentAddBinding
import com.example.projekt1.models.Fruit
import com.example.projekt1.viewmodel.FruitViewModel
import kotlinx.android.synthetic.main.fragment_add.*


class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null

    private val binding get() = _binding!!

    private val model: FruitViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddBinding.inflate(inflater, container, false)
        val view = binding.root
        ArrayAdapter.createFromResource(
            inflater.context,
            R.array.fruitTypes,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.spinner.adapter = adapter
        }

        binding.addFruitBtn.setOnClickListener {

            when {
                binding.fruitNameLayout.etName.text.isEmpty() -> Toast.makeText(
                    inflater.context,
                    R.string.nameEmpty,
                    Toast.LENGTH_SHORT
                ).show()
                binding.fruitPriceLayout.etPrice.text.isEmpty() -> Toast.makeText(
                    inflater.context,
                    R.string.priceEmpty,
                    Toast.LENGTH_SHORT
                ).show()
                binding.fruitQuantityLayout.etQuantity.text.isEmpty() -> Toast.makeText(
                    inflater.context,
                    R.string.quantityEmpty,
                    Toast.LENGTH_SHORT
                ).show()
                binding.fruitColorLayout.etColor.text.isEmpty() -> Toast.makeText(
                    inflater.context,
                    R.string.colorEmpty,
                    Toast.LENGTH_SHORT
                ).show()
                binding.fruitWeightLayout.etWeight.text.isEmpty() -> Toast.makeText(
                    inflater.context,
                    R.string.weightEmpty,
                    Toast.LENGTH_SHORT
                ).show()
                binding.fruitOriginLayout.etOrigin.text.isEmpty() -> Toast.makeText(
                    inflater.context,
                    R.string.originEmpty,
                    Toast.LENGTH_SHORT
                ).show()
                else -> {
                    model.fruits.value?.add(
                        Fruit(
                            binding.fruitNameLayout.etName.text.toString(),
                            binding.fruitPriceLayout.etPrice.text.toString().toDoubleOrNull(),
                            binding.fruitQuantityLayout.etQuantity.text.toString().toIntOrNull(),
                            binding.fruitColorLayout.etColor.text.toString(),
                            binding.fruitWeightLayout.etWeight.text.toString().toDoubleOrNull(),
                            binding.fruitRipeLayout.yesBtn.isChecked,
                            binding.spinner.selectedItem.toString(),
                            binding.fruitOriginLayout.etOrigin.text.toString(),
                            binding.fruitExoticLayout.yesBtn.isChecked,
                            binding.fruitSeasonalLayout.yesBtn.isChecked
                        )
                    )

                    binding.fruitNameLayout.etName.text.clear()
                    binding.fruitQuantityLayout.etQuantity.text.clear()
                    binding.fruitPriceLayout.etPrice.text.clear()
                    binding.fruitColorLayout.etColor.text.clear()
                    binding.fruitWeightLayout.etWeight.text.clear()
                    binding.fruitOriginLayout.etOrigin.text.clear()
                    spinner.setSelection(0)
                    binding.fruitRipeLayout.radioGroup.check(binding.fruitRipeLayout.yesBtn.id)
                    binding.fruitExoticLayout.radioGroup.check(binding.fruitExoticLayout.yesBtn.id)
                    binding.fruitSeasonalLayout.radioGroup.check(binding.fruitSeasonalLayout.yesBtn.id)
                }
            }


        }
        return view
    }


}