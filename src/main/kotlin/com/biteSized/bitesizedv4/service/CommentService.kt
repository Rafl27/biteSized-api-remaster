package com.biteSized.bitesizedv4.service

import com.biteSized.bitesizedv4.DTO.*
import org.springframework.http.ResponseEntity

interface CommentService {
    //TODO to up vote and down vote you need authorization - doing this right now!!
    fun commentDownvote(commentId: Long, userId: Long, token: String): DownvoteResponse
    fun commentUpvote(commentId: Long, userId: Long, token: String): UpvoteResponse
    fun createComment(storyId: Long, commentRequest: CommentRequest, authorization: String) : ResponseEntity<CommentResponse>
    fun storyComments(storyId : Long) : ResponseEntity<List<StoryCommentsResponse>>
    fun createReply(parentCommentId : Long, replyRequest: CommentReplyRequest, authorization : String) : ResponseEntity<CommentResponse>
    fun singleComment(commentId: Long) : ResponseEntity<List<StoryCommentsResponse>>
    fun getThreadSegmentation(commentId: Long) : ResponseEntity<List<CommentThreadsUser>>
    fun getThreadSegmentationCount(commentId: Long) : ResponseEntity<Int>
}