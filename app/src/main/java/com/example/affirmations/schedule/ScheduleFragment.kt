package com.example.affirmations.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder

private const val TAG = "ScheduleFragment"
const val ARG_OBJECT = "object"
class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private var _addDialogBinding: ScheduleDialogItemBinding? = null
    private val addDialogBinding get() = _addDialogBinding!!

    private lateinit var dayOfWeek: String


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

        //taking arguments from pager adapter
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            //NULL POINTER?
            dayOfWeek = getString(ARG_OBJECT)!!
        }

        //Recycler view and data
        val scheduleAdapter = ScheduleAdapter(
            context = requireContext(),
        ) { itemView -> adapterOnLongClick(itemView) }

        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.adapter = scheduleAdapter

        scheduleViewModel.scheduleLiveData.observe(requireActivity()) {
            it?.let { scheuleList ->
                scheduleAdapter.submitList(
                    scheuleList.filter { item -> item.dayOfWeek == dayOfWeek }
                            as MutableList<ScheduleItem>
                )
            }
        }

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
                //TODO:save new item
            }
            .show()
    }

    private fun showEditScheduleItemDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.edit_schedule_item_dialog_title))
            .setView(R.layout.schedule_dialog_item)
            .setNeutralButton(resources.getString(R.string.cancel_option)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.save_option)) { _, _ ->
                //TODO:save new item
            }
            .show()
    }

    private fun showDeleteScheduleItemDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete_schedule_item_dialog_title))
            .setNeutralButton(resources.getString(R.string.cancel_option)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.delete_option)) { _, _ ->
                //TODO:save new item
            }
            .show()
    }

    private fun disableItem() {
        //TODO: set item to disable state
    }

    private fun adapterOnLongClick(view: View) {
        showContextMenu(view, R.menu.list_item_menu)
    }

    private fun showContextMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(context, v)
        popup.menuInflater.inflate(menuRes, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.edit_option -> {
                    showEditScheduleItemDialog()
                    true
                }
                R.id.delete_option -> {
                    showDeleteScheduleItemDialog()
                    true
                }
                R.id.disable_option -> {
                    disableItem()
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


