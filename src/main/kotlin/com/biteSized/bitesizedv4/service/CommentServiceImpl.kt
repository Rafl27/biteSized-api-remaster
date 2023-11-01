package com.biteSized.bitesizedv4.service

import com.biteSized.bitesizedv4.DTO.*
import com.biteSized.bitesizedv4.enums.VoteType
import com.biteSized.bitesizedv4.exception.AlreadyVotedException
import com.biteSized.bitesizedv4.exception.NotFoundException
import com.biteSized.bitesizedv4.model.Comment
import com.biteSized.bitesizedv4.model.CommentVote
import com.biteSized.bitesizedv4.repository.CommentRepository
import com.biteSized.bitesizedv4.repository.StoryRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.sql.Timestamp
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

    override fun commentUpvote(commentId: Long, userId: Long, token: String): UpvoteResponse {
        val comment = commentRepository.findById(commentId)

        if (comment.isPresent) {
            val commentEntity = comment.get()

            if (commentEntity.votes.any {it.user.id == userId}) {
                throw AlreadyVotedException("You have already voted on this comment.")
            }

            val upvote = CommentVote(comment = commentEntity, user = tokenService.getUserTokenClaims(token), voteType = VoteType.UPVOTE)
            commentEntity.votes.add(upvote)
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

    override fun storyComments(storyId: Long): ResponseEntity<List<StoryCommentsResponse>> {
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
                    result[9] as String,
                    result[10] as String,
                    result[11] as? String ?: ""
                )
            }
            return ResponseEntity.ok(response)
        } else {
            return ResponseEntity.notFound().build()
        }
    }

    override fun createReply(parentCommentId: Long, replyRequest: CommentReplyRequest, authorization: String) : ResponseEntity<CommentResponse> {
        val parentCommentOptional = commentRepository.findById(parentCommentId)
        if (parentCommentOptional.isPresent) {
            val parentComment = parentCommentOptional.get()
            val replyComment = Comment(
                id = 0,
                content = replyRequest.content,
                story = parentComment.story,
                user = tokenService.getUserTokenClaims(authorization),
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

    override fun singleComment(commentId: Long): ResponseEntity<List<StoryCommentsResponse>> {
        val queryResult = commentRepository.getSingleComment(commentId)
        val response = queryResult.map { result ->
            StoryCommentsResponse(
                idStory = result[0] as Long,
                idComment = result[1] as Long,
                contentComment = result[2] as String,
                artComment = result[3] as? String,
                parentCommentId = result[4] as? Long,
                dateComment = result[5] as Timestamp,
                downvotesComment = result[6] as Int,
                upvotesComment = result[7] as Int,
                useridComment = result[8] as Long,
                userProfilePic = result[9] as String,
                userUsername = result[10] as String,
                userEmail = result[11] as? String ?: ""
            )
        }
        return ResponseEntity.ok(response)
    }

    override fun getThreadSegmentation(commentId: Long): ResponseEntity<List<CommentThreadsUser>> {
        val queryResult = commentRepository.threadSegmentation(commentId)
        val response = queryResult.map {
            result -> CommentThreadsUser(
                userId = result[0] as Long,
                email = result[1] as String,
                profilePicture = result[2] as String,
                username = result[3] as String,
                commentId = result[4] as Long,
                art = result[5] as? String,
                content = result[6] as String,
                date = result[7] as Timestamp,
                downvotes = result[8] as Int,
                upvotes = result[9] as Int,
                parentId = result[10] as? Long,
                storyId = result[11] as Long
            )
        }
        return ResponseEntity.ok(response)
    }
}