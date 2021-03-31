package com.example.projekt1.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.projekt1.databinding.FragmentSettingsBinding
import com.example.projekt1.locale.MyPreference
import com.example.projekt1.viewmodel.FruitViewModel


class SettingsFragment : Fragment() {

    lateinit var preference: MyPreference

    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    private val model: FruitViewModel by activityViewModels()

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

        val lang = preference.getLang()
        if (lang.equals("hr")) {
            binding.langGroup.check(binding.croBtn.id)
        } else {
            binding.langGroup.check(binding.engBtn.id)
        }


        binding.btnLang.setOnClickListener {
            preference.setLang(if (binding.croBtn.isChecked) "hr" else "en")
            activity?.recreate()
        }

        return view
    }


}