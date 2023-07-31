package com.biteSized.bitesizedv4.controller

import com.biteSized.bitesizedv4.DTO.*
import com.biteSized.bitesizedv4.model.Comment
import com.biteSized.bitesizedv4.model.User
import com.biteSized.bitesizedv4.repository.CommentRepository
import com.biteSized.bitesizedv4.repository.StoryRepository
import com.biteSized.bitesizedv4.security.JwtUtil
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.sql.Timestamp
import java.time.LocalDate
import java.util.*

@RestController
@RequestMapping("/comment")
class CommentController(@Autowired private val storyRepository: StoryRepository, @Autowired private val commentRepository: CommentRepository, @Autowired private val jwtUtil: JwtUtil) {
    @PostMapping("/{parentCommentId}/replies")
    fun createReply(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable parentCommentId: Long,
        @RequestBody replyRequest: CommentReplyRequest
    ): ResponseEntity<CommentResponse> {
        val parentCommentOptional = commentRepository.findById(parentCommentId)
        val token = authorizationHeader.replace("Bearer ", "")
        val claims = jwtUtil.decodeToken(token)
        val userId = claims.get("id", Integer::class.java)
        val username = claims.get("username", String::class.java)
        val profilePicture = claims.get("profilePicture", String::class.java)
        if (parentCommentOptional.isPresent) {
            val parentComment = parentCommentOptional.get()
            val replyComment = Comment(
                id = 0,
                content = replyRequest.content,
                story = parentComment.story,
                user = User(userId.toLong(), username, profilePicture = profilePicture),
                parent = parentComment,
                art = replyRequest.art,
                date = Date(),
            )
            val savedReplyComment = commentRepository.save(replyComment)
            val response = CommentResponse(savedReplyComment.id, savedReplyComment.content, savedReplyComment.user.username)
            return ResponseEntity.ok(response)
        } else {
            return ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/{storyId}/allcomments")
    @ApiOperation(value = "Returns storyId and all its comments/replies")
    fun storyComments(@PathVariable storyId: Long): ResponseEntity<List<StoryCommentsResponse>> {
        val story = storyRepository.findById(storyId)
        if (story.isPresent) {
            val queryResult = storyRepository.getStoryComments(storyId)
            val response = queryResult.map { result ->
                StoryCommentsResponse(
                    result[0] as Long,
                    result[1] as Long,
                    result[2] as String,
                    result[3] as? String ?: "",
                    result[4] as Long?,
                    result[5] as Timestamp,
                    result[6] as Int,
                    result[7] as Int,
                    result[8] as Long,
                )
            }
            return ResponseEntity.ok(response)
        } else {
            return ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/{storyId}")
    fun createComment(@PathVariable storyId: Long,
                      @RequestBody commentRequest: CommentRequest,
                      @RequestHeader("Authorization") authorizationHeader: String): ResponseEntity<CommentResponse> {
        val storyOptional = storyRepository.findById(storyId)
        val token = authorizationHeader.replace("Bearer ", "")
        val claims = jwtUtil.decodeToken(token)
        val userId = claims.get("id", Integer::class.java)
        val username = claims.get("username", String::class.java)
        val profilePicture = claims.get("profilePicture", String::class.java)
        if (storyOptional.isPresent) {
            val story = storyOptional.get()
            val comment = Comment(
                id = 0,
                content = commentRequest.content,
                story = story,
                user = User(userId.toLong(), username, profilePicture = profilePicture),
                date = Date(),
                art = commentRequest.art,
                parent = null
            )
            story.comments.add(comment)
            val savedComment = storyRepository.save(story).comments.last()
            val response = CommentResponse(savedComment.id, savedComment.content, savedComment.user.username)
            return ResponseEntity.ok(response)
        } else {
            return ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/{commentId}/upvote")
    fun commentUpvote(@PathVariable commentId: Long,
                    @RequestHeader("Authorization") authorizationHeader: String) : ResponseEntity<UpvoteResponse>{
        val comment = commentRepository.findById(commentId)
        if (comment.isPresent) {
            val commentEntity = comment.get()
            //?: elvis operator, if upvotes is null it returns 0
            commentEntity.upvotes = (commentEntity.upvotes ?: 0) + 1
            commentRepository.save(commentEntity)
            val upvoteResponse = UpvoteResponse(id = commentId, upvote = commentEntity.upvotes)
            return ResponseEntity(upvoteResponse, HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
    @PostMapping("/{commentId}/downvote")
    @ApiOperation(value = "downvote stories")
    fun commentDownvote(@PathVariable commentId: Long,
                      @RequestHeader("Authorization") authorizationHeader: String) : ResponseEntity<DownvoteResponse>{
        val comment = commentRepository.findById(commentId)
        if (comment.isPresent) {
            val commentEntity = comment.get()
            commentEntity.downvotes = (commentEntity.downvotes ?: 0) + 1
            commentRepository.save(commentEntity)
            val downVoteResponse = DownvoteResponse(id = commentId, downvote = commentEntity.downvotes)
            return ResponseEntity(downVoteResponse, HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}