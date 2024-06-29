package com.examples.donerite.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.examples.donerite.R
import com.examples.donerite.Task
import com.examples.donerite.TaskRepository
import com.examples.donerite.databinding.ActivityTaskDetailBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView

class TaskDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskDetailBinding

    private lateinit var taskRepository: TaskRepository
    private lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        taskRepository = TaskRepository(this)
        val taskId = intent.getLongExtra("TASK_ID", 0)
        task = taskRepository.getTaskById(taskId)!!

        val titleView = findViewById<MaterialTextView>(R.id.taskTitle)
        val descriptionView = findViewById<MaterialTextView>(R.id.taskDescription)
        val dueDateView = findViewById<MaterialTextView>(R.id.taskDueDate)
        val priorityView = findViewById<MaterialTextView>(R.id.taskPriority)

        titleView.text = task.title
        descriptionView.text = task.description
        dueDateView.text = task.dueDate
        priorityView.text = task.priority.toString()

        val editButton = findViewById<MaterialButton>(R.id.editButton)
        val deleteButton = findViewById<MaterialButton>(R.id.deleteButton)

        editButton.setOnClickListener {
            val intent = Intent(this, EditTaskActivity::class.java).apply {
                putExtra("TASK_ID", task.id)
            }
            startActivity(intent)        }

        deleteButton.setOnClickListener {
            taskRepository.deleteTask(task.id)
            finish()
        }
    }
}