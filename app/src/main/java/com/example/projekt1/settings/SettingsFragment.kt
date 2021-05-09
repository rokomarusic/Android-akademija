package com.example.projekt1.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.projekt1.R
import com.example.projekt1.databinding.FragmentSettingsBinding
import com.example.projekt1.locale.MyPreference
import com.example.projekt1.viewmodel.LocationViewModel


class SettingsFragment : Fragment() {

    lateinit var preference: MyPreference

    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    private var firstSelection = true

    private val model: LocationViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root

        preference = MyPreference(binding.root.context)

        /*val lang = preference.getLang()
        if (lang.equals("hr")) {
            binding.langGroup.check(binding.croBtn.id)
        } else {
            binding.langGroup.check(binding.engBtn.id)
        }


        binding.btnLang.setOnClickListener {
            preference.setLang(if (binding.croBtn.isChecked) "hr" else "en")
            activity?.recreate()
        }*/

        binding.langSpinner.adapter = context?.resources?.let {
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                it.getStringArray(R.array.langs)
            )
        }

        var selectedLang = preference.getLang()
        if (selectedLang.equals("en")) {
            binding.langSpinner.setSelection(0)
        } else {
            binding.langSpinner.setSelection(1)
            selectedLang = "hr"
        }
        binding.langSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (firstSelection) {
                    firstSelection = false
                } else {
                    val lang = parent?.getItemAtPosition(position) as String
                    val chosenLang = when (lang) {
                        context?.getString(R.string.cro) -> "hr"
                        context?.getString(R.string.eng) -> "en"
                        else -> ""
                    }
                    if (!selectedLang?.equals(chosenLang)!!) {
                        preference.setLang(if (chosenLang.equals("hr")) "hr" else "en")
                        activity?.recreate()
                    }


                }
            }

        }

        binding.clearCitiesButton.setOnClickListener {
            val dialogFragment = ClearDialogFragment(true, model)
            dialogFragment.show(childFragmentManager, "")

        }

        binding.clearRecentButton.setOnClickListener {
            val dialogFragment = ClearDialogFragment(false, model)
            dialogFragment.show(childFragmentManager, "")
        }

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            model.metric.value = (checkedId == binding.radioMetric.id)
        }

        binding.buttonInfo.setOnClickListener {
            val intent = Intent(context, AboutActivity::class.java)
            context?.startActivity(intent)
        }



        return view
    }


}