package com.example.lessons_schedule.schedule

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.lessons_schedule.R
import com.example.lessons_schedule.adapter.ScheduleAdapter
import com.example.lessons_schedule.data.model.ScheduleItem
import com.example.lessons_schedule.data.model.TimeTableItem
import com.example.lessons_schedule.databinding.FragmentScheduleBinding
import com.example.lessons_schedule.schedule.components.AddScheduleDialog
import com.example.lessons_schedule.schedule.components.DeleteScheduleDialog
import com.example.lessons_schedule.schedule.components.EditScheduleDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import kotlinx.android.synthetic.main.schedule_dialog_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private lateinit var dayOfWeek: String

    private lateinit var scheduleAdapter: ScheduleAdapter
    private val model: ScheduleViewModel by activityViewModels()

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

        //getting dayOfWeek from viewPagerAdapter
        arguments?.takeIf { it.containsKey(ARG_DAY_OF_WEEK) }?.apply {
            dayOfWeek = getString(ARG_DAY_OF_WEEK)!!
        }



        //Recycler view
        scheduleAdapter = ScheduleAdapter(
            context = requireContext(),
        ) { itemView, scheduleItem -> adapterOnLongClick(itemView, scheduleItem) }

        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.adapter = scheduleAdapter

        //getting schedule data from database
        model.readScheduleData.observe(viewLifecycleOwner, Observer { scheduleItems ->
            //filtering list by day of week and weekState
            model.weekState.observe(viewLifecycleOwner) { weekState ->
                val filteredList =  scheduleItems.filter { scheduleItem ->
                    (scheduleItem.dayOfWeek == dayOfWeek)
                            &&
                            (scheduleItem.weekState == weekState
                                    ||
                                    scheduleItem.weekState == ScheduleActivity.NEUTRAL_WEEK)
                }
                Log.d(TAG, "weekState in ScheduleFragment: $weekState")
                scheduleAdapter.submitList(filteredList)
            }
        })

        //fab click listener
        binding.addScheduleItemFab.setOnClickListener {
            val addDialog = AddScheduleDialog(dayOfWeek)
            addDialog.show(childFragmentManager, AddScheduleDialog.TAG)
        }
    }

    private fun adapterOnLongClick(view: View, scheduleItem: ScheduleItem) {
        showContextMenu(view, R.menu.edit_delete_menu, scheduleItem)
    }

    private fun showContextMenu(
        v: View,
        @MenuRes menuRes: Int,
        scheduleItem: ScheduleItem) {
        val popup = PopupMenu(context, v)

        popup.menuInflater.inflate(menuRes, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.edit_option -> {
                    val editDialog = EditScheduleDialog(scheduleItem)
                    editDialog.show(childFragmentManager, EditScheduleDialog.TAG)

                    true
                }
                R.id.delete_option -> {
                    val deleteDialog = DeleteScheduleDialog(scheduleItem)
                    deleteDialog.show(childFragmentManager, DeleteScheduleDialog.TAG)
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

    companion object {
        const val TAG = "ScheduleFragment"
        const val ARG_DAY_OF_WEEK = "dayOfWeek"
        const val ARG_WEEK_STATE = "weekState"
    }
}


