package com.example.affirmations.schedule

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
import androidx.recyclerview.widget.RecyclerView
import com.example.affirmations.R
import com.example.affirmations.adapter.ScheduleAdapter
import com.example.affirmations.data.model.ScheduleItem
import com.example.affirmations.databinding.FragmentScheduleBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import kotlinx.android.synthetic.main.schedule_dialog_item.view.*

private const val TAG = "ScheduleFragment"
const val ARG_OBJECT = "object"
class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private lateinit var dayOfWeek: String
    private lateinit var scheduleAdapter: ScheduleAdapter
    private lateinit var scheduleViewModel: ScheduleViewModel

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
        scheduleViewModel = ViewModelProvider(requireActivity())[ScheduleViewModel::class.java]
        scheduleViewModel.readScheduleData.observe(viewLifecycleOwner, Observer { scheduleItems ->
            //filtering list by day of week
            val filteredList =  scheduleItems.filter { scheduleItem ->
                scheduleItem.dayOfWeek == dayOfWeek
            }
            scheduleAdapter.submitList(filteredList)
        })





        //fab click listener
        binding.addScheduleItemFab.setOnClickListener {
            showAddScheduleItemDialog()
        }
    }

    private fun showAddScheduleItemDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val view = View.inflate(builder.context, R.layout.schedule_dialog_item, null)

        //setting data to exposed menu
        scheduleViewModel.readSubjectsName.observe(viewLifecycleOwner, Observer { subjectNames ->
            val subjectsExposedMenu = (view.subjects_input_layout.editText as? MaterialAutoCompleteTextView)
            subjectsExposedMenu?.setSimpleItems(subjectNames.toTypedArray())
        })

        //setting data to exposed menu
        scheduleViewModel.readLessonsTime.observe(viewLifecycleOwner, Observer { lessonsTime ->
            val subjectsExposedMenu =
                (view.lesson_time_input_layout.editText as? MaterialAutoCompleteTextView)
            val lessonsTimeArray = Array(lessonsTime.size) {""}

            for(i in lessonsTime.indices) {
                lessonsTimeArray[i] =
                    getString(R.string.lesson_time_sample, i + 1, lessonsTime[i])
            }

            subjectsExposedMenu?.setSimpleItems(lessonsTimeArray)
        })

        val dialog = builder.setTitle(R.string.add_schedule_item_dialog_title)
            .setView(view)
            .setNeutralButton(resources.getString(R.string.cancel_option), null)
            .setPositiveButton(R.string.add_option, null)
            .create()

        dialog.setOnShowListener{
            val button: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)

            button.setOnClickListener {
                val subject = view.subjects_input_layout.editText?.text.toString()
                val lessonTime = view.lesson_time_input_layout.editText?.text.toString().substring(7)

                if(subject.isNotBlank() && lessonTime.isNotBlank()) {
                    insertDataToDatabase(subject, lessonTime)
                    dialog.dismiss()
                } else {
                    if(subject.isBlank()) {
                        view.subjects_input_layout.error = resources.getString(R.string.empty_input_error_message)
                    }

                    if(lessonTime.isBlank()) {
                        view.lesson_time_input_layout.error = resources.getString(R.string.empty_input_error_message)
                    }

                }
            }
        }
        dialog.show()
    }

    private fun insertDataToDatabase(subject: String, lessonTime: String) {
            val scheduleItem = ScheduleItem(
                0,
                subject,
                lessonTime,
                0,//TODO: correct number
                dayOfWeek
            )
            scheduleViewModel.addScheduleItem(scheduleItem)
    }

    private fun showEditScheduleItemDialog(scheduleItem: ScheduleItem, position: Int) {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val view = View.inflate(builder.context, R.layout.schedule_dialog_item, null)
        val dialog = builder.setTitle(R.string.edit_schedule_item_dialog_title)
            .setView(view)
            .setNeutralButton(resources.getString(R.string.cancel_option), null)
            .setPositiveButton(R.string.save_option, null)
            .create()

        //TODO: like add dialog

        dialog.setOnShowListener{
            val button: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)

            button.setOnClickListener {
                val subject = view.subjects_input_layout.editText?.text.toString()
                val lessonTime = view.lesson_time_input_layout.editText?.text.toString()

                if(subject.isNotBlank() && lessonTime.isNotBlank()) {
                    updateDataInDatabase(subject, lessonTime, scheduleItem)
                    dialog.dismiss()
                } else {
                    if(subject.isBlank()) {
                        view.subjects_input_layout.error = resources.getString(R.string.empty_input_error_message)
                    }

                    if(lessonTime.isBlank()) {
                        view.lesson_time_input_layout.error = resources.getString(R.string.empty_input_error_message)
                    }

                }
            }
        }
        dialog.show()
    }

    private fun updateDataInDatabase(subject: String, lessonTime: String, scheduleItem: ScheduleItem) {
        val time = lessonTime.substring(2, lessonTime.lastIndex)
        val number = lessonTime.substring(0, 1)
        val newScheduleItem = ScheduleItem(
            scheduleItem.id,
            subject,
            time,
            number.toInt(),
            dayOfWeek
        )
        scheduleViewModel.updateScheduleItem(newScheduleItem)
    }

    private fun showDeleteScheduleItemDialog(scheduleItem: ScheduleItem, position: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete_lesson_dialog_title))
            .setNeutralButton(resources.getString(R.string.cancel_option)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.delete_option)) { _, _ ->
                scheduleViewModel.deleteScheduleItem(scheduleItem)
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
                    scheduleItem.isDisable = !scheduleItem.isDisable
                    scheduleAdapter.notifyItemChanged(position)
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


