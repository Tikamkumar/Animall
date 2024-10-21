package com.online.animall.data.model

data class PostModel(
    val id: String,
    val userId: String,
    val text: String,
    val content: String,
    val likes: List<String>,
    val comments: List<CommentModel>,
    val createdAt: String
)
