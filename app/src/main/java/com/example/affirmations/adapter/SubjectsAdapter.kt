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

class SubjectsAdapter (
    private val context: Context,
    private val onItemLongClick: (View) -> Unit
) : ListAdapter<Subject, SubjectsAdapter.SubjectsViewHolder>(SubjectsDiffCallback) {

    class SubjectsViewHolder
        (
        view: View,
        val context: Context,
        val onItemLongClick: (View) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private val subjectNameTv: TextView = itemView.findViewById(R.id.subject_text)
        private var currentSubject: Subject? = null

        init {
            itemView.setOnLongClickListener{
                onItemLongClick(it)
                return@setOnLongClickListener true
            }
        }


        fun bind(subject: Subject) {
            currentSubject = subject
            subjectNameTv.text = subject.name
        }


    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SubjectsViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.subject_list_item, viewGroup, false)

        return SubjectsViewHolder(view, context, onItemLongClick)
    }

    override fun onBindViewHolder(viewHolder: SubjectsViewHolder, position: Int) {
        val subject = getItem(position)
        viewHolder.bind(subject)
    }

    object SubjectsDiffCallback : DiffUtil.ItemCallback<Subject>() {
        override fun areItemsTheSame(oldItem: Subject, newItem: Subject): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Subject, newItem: Subject): Boolean {
            return oldItem.id == newItem.id
        }
    }
}