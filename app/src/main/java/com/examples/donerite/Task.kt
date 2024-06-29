package com.examples.donerite

data class Task(
    val id: Long = 0,
    val title: String,
    val description: String,
    val dueDate: String?,
    val priority: Int,
    var completed: Boolean = false
)


