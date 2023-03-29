package com.example.lessons_schedule.schedule.components

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.lessons_schedule.R
import com.example.lessons_schedule.data.model.ScheduleItem
import com.example.lessons_schedule.databinding.ScheduleDialogItemBinding
import com.example.lessons_schedule.schedule.ScheduleViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditScheduleDialog(
    private val scheduleItem: ScheduleItem
): DialogFragment() {
    lateinit var binding: ScheduleDialogItemBinding
    private val model: ScheduleViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireContext())
        binding = ScheduleDialogItemBinding.inflate(layoutInflater)

        builder.setView(binding.root)

        val dialog = builder.setTitle(R.string.edit_schedule_item_dialog_title)
            .setView(binding.root)
            .setNeutralButton(resources.getString(R.string.cancel_option), null)
            .setPositiveButton(R.string.save_option, null)
            .create()

        //setting up data from the chosen element
        binding.subjectsExposedMenu.setText(scheduleItem.subjectName)

        binding.lessonsNumbersExposedMenu.setText("")
        binding.lessonsNumbersExposedMenu.setText(
            getString(R.string.lessons_numbers_sample, scheduleItem.number)
        )

        dialog.setOnShowListener{
            val btnPositive: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)

            btnPositive.setOnClickListener {
                val subjectText = binding.subjectsExposedMenu.text.toString()
                val lessonTimeText = binding.lessonsNumbersExposedMenu.text.toString()

                if(subjectText.isNotBlank() && lessonTimeText.isNotBlank()) {
                    val lessonNumber =  model.getLessonNumberFromText(lessonTimeText)

                    model.viewModelScope.launch(Dispatchers.Main) {
                        model.updateDataInDatabase(
                            subjectText,
                            model.getLessonTimeByLessonNumber(lessonNumber),
                            lessonNumber,
                            scheduleItem
                        )
                    }
                    dialog.dismiss()
                } else {
                    if(subjectText.isBlank()) {
                        binding.subjectsInputLayout.error =
                            resources.getString(R.string.empty_input_error_message)
                    }

                    if(lessonTimeText.isBlank()) {
                        binding.lessonsNumbersInputLayout.error =
                            resources.getString(R.string.empty_input_error_message)
                    }
                }
            }
        }

        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }
    //TODO: add default value to weekState in dialog
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //setting up subject names exposed menu data
        model.readSubjectsName.observe(viewLifecycleOwner, Observer { subjectNames ->
            binding.subjectsExposedMenu.setSimpleItems(subjectNames.toTypedArray())
            Log.d(TAG, subjectNames.size.toString())
        })


        //setting up lessons numbers exposed menu data
        model.readTimeTableData.observe(viewLifecycleOwner, Observer { timeTableItems ->
            val lessonsNumbersArray = Array(timeTableItems.size) {""}

            for(i in timeTableItems.indices) {
                lessonsNumbersArray[i] =
                    getString(
                        R.string.lessons_numbers_sample,
                        timeTableItems[i].lessonNumber
                    )
            }

            binding.lessonsNumbersExposedMenu.setSimpleItems(lessonsNumbersArray)
        })
    }

    companion object {
        const val TAG = "EditScheduleDialog"
    }
}