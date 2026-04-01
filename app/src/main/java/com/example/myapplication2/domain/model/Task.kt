package com.example.myapplication2.domain.model

data class Task(
    val id: Long,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)