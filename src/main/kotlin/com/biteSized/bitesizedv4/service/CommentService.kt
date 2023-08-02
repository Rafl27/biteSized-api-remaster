package com.biteSized.bitesizedv4.service

import com.biteSized.bitesizedv4.DTO.*
import org.springframework.http.ResponseEntity

interface CommentService {
    //TODO to up vote and down vote you need authorization
    fun commentDownvote(commentId: Long): DownvoteResponse
    fun commentUpvote(commentId: Long): UpvoteResponse
    fun createComment(storyId: Long, commentRequest: CommentRequest, authorization: String) : ResponseEntity<CommentResponse>
    fun storyComments(storyId : Long) : ResponseEntity<List<StoryCommentsResponse>>
    fun createReply(parentCommentId : Long, replyRequest: CommentReplyRequest, authorization : String) : ResponseEntity<CommentResponse>
}