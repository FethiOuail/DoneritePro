package com.examples.donerite.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.examples.donerite.R
import com.examples.donerite.TaskAdapter
import com.examples.donerite.TaskRepository
import com.examples.donerite.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var taskRepository: TaskRepository
    private lateinit var taskAdapter: TaskAdapter

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        taskRepository = TaskRepository(this)
        taskAdapter = TaskAdapter(taskRepository.getAllTasks(), this)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = taskAdapter

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            // Code to start AddTaskActivity

           startActivity(Intent(this, AddTaskActivity::class.java))
        }

        binding.showCompletedTasksButton.setOnClickListener {
            startActivity(Intent(this, CompletedTasksActivity::class.java))

        }


    }

    override fun onResume() {
        super.onResume()
        taskAdapter.updateTasks(taskRepository.getAllTasks())
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_by_due_date -> {
                sortTasksByDueDate()
                true
            }

            R.id.action_sort_by_priority -> {
                sortTasksByPriority()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun sortTasksByDueDate() {
        val taskRepository = TaskRepository(this)
        val tasks = taskRepository.getTasksSortedByDueDate()
        taskAdapter.updateTasks(tasks)
        taskAdapter.notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun sortTasksByPriority() {
        val taskRepository = TaskRepository(this)
        val tasks = taskRepository.getTasksSortedByPriority()
        taskAdapter.updateTasks(tasks)
        taskAdapter.notifyDataSetChanged()

    }

}