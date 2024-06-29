package com.examples.donerite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns

class TaskRepository(context: Context) {
    private val dbHelper = TaskDatabaseHelper(context)

    fun getAllTasks(): List<Task> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            TaskDatabaseHelper.TABLE_NAME, null, null, null, null, null, "${TaskDatabaseHelper.COLUMN_DUE_DATE} ASC"
        )
        return cursorToList(cursor)
    }

    fun addTask(task: Task): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(TaskDatabaseHelper.COLUMN_TITLE, task.title)
            put(TaskDatabaseHelper.COLUMN_DESCRIPTION, task.description)
            put(TaskDatabaseHelper.COLUMN_DUE_DATE, task.dueDate)
            put(TaskDatabaseHelper.COLUMN_PRIORITY, task.priority)
            put(TaskDatabaseHelper.COLUMN_COMPLETED, if (task.completed) 1 else 0)
        }
        return db.insert(TaskDatabaseHelper.TABLE_NAME, null, values)
    }

    fun updateTask(task: Task): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(TaskDatabaseHelper.COLUMN_TITLE, task.title)
            put(TaskDatabaseHelper.COLUMN_DESCRIPTION, task.description)
            put(TaskDatabaseHelper.COLUMN_DUE_DATE, task.dueDate)
            put(TaskDatabaseHelper.COLUMN_PRIORITY, task.priority)
            put(TaskDatabaseHelper.COLUMN_COMPLETED, if (task.completed) 1 else 0)
        }
        return db.update(TaskDatabaseHelper.TABLE_NAME, values, "${TaskDatabaseHelper.COLUMN_ID} = ?", arrayOf(task.id.toString()))
    }

    fun deleteTask(id: Long): Int {
        val db = dbHelper.writableDatabase
        return db.delete(TaskDatabaseHelper.TABLE_NAME, "${TaskDatabaseHelper.COLUMN_ID} = ?", arrayOf(id.toString()))
    }

    private fun cursorToList(cursor: Cursor): List<Task> {
        val tasks = mutableListOf<Task>()
        with(cursor) {
            while (moveToNext()) {
                val task = Task(
                    id = getLong(getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_ID)),
                    title = getString(getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_TITLE)),
                    description = getString(getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_DESCRIPTION)),
                    dueDate = getString(getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_DUE_DATE)),
                    priority = getInt(getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_PRIORITY)),
                    completed = getInt(getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_COMPLETED)) == 1
                )
                tasks.add(task)
            }
        }
        cursor.close()
        return tasks
    }

    fun getTaskById(id: Long): Task? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            TaskDatabaseHelper.TABLE_NAME,
            null,
            "${TaskDatabaseHelper.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val task = Task(
                id = cursor.getLong(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_ID)),
                title = cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_TITLE)),
                description = cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_DESCRIPTION)),
                dueDate = cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_DUE_DATE)),
                priority = cursor.getInt(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_PRIORITY)),
                completed = cursor.getInt(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_COMPLETED)) == 1
            )
            cursor.close()
            task
        } else {
            cursor.close()
            null
        }
    }
    fun getCompletedTasks(): List<Task> {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            BaseColumns._ID,
            TaskContract.TaskEntry.COLUMN_NAME_TITLE,
            TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION,
            TaskContract.TaskEntry.COLUMN_NAME_DUE_DATE,
            TaskContract.TaskEntry.COLUMN_NAME_PRIORITY,
            TaskContract.TaskEntry.COLUMN_NAME_COMPLETED
        )

        val selection = "${TaskContract.TaskEntry.COLUMN_NAME_COMPLETED} = ?"
        val selectionArgs = arrayOf("1")

        val cursor = db.query(
            TaskContract.TaskEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            "${TaskContract.TaskEntry.COLUMN_NAME_DUE_DATE} ASC"
        )

        val tasks = mutableListOf<Task>()
        with(cursor) {
            while (moveToNext()) {
                val taskId = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                val title = getString(getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_TITLE))
                val description = getString(getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION))
                val dueDate = getString(getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_DUE_DATE))
                val priority = getInt(getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_PRIORITY))
                val completed = getInt(getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_COMPLETED)) == 1

                tasks.add(Task(taskId, title, description, dueDate.toString(), priority, completed))
            }
        }
        cursor.close()
        return tasks
    }


    fun getTasksSortedByDueDate(): List<Task> {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            BaseColumns._ID,
            TaskContract.TaskEntry.COLUMN_NAME_TITLE,
            TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION,
            TaskContract.TaskEntry.COLUMN_NAME_DUE_DATE,
            TaskContract.TaskEntry.COLUMN_NAME_PRIORITY,
            TaskContract.TaskEntry.COLUMN_NAME_COMPLETED
        )

        val sortOrder = "${TaskContract.TaskEntry.COLUMN_NAME_DUE_DATE} ASC"

        val cursor = db.query(
            TaskContract.TaskEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            sortOrder
        )

        return extractTasksFromCursor(cursor)
    }

    fun getTasksSortedByPriority(): List<Task> {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            BaseColumns._ID,
            TaskContract.TaskEntry.COLUMN_NAME_TITLE,
            TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION,
            TaskContract.TaskEntry.COLUMN_NAME_DUE_DATE,
            TaskContract.TaskEntry.COLUMN_NAME_PRIORITY,
            TaskContract.TaskEntry.COLUMN_NAME_COMPLETED
        )

        val sortOrder = "${TaskContract.TaskEntry.COLUMN_NAME_PRIORITY} ASC"

        val cursor = db.query(
            TaskContract.TaskEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            sortOrder
        )

        return extractTasksFromCursor(cursor)
    }

    private fun extractTasksFromCursor(cursor: Cursor): List<Task> {
        val tasks = mutableListOf<Task>()
        with(cursor) {
            while (moveToNext()) {
                val taskId = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                val title = getString(getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_TITLE))
                val description = getString(getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION))
                val dueDate = getString(getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_DUE_DATE))
                val priority = getInt(getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_PRIORITY))
                val completed = getInt(getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_COMPLETED)) == 1

                tasks.add(Task(taskId, title, description, dueDate.toString(), priority, completed))
            }
        }
        cursor.close()
        return tasks
    }

}

object TaskContract {

    object TaskEntry : BaseColumns {
        const val TABLE_NAME = "tasks"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_DESCRIPTION = "description"
        const val COLUMN_NAME_DUE_DATE = "due_date"
        const val COLUMN_NAME_PRIORITY = "priority"
        const val COLUMN_NAME_COMPLETED = "completed"
    }
}
