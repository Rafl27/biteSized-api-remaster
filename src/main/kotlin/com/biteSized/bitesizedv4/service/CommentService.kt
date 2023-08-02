package com.biteSized.bitesizedv4.service

import com.biteSized.bitesizedv4.DTO.*
import org.springframework.http.ResponseEntity

interface CommentService {
    fun commentDownvote(commentId: Long): DownvoteResponse
    fun commentUpvote(commentId: Long): UpvoteResponse
    fun createComment(storyId: Long, commentRequest: CommentRequest, authorization: String) : ResponseEntity<CommentResponse>
    fun storyComments(storyId : Long) : ResponseEntity<List<StoryCommentsResponse>>
}