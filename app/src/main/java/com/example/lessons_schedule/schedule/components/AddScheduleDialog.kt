package com.example.lessons_schedule.schedule.components

import android.app.ActionBar.LayoutParams
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.*
import com.example.lessons_schedule.R
import com.example.lessons_schedule.databinding.ScheduleDialogItemBinding
import com.example.lessons_schedule.schedule.ScheduleActivity
import com.example.lessons_schedule.schedule.ScheduleViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.schedule_dialog_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddScheduleDialog(
    private val dayOfWeek: String
): DialogFragment() {
    private lateinit var binding: ScheduleDialogItemBinding
    private val model: ScheduleViewModel by activityViewModels()
    private var weekStates = emptyArray<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireActivity())

        binding = ScheduleDialogItemBinding.inflate(layoutInflater)
        weekStates = resources.getStringArray(R.array.week_states)

        builder.setView(binding.root)

        val dialog = builder.setTitle(R.string.add_schedule_item_dialog_title)
            .setView(binding.root)
            .setNeutralButton(resources.getString(R.string.cancel_option), null)
            .setPositiveButton(R.string.add_option, null)
            .create()

        //explicitly sizing the dialog box to prevent it from shrinking when the
        //keyboard appears
        //dialog.window?.setLayout(LayoutParams.MATCH_PARENT, 1755)

        dialog.setOnShowListener{
            val btnPositive: Button =  dialog.getButton(AlertDialog.BUTTON_POSITIVE)

            //setting up default value for weekStateExposedMenu
            binding.weekTypeExposedMenu.setText(weekStates[ScheduleActivity.NEUTRAL_WEEK])

            //setting up week states items for exposed menu
            binding.weekTypeExposedMenu.setSimpleItems(weekStates)


            btnPositive.setOnClickListener {
                val subjectText = binding.subjectsExposedMenu.text.toString()
                val lessonTimeText = binding.lessonsNumbersExposedMenu.text.toString()
                val weekStateText = binding.weekTypeExposedMenu.text.toString()
                val classroomText = binding.tfClassroom.text.toString()

                if(subjectText.isNotBlank() && lessonTimeText.isNotBlank() && classroomText.isNotBlank()) {
                    val lessonNumber =  model.getLessonNumberFromText(lessonTimeText)

                    model.viewModelScope.launch( Dispatchers.Main) {
                        model.insertDataToDatabase(
                            subjectText,
                            model.getLessonTimeByLessonNumber(lessonNumber),
                            lessonNumber,
                            dayOfWeek,
                            getWeekStateFromText(weekStateText),
                            classroomText
                        )
                    }
                    dialog.dismiss()
                } else {
                    if(subjectText.isEmpty()) {
                        binding.subjectsInputLayout.error = resources.getString(R.string.empty_input_error_message)
                    }

                    if(lessonTimeText.isEmpty()) {
                        binding.lessonsNumbersInputLayout.error = resources.getString(R.string.empty_input_error_message)
                    }

                    if(classroomText.isEmpty()) {
                        binding.classroomInputLayout.error = resources.getString(R.string.empty_input_error_message)
                    }
                }
            }

        }

        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //setting up subject names exposed menu data for exposed menu
            model.readSubjectsName.observe(viewLifecycleOwner, Observer { subjectNames ->
                binding.subjectsExposedMenu.setSimpleItems(subjectNames.toTypedArray())
                Log.d(TAG, subjectNames.size.toString())
            })


        //setting up lessons numbers exposed menu data for exposed menu
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
    private fun getWeekStateFromText(weekState: String): Int {
        val weekStates = activity?.resources?.getStringArray(R.array.week_states)
        return when(weekState) {
            weekStates?.get(ScheduleActivity.UPPER_WEEK) -> ScheduleActivity.UPPER_WEEK
            weekStates?.get(ScheduleActivity.LOWER_WEEK) -> ScheduleActivity.LOWER_WEEK
            else -> ScheduleActivity.NEUTRAL_WEEK
        }
    }

    companion object {
        const val TAG = "AddScheduleItemDialog"
    }
}