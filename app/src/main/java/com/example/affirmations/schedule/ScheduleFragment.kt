package com.example.affirmations.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.affirmations.R
import com.example.affirmations.adapter.ScheduleAdapter
import com.example.affirmations.model.ScheduleItem
import com.example.affirmations.databinding.FragmentScheduleBinding

private const val TAG = "ScheduleFragment"
const val ARG_OBJECT = "object"
class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!
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
            context = requireContext()
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


