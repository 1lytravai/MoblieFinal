package com.example.mobliefinal


data class Topic(
    val createdAt: Long = 0,
    val description: String = "",
    val name: String = "",
    val public: Boolean = false,
    val user: String = "",
    var favorite: Boolean = false
) {
    // Thêm constructor không đối số
    constructor() : this(0, "", "", false, "", false)
}

