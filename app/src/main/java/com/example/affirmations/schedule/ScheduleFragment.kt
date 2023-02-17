package com.example.affirmations.schedule

import android.icu.text.Transliterator.Position
import android.os.Bundle
import android.view.ContextMenu
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.affirmations.R
import com.example.affirmations.adapter.ScheduleAdapter
import com.example.affirmations.databinding.ScheduleDialogItemBinding
import com.example.affirmations.model.ScheduleItem
import com.example.affirmations.databinding.FragmentScheduleBinding
import com.example.affirmations.model.TimeTableItem
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

    private val scheduleViewModel by viewModels<ScheduleViewModel> {
        ScheduleViewModelFactory (requireContext())
    }

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

        //Recycler view and data
        scheduleAdapter = ScheduleAdapter(
            context = requireContext(),
        ) { itemView, scheduleItem, position -> adapterOnLongClick(itemView, scheduleItem, position) }

        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.adapter = scheduleAdapter

        scheduleViewModel.scheduleLiveData.observe(requireActivity()) {
            it?.let { scheduleList ->
                scheduleAdapter.submitList(
                    scheduleList.filter { item -> item.dayOfWeek == dayOfWeek }
                            as MutableList<ScheduleItem>
                )
            }
        }

        binding.addScheduleItemFab.setOnClickListener {
            showAddScheduleItemDialog()
        }
    }


    //TODO:not for db
    private fun showAddScheduleItemDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.add_schedule_item_dialog_title))
            .setView(R.layout.schedule_dialog_item)
            .setNeutralButton(resources.getString(R.string.cancel_option)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.add_option)) { _, _ ->
                //TODO:save new item
                val subject = addDialogBinding.subjectExposedMenu.text.toString()
                val lessonTime = addDialogBinding.lessonTimeExposedMenu.text.toString()
                scheduleViewModel.insertScheduleItem(
                    subject,
                    lessonTime,
                    6,
                    dayOfWeek,
                )

                scheduleAdapter.notifyDataSetChanged()
            }
            .show()
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
                scheduleViewModel.dataSource.removeScheduleItem(scheduleItem)
                scheduleAdapter.notifyItemChanged(position)
            }
            .show()
    }

    private fun adapterOnLongClick(view: View, scheduleItem: ScheduleItem, position: Int) {
        showContextMenu(view, R.menu.list_item_menu, scheduleItem, position)
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


