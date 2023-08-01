package com.biteSized.bitesizedv4.service

import com.biteSized.bitesizedv4.DTO.DownvoteResponse

interface CommentService {
    fun commentDownvote(commentId: Long): DownvoteResponse
}