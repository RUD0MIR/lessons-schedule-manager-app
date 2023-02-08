package com.example.affirmations.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.affirmations.R
import com.example.affirmations.data.TimeTableItem


class TimeTableAdapter(
    private val context: Context,
    private val onItemLongClick: (View) -> Unit
) : ListAdapter<TimeTableItem, TimeTableAdapter.TimeTableViewHolder>(TimeTableDiffCallback) {

    class TimeTableViewHolder
        (
        view: View,
        val context: Context,
        val onItemLongClick: (View) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private val timeTableSubject: TextView = itemView.findViewById(R.id.timetb_subject_text)
        private val timeTableTime: TextView = itemView.findViewById(R.id.timetb_time)
        private val timeTableNumber: TextView = itemView.findViewById(R.id.timetb_number)
        private var currentTimeTableItem: TimeTableItem? = null

        init {
            itemView.setOnLongClickListener{
                onItemLongClick(it)
                return@setOnLongClickListener true
            }
        }


        fun bind(timeTableItem: TimeTableItem) {
            currentTimeTableItem = timeTableItem
            timeTableSubject.text = timeTableItem.subject
            timeTableTime.text = timeTableItem.time
            timeTableNumber.text = timeTableItem.number.toString()
        }


    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TimeTableViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.time_table_list_item, viewGroup, false)

        return TimeTableViewHolder(view, context, onItemLongClick)
    }

    override fun onBindViewHolder(viewHolder: TimeTableViewHolder, position: Int) {
        val timeTableItem = getItem(position)
        viewHolder.bind(timeTableItem)


    }

    object TimeTableDiffCallback : DiffUtil.ItemCallback<TimeTableItem>() {
        override fun areItemsTheSame(oldItem: TimeTableItem, newItem: TimeTableItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TimeTableItem, newItem: TimeTableItem): Boolean {
            return oldItem.id == newItem.id
        }
    }
}

