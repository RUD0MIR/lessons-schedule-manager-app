package com.example.affirmations.subjects

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.MenuRes
import androidx.recyclerview.widget.RecyclerView
import com.example.affirmations.R
import com.example.affirmations.adapter.SubjectsAdapter
import com.example.affirmations.databinding.ActivitySubjectsBinding
import com.example.affirmations.databinding.SubjectsDialogItemBinding
import com.example.affirmations.model.ScheduleItem
import com.example.affirmations.model.Subject
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout

class SubjectsActivity : AppCompatActivity() {

    private val context = this@SubjectsActivity
    private lateinit var binding: ActivitySubjectsBinding
    private lateinit var dialogItemBinding: SubjectsDialogItemBinding
    private lateinit var subjectsAdapter: SubjectsAdapter

    private val subjectsViewModel by viewModels<SubjectsViewModel> {
        SubjectViewModelFactory (context)
    }

    private lateinit var inputLayout: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubjectsBinding.inflate(layoutInflater)
        dialogItemBinding = SubjectsDialogItemBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Recycler view and data
        subjectsAdapter = SubjectsAdapter(
            context = context
        ) { itemView, subject, position -> adapterOnLongClick(itemView, subject, position) }

        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.adapter = subjectsAdapter

        subjectsViewModel.subjectsLiveData.observe(context) {
            it?.let { subjectList ->
                subjectsAdapter.submitList(
                    subjectList as MutableList<Subject>
                )
            }
        }

        //listener for app bar menu
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        binding.addSubjectFab.setOnClickListener {
            showAddSubjectDialog()
        }
    }

    //TODO:not for db
    private fun showEditSubjectDialog(subject: Subject, position: Int) {
        MaterialAlertDialogBuilder(context)
            .setTitle(resources.getString(R.string.edit_subject_dialog_title))
            .setView(R.layout.subjects_dialog_item)
            .setNeutralButton(resources.getString(R.string.cancel_option)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.save_option)) { _, _ ->
                //TODO:save new item
            }
            .show()
    }

    //TODO:not for db
    private fun showDeleteSubjectDialog(subject: Subject, position: Int) {
        MaterialAlertDialogBuilder(context)
            .setTitle(resources.getString(R.string.delete_subject_dialog_title))
            .setNeutralButton(resources.getString(R.string.cancel_option)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.delete_option)) { _, _ ->
                subjectsViewModel.dataSource.removeSubject(subject)
                subjectsAdapter.notifyItemChanged(position)
            }
            .show()
    }

    //TODO:not for db
    private fun showAddSubjectDialog() {
        MaterialAlertDialogBuilder(context)
            .setTitle(resources.getString(R.string.add_subject_title))
            .setView(R.layout.subjects_dialog_item)
                .setNeutralButton(resources.getString(R.string.cancel_option)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.save_option)) { _, _ ->
                val inputText = dialogItemBinding.addSubjectTextField.editText?.text.toString()
                subjectsViewModel.insertSubject(inputText)
            }
            .show()
    }

    private fun adapterOnLongClick(view: View, subject: Subject, position: Int) {
        showContextMenu(view, R.menu.edit_delete_menu, subject, position)
    }

    private fun showContextMenu(
        v: View,
        @MenuRes menuRes: Int,
        subject: Subject,
        position: Int
    ) {
        val popup = PopupMenu(context, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.edit_option -> {
                    showEditSubjectDialog(subject, position)
                    true
                }
                R.id.delete_option -> {
                    showDeleteSubjectDialog(subject, position)
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