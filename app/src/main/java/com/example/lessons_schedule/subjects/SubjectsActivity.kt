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
import com.example.lessons_schedule.subjects.components.AddSubjectDialog
import com.example.lessons_schedule.subjects.components.DeleteSubjectDialog
import com.example.lessons_schedule.subjects.components.EditSubjectDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.subjects_dialog_item.view.*

class SubjectsActivity : AppCompatActivity() {

    private val context = this@SubjectsActivity
    private lateinit var binding: ActivitySubjectsBinding
    private lateinit var subjectsAdapter: SubjectsAdapter
    private lateinit var subjectsViewModel: SubjectsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubjectsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Recycler view
        subjectsAdapter = SubjectsAdapter(
            context = context
        ) { itemView, subject -> adapterOnLongClick(itemView, subject) }

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
            val addDialog = AddSubjectDialog()
            addDialog.show(supportFragmentManager, AddSubjectDialog.TAG)
        }
    }

    private fun adapterOnLongClick(view: View, subject: Subject) {
        showContextMenu(view, R.menu.edit_delete_menu, subject)
    }

    private fun showContextMenu(
        v: View,
        @MenuRes menuRes: Int,
        subject: Subject
    ) {
        val popup = PopupMenu(context, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.edit_option -> {
                    val editDialog = EditSubjectDialog(subject)
                    editDialog.show(supportFragmentManager, EditSubjectDialog.TAG)

                    true
                }
                R.id.delete_option -> {
                    val deleteDialog = DeleteSubjectDialog(subject)
                    deleteDialog.show(supportFragmentManager, DeleteSubjectDialog.TAG)

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