package com.example.lessons_schedule.subjects.components

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.lessons_schedule.R
import com.example.lessons_schedule.data.model.Subject
import com.example.lessons_schedule.databinding.ScheduleDialogItemBinding
import com.example.lessons_schedule.databinding.SubjectsDialogItemBinding
import com.example.lessons_schedule.subjects.SubjectsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.subjects_dialog_item.view.*

class AddSubjectDialog: DialogFragment() {
    lateinit var binding: SubjectsDialogItemBinding
    private val model: SubjectsViewModel by activityViewModels()

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        return binding.root
//    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = SubjectsDialogItemBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(requireContext())

        val dialog = builder.setTitle(resources.getString(R.string.add_subject_title))
            .setView(binding.root)
            .setNeutralButton(resources.getString(R.string.cancel_option), null)
            .setPositiveButton(resources.getString(R.string.add_option), null)
            .create()

        dialog.setOnShowListener{
            val button: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)

            button.setOnClickListener {
                val inputText = binding.subjectExposedMenu.text.toString()

                    if(inputText.isNotBlank()){
                        val subject = Subject(
                            0,
                            inputText
                        )
                        model.addSubject(subject)
                    dialog.dismiss()
                } else {
                    binding.subjectInputLayout.error = getString(R.string.empty_input_error_message)
                }
            }
        }

        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    companion object {
        const val TAG = "AddSubjectDialog"
    }
}