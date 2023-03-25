package com.example.lessons_schedule.subjects

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.lessons_schedule.R
import com.example.lessons_schedule.adapter.SubjectsAdapter
import com.example.lessons_schedule.databinding.ActivitySubjectsBinding
import com.example.lessons_schedule.databinding.SubjectsDialogItemBinding
import com.example.lessons_schedule.data.model.Subject
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.subjects_dialog_item.view.*

class SubjectsActivity : AppCompatActivity() {

    private val context = this@SubjectsActivity
    private lateinit var binding: ActivitySubjectsBinding
    private lateinit var dialogItemBinding: SubjectsDialogItemBinding
    private lateinit var subjectsAdapter: SubjectsAdapter
    private lateinit var subjectsViewModel: SubjectsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubjectsBinding.inflate(layoutInflater)
        dialogItemBinding = SubjectsDialogItemBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Recycler view
        subjectsAdapter = SubjectsAdapter(
            context = context
        ) { itemView, subject, position -> adapterOnLongClick(itemView, subject, position) }

        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.adapter = subjectsAdapter

        //getting data from database and passing it into recycler view
        subjectsViewModel = ViewModelProvider(context)[SubjectsViewModel::class.java]
        subjectsViewModel.readSubjectsData.observe(context, Observer { subjects ->
            subjectsAdapter.submitList(subjects)
        })

        //listener for app bar menu
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        //fab listener
        binding.addSubjectFab.setOnClickListener {
            showAddSubjectDialog()
        }
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

    private fun showEditSubjectDialog(subject: Subject, position: Int) {
        val builder = MaterialAlertDialogBuilder(context)
        val view = View.inflate(builder.context, R.layout.subjects_dialog_item, null)
        val dialog = builder.setTitle(resources.getString(R.string.edit_subject_dialog_title))
            .setView(view)
            .setNeutralButton(resources.getString(R.string.cancel_option), null)
            .setPositiveButton(resources.getString(R.string.save_option), null)
            .create()

        view.addSubjectTextField.editText?.setText(subject.name)

        dialog.setOnShowListener{
            val button: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)

            button.setOnClickListener {
                val inputText = view.addSubjectTextField.editText?.text.toString()

                if(inputText.isNotBlank()) {
                    updateDataInDatabase(inputText, view, subject.id)
                    subjectsAdapter.notifyItemChanged(position)
                    dialog.dismiss()
                } else {
                    view.addSubjectTextField.error = getString(R.string.empty_input_error_message)
                }
            }
        }
        dialog.show()
    }

    private fun updateDataInDatabase(inputText: String, view: View, subjectId: Int) {

        if(inputText.isNotBlank()){
            val newSubject = Subject(subjectId, inputText)
            subjectsViewModel.updateSubject(newSubject)
        }else{
            view.addSubjectTextField.error = getString(R.string.empty_input_error_message)
        }
    }

    private fun showDeleteSubjectDialog(subject: Subject, position: Int) {
        MaterialAlertDialogBuilder(context)
            .setTitle(resources.getString(R.string.delete_subject_dialog_title))
            .setNeutralButton(resources.getString(R.string.cancel_option)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.delete_option)) { _, _ ->
                subjectsViewModel.deleteSubject(subject)
                subjectsAdapter.notifyItemChanged(position)
            }
            .show()
    }

    private fun showAddSubjectDialog() {
        val builder = MaterialAlertDialogBuilder(context)
        val view = View.inflate(builder.context, R.layout.subjects_dialog_item, null)
        val dialog = builder.setTitle(resources.getString(R.string.add_subject_title))
            .setView(view)
            .setNeutralButton(resources.getString(R.string.cancel_option), null)
            .setPositiveButton(resources.getString(R.string.add_option), null)
            .create()

        dialog.setOnShowListener{
            val button: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)

            button.setOnClickListener {
                val inputText = view.addSubjectTextField.editText?.text.toString()

                if(inputText.isNotBlank()) {
                    insertDataToDatabase(inputText, view)
                    dialog.dismiss()
                } else {
                    view.addSubjectTextField.error = getString(R.string.empty_input_error_message)
                }
            }
        }
        dialog.show()
    }

    private fun insertDataToDatabase(inputText: String, view: View) {

        if(inputText.isNotBlank()){
            val subject = Subject(
                0,
                inputText
            )
            subjectsViewModel.addSubject(subject)
        }else{
            view.addSubjectTextField.error = getString(R.string.empty_input_error_message)
        }
    }
}