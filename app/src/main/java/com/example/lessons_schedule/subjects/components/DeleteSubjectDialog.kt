package com.example.lessons_schedule.subjects.components

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.lessons_schedule.R
import com.example.lessons_schedule.data.model.Subject
import com.example.lessons_schedule.subjects.SubjectsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.FieldPosition

class DeleteSubjectDialog(
    private val subject: Subject
): DialogFragment() {
    private val model: SubjectsViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete_subject_dialog_title))
            .setNeutralButton(resources.getString(R.string.cancel_option)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.delete_option)) { _, _ ->
                model.deleteSubject(subject)
            }
            .create()

        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    companion object {
        const val TAG = "DeleteSubjectDialog"
    }
}