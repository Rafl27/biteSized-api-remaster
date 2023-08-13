package com.biteSized.bitesizedv4.DTO

data class UserStories(
    val id: Long,
    val title: String,
    val content: String,
    val upvotes: Int?,
    val downvotes: Int?,
    val art: String
)
