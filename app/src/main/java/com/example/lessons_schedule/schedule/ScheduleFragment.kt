package com.example.lessons_schedule.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.lessons_schedule.R
import com.example.lessons_schedule.adapter.ScheduleAdapter
import com.example.lessons_schedule.data.model.ScheduleItem
import com.example.lessons_schedule.data.model.TimeTableItem
import com.example.lessons_schedule.databinding.FragmentScheduleBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import kotlinx.android.synthetic.main.schedule_dialog_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "ScheduleFragment"
const val ARG_OBJECT = "object"
class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private var timeTableData: ArrayList<TimeTableItem> = ArrayList()
    private var subjectsNamesData: ArrayList<String> =  ArrayList()

    private lateinit var dayOfWeek: String
    private lateinit var scheduleAdapter: ScheduleAdapter
    private lateinit var model: ScheduleViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            dayOfWeek = getString(ARG_OBJECT)!!
        }

        //Recycler view
        scheduleAdapter = ScheduleAdapter(
            context = requireContext(),
        ) { itemView, scheduleItem, position -> adapterOnLongClick(itemView, scheduleItem, position) }

        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.adapter = scheduleAdapter

        //initializing view model and getting schedule data from database
        model = ViewModelProvider(requireActivity())[ScheduleViewModel::class.java]
        model.readScheduleData.observe(viewLifecycleOwner, Observer { scheduleItems ->
            //filtering list by day of week
            val filteredList =  scheduleItems.filter { scheduleItem ->
                scheduleItem.dayOfWeek == dayOfWeek
            }
            scheduleAdapter.submitList(filteredList)
        })

        //read schedule names from database
        model.readSubjectsName.observe(viewLifecycleOwner, Observer { subjectNames ->
            subjectsNamesData = ArrayList(subjectNames)
        })

        //read lesson time data from database
        model.readTimeTableData.observe(viewLifecycleOwner, Observer { lessonsTime ->
            for(i in lessonsTime.indices) {
                timeTableData = ArrayList(lessonsTime)
            }
        })

        //fab click listener
        binding.addScheduleItemFab.setOnClickListener {
            showAddScheduleItemDialog()
        }
    }

    private fun showAddScheduleItemDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val view = View.inflate(builder.context, R.layout.schedule_dialog_item, null)

        setUpSubjectsNamesExposedMenuData(view)
        setUpLessonsTimeExposedMenuData(view)

        val dialog = builder.setTitle(R.string.add_schedule_item_dialog_title)
            .setView(view)
            .setNeutralButton(resources.getString(R.string.cancel_option), null)
            .setPositiveButton(R.string.add_option, null)
            .create()

        dialog.setOnShowListener{
            val btnPositive: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)

            btnPositive.setOnClickListener {
                val subjectText = view.subjects_input_layout.editText?.text.toString()
                val lessonTimeText = view.lesson_time_input_layout.editText?.text.toString()

                if(subjectText.isNotBlank() && lessonTimeText.isNotBlank()) {
                    val lessonNumber =  model.getLessonNumberFromText(lessonTimeText)

                    model.viewModelScope.launch( Dispatchers.Main) {
                        model.insertDataToDatabase(
                            subjectText,
                            model.getLessonTimeByLessonNumber(lessonNumber),
                            lessonNumber,
                            dayOfWeek
                        )
                    }

                    dialog.dismiss()
                } else {
                    if(subjectText.isEmpty()) {
                        view.subjects_input_layout.error = resources.getString(R.string.empty_input_error_message)
                    }

                    if(lessonTimeText.isEmpty()) {
                        view.lesson_time_input_layout.error = resources.getString(R.string.empty_input_error_message)
                    }

                }
            }
        }
        dialog.show()
    }



    private fun showEditScheduleItemDialog(scheduleItem: ScheduleItem, position: Int) {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val view = View.inflate(builder.context, R.layout.schedule_dialog_item, null)

        //setting up data from the chosen element
        view.subjects_input_layout.editText?.setText(scheduleItem.subjectName)

        view.lesson_time_input_layout.editText?.setText("")
        view.lesson_time_input_layout.editText?.setText(
            getString(R.string.lessons_numbers_sample, scheduleItem.number))

        setUpSubjectsNamesExposedMenuData(view)
        setUpLessonsTimeExposedMenuData(view)

        val dialog = builder.setTitle(R.string.edit_schedule_item_dialog_title)
            .setView(view)
            .setNeutralButton(resources.getString(R.string.cancel_option), null)
            .setPositiveButton(R.string.save_option, null)
            .setCancelable(false)
            .create()
        //TODO: prevent a dialog from dismissing when the user clicks outside of it

        dialog.setOnShowListener{
            val btnPositive: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)

            btnPositive.setOnClickListener {
                val subjectText = view.subjects_input_layout.editText?.text.toString()
                val lessonTimeText = view.lesson_time_input_layout.editText?.text.toString()

                val subjectsExposedMenu =
                    (view.subjects_input_layout.editText as? MaterialAutoCompleteTextView)

                if(subjectText.isNotBlank() && lessonTimeText.isNotBlank()) {
                    if(subjectsNamesData.contains(subjectText)) {
                        view.subjects_input_layout.error = resources.getString(R.string.item_not_from_menu_error_message)
                    } else {
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
                    }
                } else {
                    if(subjectText.isBlank()) {
                        view.subjects_input_layout.error = resources.getString(R.string.empty_input_error_message)
                    }

                    if(lessonTimeText.isBlank()) {
                        view.lesson_time_input_layout.error = resources.getString(R.string.empty_input_error_message)
                    }
                }
            }
        }
        dialog.show()
    }

    fun isSubjectsExposedMenuContainsSubject(
        subjectName: String,
        subjectsExposedMenu:  MaterialAutoCompleteTextView
    ): Boolean {
        return subjectsNamesData.contains(subjectName)
    }

    private fun setUpSubjectsNamesExposedMenuData(view: View) {
        val subjectsExposedMenu = (view.subjects_input_layout.editText as? MaterialAutoCompleteTextView)
        subjectsExposedMenu?.setSimpleItems(subjectsNamesData.toTypedArray())
    }

    private fun setUpLessonsTimeExposedMenuData(view: View) {
        val lessonTimeExposedMenu =
            (view.lesson_time_input_layout.editText as? MaterialAutoCompleteTextView)
        val lessonsTimeArray = Array(timeTableData.size) {""}

        for(i in timeTableData.indices) {
            lessonsTimeArray[i] =
                getString(
                    R.string.lessons_numbers_sample,
                    timeTableData[i].lessonNumber
                )//i + 1 for lesson time
        }

        lessonTimeExposedMenu?.setSimpleItems(lessonsTimeArray)
    }

    private fun showDeleteScheduleItemDialog(scheduleItem: ScheduleItem, position: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete_lesson_dialog_title))
            .setNeutralButton(resources.getString(R.string.cancel_option)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.delete_option)) { _, _ ->
                model.deleteScheduleItem(scheduleItem)
            }
            .show()
    }

    private fun adapterOnLongClick(view: View, scheduleItem: ScheduleItem, position: Int) {
        showContextMenu(view, R.menu.list_item_menu, scheduleItem, position)
    }

    private fun showContextMenu(
        v: View,
        @MenuRes menuRes: Int,
        scheduleItem: ScheduleItem,
        position: Int) {
        val popup = PopupMenu(context, v)

        popup.menuInflater.inflate(menuRes, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.edit_option -> {
                    showEditScheduleItemDialog(scheduleItem, position)
                    true
                }
                R.id.delete_option -> {
                    showDeleteScheduleItemDialog(scheduleItem, position)
                    true
                }
                R.id.disable_option -> {
                    model.changeDisabledState(scheduleItem)
                    true
                }
                else -> {
                    Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
                    true
                }
            }
        }

        popup.show()
    }
}


