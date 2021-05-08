package com.example.projekt1.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.projekt1.R
import com.example.projekt1.databinding.DialogLayoutBinding
import com.example.projekt1.viewmodel.LocationViewModel
import com.google.android.material.snackbar.Snackbar

class ClearDialogFragment(val cities: Boolean, val model: LocationViewModel) : DialogFragment() {


    private var _binding: DialogLayoutBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.item_background);
        _binding = DialogLayoutBinding.inflate(inflater, container, false)
        val activity = this.parentFragment

        binding.tvDialog.text =
            if (cities) resources.getString(R.string.clear_cities) else resources.getString(R.string.clear_recent)
        binding.btnClear.setOnClickListener {
            if (cities) {
                model.clearFavouritesDB(context)
                val snackbar = activity?.view?.let { it1 ->
                    Snackbar.make(
                        it1,
                        getString(R.string.fav_is_clear),
                        Snackbar.LENGTH_LONG
                    )
                }
                if (snackbar != null) {
                    snackbar.setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                    snackbar.show()
                }
            } else {
                model.clearRecentDB(context)
                val snackbar = activity?.view?.let { it1 ->
                    Snackbar.make(
                        it1,
                        getString(R.string.recent_is_clear),
                        Snackbar.LENGTH_LONG
                    )
                }
                if (snackbar != null) {
                    snackbar.setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                    snackbar.show()
                }
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