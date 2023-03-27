package com.example.lessons_schedule.schedule.components

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.lessons_schedule.R
import com.example.lessons_schedule.data.model.ScheduleItem
import com.example.lessons_schedule.schedule.ScheduleViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DeleteScheduleDialog(
    private val scheduleItem: ScheduleItem
): DialogFragment() {
    private val model: ScheduleViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete_lesson_dialog_title))
            .setNeutralButton(resources.getString(R.string.cancel_option)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.delete_option)) { _, _ ->
                model.deleteScheduleItem(scheduleItem)
            }
            .create()

        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    companion object {
        const val TAG = "DeleteScheduleDialog"
    }
}