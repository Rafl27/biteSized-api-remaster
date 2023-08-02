package com.biteSized.bitesizedv4.service

import com.biteSized.bitesizedv4.DTO.DownvoteResponse
import com.biteSized.bitesizedv4.DTO.UpvoteResponse

interface CommentService {
    fun commentDownvote(commentId: Long): DownvoteResponse
    fun commentUpvote(commentId: Long): UpvoteResponse
}