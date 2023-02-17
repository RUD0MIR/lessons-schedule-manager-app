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
import com.example.affirmations.model.Subject
import com.example.affirmations.model.TimeTableItem

class TimeTableAdapter (
    private val context: Context,
    private val onItemLongClick: (View, TimeTableItem, Int) -> Unit
) : ListAdapter<TimeTableItem, TimeTableAdapter.TimeTableViewHolder>(TimeTableDiffCallback) {

    class TimeTableViewHolder
        (
        view: View,
        val context: Context,
        private val onItemLongClick: (View, TimeTableItem, Int) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private val lessonTimeTv: TextView = itemView.findViewById(R.id.lesson_time_tv)
        private val lessonTv: TextView = itemView.findViewById(R.id.lesson_tv)
        private var currentTimeTableItem: TimeTableItem? = null

        init {
            itemView.setOnLongClickListener{ listItem ->
                currentTimeTableItem?.let { timeTableItem ->
                    onItemLongClick(listItem, timeTableItem, absoluteAdapterPosition)
                }
                return@setOnLongClickListener true
            }
        }


        fun bind(timeTableItem: TimeTableItem) {
            lessonTimeTv.text = timeTableItem.lessonTime
            lessonTv.text = timeTableItem.lessonNumber.toString()
            currentTimeTableItem = timeTableItem
        }


    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TimeTableViewHolder {
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