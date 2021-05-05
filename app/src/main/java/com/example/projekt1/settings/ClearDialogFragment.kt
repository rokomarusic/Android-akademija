package com.example.projekt1.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.projekt1.R
import com.example.projekt1.databinding.DialogLayoutBinding
import com.example.projekt1.viewmodel.LocationViewModel

class ClearDialogFragment(val cities: Boolean, val model: LocationViewModel) : DialogFragment() {


    private var _binding: DialogLayoutBinding? = null

    private val binding get() = _binding!!

    /*override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_layout, container, false)
    }*/

    /*override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setView(it.layoutInflater.inflate(R.layout.dialog_layout, null))
            builder.setMessage(if(cities) R.string.clear_cities else R.string.clear_recent)
                .setPositiveButton(R.string.delete,
                    { dialog, id ->
                        if(cities){
                            model.clearFavouritesDB(context)
                        }else{
                            model.clearRecentDB(context)
                        }
                    })
                .setNegativeButton(R.string.cancel,
                    { dialog, id ->
                        // User cancelled the dialog
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.item_background);
        _binding = DialogLayoutBinding.inflate(inflater, container, false)

        binding.tvDialog.text =
            if (cities) resources.getString(R.string.clear_cities) else resources.getString(R.string.clear_recent)
        binding.btnClear.setOnClickListener {
            if (cities) {
                model.clearFavouritesDB(context)
            } else {
                model.clearRecentDB(context)
            }
            dialog?.dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dialog?.dismiss()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}