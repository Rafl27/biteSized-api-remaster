package com.biteSized.bitesizedv4.controller

import com.biteSized.bitesizedv4.DTO.*
import com.biteSized.bitesizedv4.service.CommentService
import com.biteSized.bitesizedv4.service.TokenService
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/comment")
@CrossOrigin(origins = arrayOf("http://localhost:5173"))
class CommentController(private val commentService: CommentService, private val tokenService: TokenService) {
    @PostMapping("/{parentCommentId}/replies")
    fun createReply(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable parentCommentId: Long,
        @RequestBody replyRequest: CommentReplyRequest
    ): ResponseEntity<CommentResponse> {
        return commentService.createReply(parentCommentId, replyRequest, authorizationHeader)
    }

    @GetMapping("/{storyId}/allcomments")
    @ApiOperation(value = "Returns storyId and all its comments/replies")
    fun storyComments(@PathVariable storyId: Long): ResponseEntity<List<StoryCommentsResponse>> {
        return commentService.storyComments(storyId)
    }

    @PostMapping("/{storyId}")
    fun createComment(@PathVariable storyId: Long,
                      @RequestBody commentRequest: CommentRequest,
                      @RequestHeader("Authorization") authorizationHeader: String): ResponseEntity<CommentResponse> {
        return commentService.createComment(storyId, commentRequest, authorizationHeader)
    }

    @PutMapping("/{commentId}/upvote")
    fun commentUpvote(@PathVariable commentId: Long,
                    @RequestHeader("Authorization") authorizationHeader: String) : ResponseEntity<UpvoteResponse>{
        val userId = tokenService.getUserId(authorizationHeader).toLong()
        val upvoteResponse = commentService.commentUpvote(commentId, userId, authorizationHeader)
        return ResponseEntity(upvoteResponse, HttpStatus.OK)
    }
    @PutMapping("/{commentId}/downvote")
    @ApiOperation(value = "downvote stories")
    fun commentDownvote(@PathVariable commentId: Long,
                      @RequestHeader("Authorization") authorizationHeader: String) : ResponseEntity<DownvoteResponse>{
        val downVoteResponse = commentService.commentDownvote(commentId)
        return ResponseEntity(downVoteResponse, HttpStatus.OK)
    }

    @GetMapping("/{commentId}/single-thread")
    @ApiOperation("Information about a single comment")
    fun singleComment(@PathVariable commentId: Long): ResponseEntity<List<StoryCommentsResponse>> {
        return commentService.singleComment(commentId)
    }

    @GetMapping("/{commentId}/more-threads")
    @ApiOperation("Gets all the response threads for a certain thread")
    fun moreThreads(@PathVariable commentId: Long) : ResponseEntity<List<CommentThreadsUser>> {
        return commentService.getThreadSegmentation(commentId)
    }
}