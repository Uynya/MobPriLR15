package com.example.myapplication2.data.mapper

import com.example.myapplication2.domain.model.Task

object TaskMapper {
    fun toDomain(entity: com.example.myapplication2.data.repository.TaskEntity): Task {
        return Task(
            id = entity.id,
            title = entity.title,
            description = entity.description
        )
    }

    fun toEntity(domain: Task): com.example.myapplication2.data.repository.TaskEntity {
        return com.example.myapplication2.data.repository.TaskEntity(
            id = domain.id,
            title = domain.title,
            description = domain.description
        )
    }
}