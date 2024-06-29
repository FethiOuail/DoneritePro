package com.examples.donerite


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.examples.donerite.ui.TaskDetailActivity

class TaskAdapter(private var tasks: List<Task>, private val context: Context) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    private val taskRepository = TaskRepository(context)

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.taskTitle)
        val dueDate: TextView = view.findViewById(R.id.taskDueDate)
        val completed: CheckBox = view.findViewById(R.id.taskCompleted)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.title.text = task.title
        holder.dueDate.text = task.dueDate
        holder.completed.isChecked = task.completed

        holder.completed.setOnCheckedChangeListener { _, isChecked ->
            // Code to update task completion status
            task.completed = isChecked
            taskRepository.updateTask(task)
        }

        holder.itemView.setOnClickListener {
            // Code to open TaskDetailActivity
            val intent = Intent(context, TaskDetailActivity::class.java).apply {
                putExtra("TASK_ID", task.id)
            }
            context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int = tasks.size

    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }
}
