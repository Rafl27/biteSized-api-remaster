package com.biteSized.bitesizedv4.DTO

import java.util.Date

data class SingleStory(
    val id: Long,
    val title: String,
    val content: String,
    val date: Date?,
    val downvotes: Int?,
    val upvotes: Int?,
    val art: String
)
