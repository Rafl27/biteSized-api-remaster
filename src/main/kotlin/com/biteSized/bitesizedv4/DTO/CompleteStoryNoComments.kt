package com.biteSized.bitesizedv4.DTO

import com.biteSized.bitesizedv4.model.User
import java.time.LocalDate
import java.util.*

data class CompleteStoryNoComments(
    val profile_picture : String,
    val username : String,
    val userId : Long,
    val art : String,
    val date : Date?,
    val downvotes : Int?,
    val upvotes : Int?,
    val title : String,
    val content : String,
    val storyId : Long,
    val language : String?,
    val followerCount: Long
)
