package com.example.lessons_schedule.schedule.components

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.lessons_schedule.R
import com.example.lessons_schedule.databinding.WeekStateDialogBinding
import com.example.lessons_schedule.schedule.ScheduleActivity
import com.example.lessons_schedule.schedule.ScheduleViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class WeekStateDialog: DialogFragment() {
    private lateinit var binding: WeekStateDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireActivity())

        binding = WeekStateDialogBinding.inflate(layoutInflater)

        builder.setView(binding.root)

        val dialog = builder
            .setView(binding.root)
            .setPositiveButton(R.string.save_option, null)
            .create()

        dialog.setOnShowListener{
            val btnPositive: Button =  dialog.getButton(AlertDialog.BUTTON_POSITIVE)

            btnPositive.setOnClickListener {
                val weekState = binding.weekStateExposedMenu.text.toString()

                if(weekState.isNotBlank()) {
                    val prefs = activity?.getPreferences(Context.MODE_PRIVATE)
                    if(prefs != null) {
                        with (prefs.edit()) {
                            putBoolean(ScheduleActivity.FIRST_START_PREF, false)
                            putString(ScheduleActivity.FIRST_START_PREF, weekState)
                            apply()
                        }
                    }

                    dialog.dismiss()
                } else {
                    binding.weekStateInputLayout.error = resources.getString(R.string.empty_input_error_message)
                }
            }
        }

        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    companion object {
        const val TAG = "WeekStateDialog"
    }
}