package com.biteSized.bitesizedv4.DTO

import java.sql.Timestamp
import java.time.LocalDate
import javax.management.monitor.StringMonitor

data class StoryCommentsResponse(
    val idStory: Long,
    val idComment: Long,
    val contentComment: String,
    val artComment: String?,
    val parentCommentId: Long?,
    val dateComment: Timestamp,
    val downvotesComment: Int,
    val upvotesComment: Int,
    val useridComment: Long,
    val userProfilePic: String,
    val userUsername: String,
    val userEmail: String
)
