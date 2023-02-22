package com.example.affirmations.subjects

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.MenuRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.affirmations.R
import com.example.affirmations.adapter.SubjectsAdapter
import com.example.affirmations.databinding.ActivitySubjectsBinding
import com.example.affirmations.databinding.SubjectsDialogItemBinding
import com.example.affirmations.model.ScheduleItem
import com.example.affirmations.model.Subject
import com.example.affirmations.schedule.ScheduleViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.subject_list_item.view.*
import kotlinx.android.synthetic.main.subjects_dialog_item.view.*

class SubjectsActivity : AppCompatActivity() {

    private val context = this@SubjectsActivity
    private lateinit var binding: ActivitySubjectsBinding
    private lateinit var dialogItemBinding: SubjectsDialogItemBinding
    private lateinit var subjectsAdapter: SubjectsAdapter
    private lateinit var subjectsViewModel: SubjectsViewModel

    private lateinit var inputLayout: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubjectsBinding.inflate(layoutInflater)
        dialogItemBinding = SubjectsDialogItemBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        //receive data from fragments
        supportFragmentManager
            .setFragmentResultListener("requestKey", this) { requestKey, bundle ->
                // We use a String here, but any type that can be put in a Bundle is supported
                val result = bundle.getString("bundleKey")
                // Do something with the result
            }

        //Recycler view and data
        subjectsAdapter = SubjectsAdapter(
            context = context
        ) { itemView, subject, position -> adapterOnLongClick(itemView, subject, position) }

        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.adapter = subjectsAdapter

        subjectsViewModel = ViewModelProvider(context)[SubjectsViewModel::class.java]
        subjectsViewModel.readSubjectsData.observe(context, Observer { subjects ->
            subjectsAdapter.submitList(subjects)
        })

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
                subjectsViewModel.deleteSubject(subject)
                subjectsAdapter.notifyItemChanged(position)
            }
            .show()
    }

    //TODO:not for db
    private fun showAddSubjectDialog() {

        val builder = MaterialAlertDialogBuilder(context)
        val view = View.inflate(builder.context, R.layout.subjects_dialog_item, null)

        builder.setTitle(resources.getString(R.string.add_subject_title))
            .setView(view)
            .setNeutralButton(resources.getString(R.string.cancel_option)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.save_option)) { _, _ ->
                val inputText = view.addSubjectTextField.editText?.text.toString()
                insertDataToDatabase(inputText, view)
                subjectsAdapter.notifyDataSetChanged()//TODO: correct notify
            }
            .show()
    }

    private fun insertDataToDatabase(inputText: String, view: View) {

        if(inputText.isNotBlank()){
            val subject = Subject(
                0,
                inputText
            )
            subjectsViewModel.addSubject(subject)
        }else{
//            Toast.makeText(context, inputText, Toast.LENGTH_LONG).show()//"Please fill out the field."
            view.addSubjectTextField.error = getString(R.string.empty_input_error_message)
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
}