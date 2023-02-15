package com.example.affirmations.subjects

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.annotation.MenuRes
import androidx.recyclerview.widget.RecyclerView
import com.example.affirmations.R
import com.example.affirmations.adapter.SubjectsAdapter
import com.example.affirmations.databinding.ActivitySubjectsBinding
import com.example.affirmations.databinding.AddSubjectDialogItemBinding
import com.example.affirmations.model.Subject
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout

class SubjectsActivity : AppCompatActivity() {

    private val context = this@SubjectsActivity
    private lateinit var binding: ActivitySubjectsBinding
    private lateinit var dialogItemBinding: AddSubjectDialogItemBinding

    private val subjectsViewModel by viewModels<SubjectsViewModel> {
        SubjectViewModelFactory (context)
    }

    private lateinit var inputLayout: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubjectsBinding.inflate(layoutInflater)
        dialogItemBinding = AddSubjectDialogItemBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Recycler view and data
        val subjectsAdapter = SubjectsAdapter(
            context = context
        ) { itemView -> adapterOnLongClick(itemView) }

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

    private fun showAddSubjectDialog() {
        MaterialAlertDialogBuilder(context)
            .setTitle(resources.getString(R.string.add_subject_title))
            .setView(R.layout.add_subject_dialog_item)
                .setNeutralButton(resources.getString(R.string.cancel_option)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.save_option)) { _, _ ->
                val inputText = dialogItemBinding.addSubjectTextField.editText?.text.toString()
                subjectsViewModel.insertSubject(inputText)
            }
            .show()
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

    private fun switchActivities(destination: Class<*>) {
        val switchActivityIntent = Intent(this, destination)
        startActivity(switchActivityIntent)
    }
}