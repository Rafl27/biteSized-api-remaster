package com.biteSized.bitesizedv4.DTO

import java.sql.Timestamp

data class CommentThreadsUser(
    val userId : Long,
    val email : String,
    val profilePicture : String,
    val username : String,
    val commentId : Long,
    val art : String?,
    val content : String,
    val date : Timestamp,
    val downvotes : Int,
    val upvotes : Int,
    val parentId : Long?,
    val storyId : Long
    )
