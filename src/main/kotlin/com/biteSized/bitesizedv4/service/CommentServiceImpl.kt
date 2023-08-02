package com.biteSized.bitesizedv4.service

import com.biteSized.bitesizedv4.DTO.DownvoteResponse
import com.biteSized.bitesizedv4.DTO.UpvoteResponse
import com.biteSized.bitesizedv4.exception.NotFoundException
import com.biteSized.bitesizedv4.repository.CommentRepository
import org.springframework.stereotype.Service

@Service
class CommentServiceImpl(private val commentRepository: CommentRepository) : CommentService {
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
}