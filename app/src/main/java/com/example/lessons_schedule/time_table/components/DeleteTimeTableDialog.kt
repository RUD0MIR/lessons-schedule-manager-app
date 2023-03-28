package com.example.lessons_schedule.time_table.components

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.lessons_schedule.R
import com.example.lessons_schedule.data.model.TimeTableItem
import com.example.lessons_schedule.time_table.TimeTableViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DeleteTimeTableDialog(
    private val timeTableItem: TimeTableItem,
    private val onSubmitClick: () -> Unit
    ): DialogFragment() {
    private val model: TimeTableViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete_lesson_dialog_title))
            .setNeutralButton(resources.getString(R.string.cancel_option)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.delete_option)) { _, _ ->
                onSubmitClick()
                model.deleteTimeTableItem(timeTableItem)
            }
            .create()



        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    companion object {
        const val TAG = "DeleteTimeTableDialog"
    }
}