package com.example.affirmations.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.affirmations.R
import com.example.affirmations.data.DataSource
import com.example.affirmations.data.TimeTableItem
import com.example.affirmations.databinding.TimeTableListItemBinding

class TimeTableAdapter(private val onClick: (TimeTableItem) -> Unit) :
    ListAdapter<TimeTableItem, TimeTableAdapter.TimeTableViewHolder>(TimeTableDiffCallback) {

    class TimeTableViewHolder(view: View, val onClick: (TimeTableItem) -> Unit) :
        RecyclerView.ViewHolder(view) {
        private val timeTableSubject: TextView = itemView.findViewById(R.id.timetb_subject_text)
        private val timeTableTime: TextView = itemView.findViewById(R.id.timetb_time)
        private val timeTableNumber: TextView = itemView.findViewById(R.id.timetb_number)
        private var currentTimeTableItem: TimeTableItem? = null

        init {
            itemView.setOnClickListener {
                currentTimeTableItem?.let {
                    onClick(it)
                }
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
        return TimeTableViewHolder(view, onClick)
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
