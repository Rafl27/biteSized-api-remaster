package com.biteSized.bitesizedv4.service

import com.biteSized.bitesizedv4.DTO.CommentRequest
import com.biteSized.bitesizedv4.DTO.CommentResponse
import com.biteSized.bitesizedv4.DTO.DownvoteResponse
import com.biteSized.bitesizedv4.DTO.UpvoteResponse
import com.biteSized.bitesizedv4.exception.NotFoundException
import com.biteSized.bitesizedv4.model.Comment
import com.biteSized.bitesizedv4.model.User
import com.biteSized.bitesizedv4.repository.CommentRepository
import com.biteSized.bitesizedv4.repository.StoryRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class CommentServiceImpl(
    private val commentRepository: CommentRepository,
    private val storyRepository: StoryRepository,
    private val tokenService: TokenService
) : CommentService {
    override fun commentDownvote(commentId: Long): DownvoteResponse {
        val comment = commentRepository.findById(commentId)
        if (comment.isPresent) {
            val commentEntity = comment.get()
            commentEntity.downvotes = (commentEntity.downvotes ?: 0) + 1
            commentRepository.save(commentEntity)
            return DownvoteResponse(id = commentId, downvote = commentEntity.downvotes)
        } else {
            throw NotFoundException("Comment with ID $commentId not found")
        }
    }

    override fun commentUpvote(commentId: Long): UpvoteResponse {
        val comment = commentRepository.findById(commentId)
        if (comment.isPresent) {
            val commentEntity = comment.get()
            commentEntity.upvotes = (commentEntity.upvotes ?: 0) + 1
            commentRepository.save(commentEntity)
            return UpvoteResponse(id = commentId, upvote = commentEntity.upvotes)
        } else {
            throw NotFoundException("Comment with ID $commentId not found")
        }
    }

    override fun createComment(storyId: Long, commentRequest: CommentRequest, authorization: String): ResponseEntity<CommentResponse> {
        val storyOptional = storyRepository.findById(storyId)
        if (storyOptional.isPresent) {
            val story = storyOptional.get()
            val comment = Comment(
                id = 0,
                content = commentRequest.content,
                story = story,
                user = tokenService.getUserTokenClaims(authorization),
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
}