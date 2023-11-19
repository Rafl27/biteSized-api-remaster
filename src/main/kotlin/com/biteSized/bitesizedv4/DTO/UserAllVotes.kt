package com.biteSized.bitesizedv4.DTO

import java.util.*

data class UserAllVotes(
        val CommentId : Long,
        val art : String?,
        val content : String,
        val date : Date?,
        val downvotes : Int,
        val upvotes : Int,
        val story_id : Long,
        val vote_type : String
)
