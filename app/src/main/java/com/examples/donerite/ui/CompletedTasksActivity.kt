package com.examples.donerite.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.examples.donerite.R
import com.examples.donerite.Task
import com.examples.donerite.TaskAdapter
import com.examples.donerite.TaskRepository
import com.examples.donerite.databinding.ActivityCompletedTasksBinding

class CompletedTasksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCompletedTasksBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: TextView
    private lateinit var adapter: TaskAdapter // Replace with your adapter type

    private lateinit var taskRepository: TaskRepository
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompletedTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = findViewById(R.id.completedTasksRecyclerView)
        emptyView = findViewById(R.id.emptyView)



        taskRepository = TaskRepository(this)
        taskAdapter = TaskAdapter(taskRepository.getCompletedTasks(), this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = taskAdapter



        // Initialize RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TaskAdapter(fetchCompletedTasksFromDatabase(), this) // Replace with your adapter type

        // Populate completed tasks
        val completedTasks = fetchCompletedTasksFromDatabase()
        if (completedTasks.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyView.visibility = View.GONE
            recyclerView.adapter = adapter
        }

    }


    private fun fetchCompletedTasksFromDatabase(): List<Task> {
        // Replace with your actual implementation to fetch completed tasks from your database
        // Example: Using TaskRepository to fetch completed tasks
        val taskRepository = TaskRepository(this)
        return taskRepository.getCompletedTasks()
    }
}