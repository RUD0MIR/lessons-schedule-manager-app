package com.example.affirmations.schedule

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.affirmations.R
import com.example.affirmations.adapter.ScheduleAdapter
import com.example.affirmations.databinding.ScheduleDialogItemBinding
import com.example.affirmations.model.ScheduleItem
import com.example.affirmations.databinding.FragmentScheduleBinding
import com.example.affirmations.model.Subject
import com.google.android.material.dialog.MaterialAlertDialogBuilder

private const val TAG = "ScheduleFragment"
const val ARG_OBJECT = "object"
class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private var _addDialogBinding: ScheduleDialogItemBinding? = null
    private val addDialogBinding get() = _addDialogBinding!!

    private lateinit var dayOfWeek: String
    private lateinit var scheduleAdapter: ScheduleAdapter
    private lateinit var scheduleViewModel: ScheduleViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        _addDialogBinding = ScheduleDialogItemBinding.inflate(inflater, container, false)
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

        //initializing view model and getting schedule data
        scheduleViewModel = ViewModelProvider(requireActivity())[ScheduleViewModel::class.java]
        scheduleViewModel.readScheduleData.observe(viewLifecycleOwner, Observer { scheduleItems ->
            scheduleAdapter.submitList(scheduleItems)
        })

        scheduleViewModel.readSubjectsName.observe(viewLifecycleOwner, Observer { subjectNames ->
            val adapter = ArrayAdapter(requireContext(), R.layout.schedule_list_item, subjectNames)
            (addDialogBinding.addScheduleItemTf.editText?.text as? AutoCompleteTextView)?.setAdapter(adapter)
        })

        //fab click listener
        binding.addScheduleItemFab.setOnClickListener {
            showAddScheduleItemDialog()
        }
    }

    private fun showAddScheduleItemDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.add_schedule_item_dialog_title))
            .setView(R.layout.schedule_dialog_item)
            .setNeutralButton(resources.getString(R.string.cancel_option)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.add_option)) { _, _ ->

                insertDataToDatabase()
                scheduleAdapter.notifyDataSetChanged()//TODO: use correct notifySetChanged
            }
            .show()
    }

    private fun insertDataToDatabase() {
        val subject = addDialogBinding.subjectExposedMenu.text.toString()
        val lessonTime = addDialogBinding.lessonTimeExposedMenu.text.toString()

        if(inputCheck(subject, lessonTime)){
            val scheduleItem = ScheduleItem(
                0,
                subject,
                lessonTime,
                1,//TODO: display correct number
                dayOfWeek
            )
            // Add Data to Database
            scheduleViewModel.addScheduleItem(scheduleItem)
        }else{
            Toast.makeText(requireContext(), subject + lessonTime, Toast.LENGTH_LONG).show()//Please fill out all fields.
        }
    }



    //TODO:not for db
    private fun showEditScheduleItemDialog(scheduleItem: ScheduleItem, position: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.edit_schedule_item_dialog_title))
            .setView(R.layout.schedule_dialog_item)
            .setNeutralButton(resources.getString(R.string.cancel_option)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.save_option)) { _, _ ->
                //TODO:save new item
            }
            .show()
    }

    //TODO:not for db
    private fun showDeleteScheduleItemDialog(scheduleItem: ScheduleItem, position: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete_lesson_dialog_title))
            .setNeutralButton(resources.getString(R.string.cancel_option)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.delete_option)) { _, _ ->
                scheduleViewModel.deleteScheduleItem(scheduleItem)
                scheduleAdapter.notifyItemRemoved(position)
            }
            .show()
    }

    private fun adapterOnLongClick(view: View, scheduleItem: ScheduleItem, position: Int) {
        showContextMenu(view, R.menu.list_item_menu, scheduleItem, position)
    }

    private fun inputCheck(subject: String, lessonTime: String): Boolean{
        return !(TextUtils.isEmpty(subject) && TextUtils.isEmpty(lessonTime))
    }
    
    //TODO:not for db
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


