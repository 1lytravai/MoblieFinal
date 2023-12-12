package com.example.mobliefinal

data class Topic(
    val topicId: String? = null,
    val createdAt: Long = 0,
    val description: String? = null,
    val name: String? = null,
    val public: Boolean = false,
    val user: String? = null,
    var favorite: Boolean = false
)

