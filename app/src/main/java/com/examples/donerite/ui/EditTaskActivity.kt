package com.examples.donerite.ui

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.examples.donerite.R
import com.examples.donerite.Task
import com.examples.donerite.TaskRepository
import com.examples.donerite.databinding.ActivityEditTaskBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditTaskBinding

    private lateinit var taskRepository: TaskRepository
    private lateinit var task: Task

    private lateinit var dueDateInput: TextInputEditText
    private val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskRepository = TaskRepository(this)
        val taskId = intent.getLongExtra("TASK_ID", 0)
        task = taskRepository.getTaskById(taskId) ?: return

        val titleInput = findViewById<TextInputEditText>(R.id.taskTitleInput)
        val descriptionInput = findViewById<TextInputEditText>(R.id.taskDescriptionInput)
        dueDateInput = findViewById(R.id.taskDueDateInput)
        val priorityInput = findViewById<TextInputEditText>(R.id.taskPriorityInput)

        titleInput.setText(task.title)
        descriptionInput.setText(task.description)
        dueDateInput.setText(task.dueDate)
        priorityInput.setText(task.priority.toString())



        dueDateInput.setOnClickListener {
            showDatePicker()
        }

        val saveButton = findViewById<MaterialButton>(R.id.saveButton)
        saveButton.setOnClickListener {
            val updatedTask = task.copy(
                title = titleInput.text.toString(),
                description = descriptionInput.text.toString(),
                dueDate = dateFormat.format(calendar.time),
                priority = priorityInput.text.toString().toInt()
            )
            taskRepository.updateTask(updatedTask)
            finish()
        }
    }

    private fun showDatePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDueDateInput()
        }

        DatePickerDialog(
            this,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateDueDateInput() {
        dueDateInput.setText(dateFormat.format(calendar.time))
    }
}