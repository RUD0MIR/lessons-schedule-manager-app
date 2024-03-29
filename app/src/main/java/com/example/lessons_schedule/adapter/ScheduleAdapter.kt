package com.example.lessons_schedule.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lessons_schedule.R
import com.example.lessons_schedule.data.model.ScheduleItem
import com.example.lessons_schedule.schedule.ScheduleActivity


class ScheduleAdapter(
    private val context: Context,
    private val onItemLongClick: (View, ScheduleItem) -> Unit,
) : ListAdapter<ScheduleItem, ScheduleAdapter.ScheduleViewHolder>(ScheduleDiffCallback) {

    class ScheduleViewHolder
        (
        view: View,
        val context: Context,
        val onItemLongClick: (View, ScheduleItem) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private val subjectTv: TextView = itemView.findViewById(R.id.timetb_subject_text)
        private val timeTv: TextView = itemView.findViewById(R.id.timetb_time)
        private val numberTv: TextView = itemView.findViewById(R.id.timetb_number)
        private val ivIcon: ImageView = itemView.findViewById(R.id.ivWatchIcon)
        private val tvClassroom: TextView = itemView.findViewById(R.id.tvClassroom)
        private var currentScheduleItem: ScheduleItem? = null

        init {
            itemView.setOnLongClickListener{ itemView ->
                currentScheduleItem?.let { scheduleItem ->
                    onItemLongClick(itemView, scheduleItem)
                }
                return@setOnLongClickListener true
            }
        }

        fun bind(scheduleItem: ScheduleItem) {
            currentScheduleItem = scheduleItem
            subjectTv.text = scheduleItem.subjectName
            timeTv.text = scheduleItem.lessonTime
            numberTv.text = scheduleItem.number.toString()
            tvClassroom.text = context.getString(R.string.classroom_number_template, scheduleItem.classroom)

            if(scheduleItem.weekState != ScheduleActivity.NEUTRAL_WEEK) {
                ivIcon.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(getItem(position).weekState != ScheduleActivity.NEUTRAL_WEEK) return 1
        return 0
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ScheduleViewHolder {
//        val view  = when(viewType) {
//            1 -> {
//                LayoutInflater.from(viewGroup.context)
//                    .inflate(R.layout.disbled_schedule_list_item, viewGroup, false)
//            }
//            else -> LayoutInflater.from(viewGroup.context)
//                .inflate(R.layout.schedule_list_item, viewGroup, false)
//        }

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.schedule_list_item, viewGroup, false)
        return ScheduleViewHolder(view, context, onItemLongClick)
    }

    override fun onBindViewHolder(viewHolder: ScheduleViewHolder, position: Int) {
        val scheduleItem = getItem(position)
        viewHolder.bind(scheduleItem)
    }

    object ScheduleDiffCallback : DiffUtil.ItemCallback<ScheduleItem>() {
        override fun areItemsTheSame(oldItem: ScheduleItem, newItem: ScheduleItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ScheduleItem, newItem: ScheduleItem): Boolean {
            return oldItem.id == newItem.id
        }
    }
}

