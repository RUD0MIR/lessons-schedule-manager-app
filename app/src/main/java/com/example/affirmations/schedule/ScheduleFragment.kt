package com.example.affirmations.schedule

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.MenuRes
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.affirmations.R
import com.example.affirmations.adapter.TimeTableAdapter
import com.example.affirmations.data.DaysOfWeek
import com.example.affirmations.data.TimeTableItem
import com.example.affirmations.data.daysOfWeek
import com.example.affirmations.databinding.FragmentScheduleBinding
import com.google.android.material.tabs.TabLayout

private const val TAG = "ScheduleFragmentdebug"
const val ARG_OBJECT = "object"
class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!
    private lateinit var dayOfWeek: String

    private val timeTableListViewModel by viewModels<TimeTableListViewModel> {
        TimeTableListViewModelFactory (requireContext())
    }

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

        //taking arguments from pager adapter
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            //NULL POINTER?
            dayOfWeek = getString(ARG_OBJECT)!!
        }

        //Recycler view and data
        val timeTableAdapter = TimeTableAdapter(
            context = requireContext()
        ) { itemView -> adapterOnLongClick(itemView) }

        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.adapter = timeTableAdapter

        timeTableListViewModel.timeTableLiveData.observe(requireActivity()) {
            it?.let { timeTableList ->
                timeTableAdapter.submitList(
                    timeTableList.filter { item -> item.dayOfWeek == dayOfWeek }
                            as MutableList<TimeTableItem>
                )
            }
        }

        binding.addItemFab.setOnClickListener {
            //TODO: show add item dialog
        }
    }

    private fun adapterOnLongClick(view: View) {
        showContextMenu(view, R.menu.list_item_menu)
    }

    private fun showContextMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(context, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener {
            TODO("menuItem: MenuItem ->")
        }

        popup.show()
    }

}


