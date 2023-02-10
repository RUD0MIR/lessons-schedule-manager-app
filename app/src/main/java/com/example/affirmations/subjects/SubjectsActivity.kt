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
import com.example.affirmations.model.Subject

class SubjectsActivity : AppCompatActivity() {

    private val context = this@SubjectsActivity
    private lateinit var binding: ActivitySubjectsBinding

    private val subjectsViewModel by viewModels<SubjectsViewModel> {
        SubjectViewModelFactory (context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubjectsBinding.inflate(layoutInflater)
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