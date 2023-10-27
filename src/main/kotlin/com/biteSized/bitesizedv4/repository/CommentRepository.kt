package com.biteSized.bitesizedv4.repository

import com.biteSized.bitesizedv4.model.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CommentRepository : JpaRepository<Comment, Long> {
    @Query("SELECT story.id as idStory, comment.id as idComment, comment.content as contentComment,\n" +
            "       comment.art as artComment, comment.parent_id as parentCommentId, comment.date as dateComment,\n" +
            "        comment.downvotes as downvotesComment, comment.upvotes as upvotesCommment, comment.user_id as useridComment,\n" +
            "        profile_picture as userProfilePic, username as userUsername, email as userEmail\n" +
            "\n" +
            "FROM story JOIN comment ON story.id = comment.story_id JOIN user ON comment.user_id = user.id\n" +
            "WHERE comment.id = :commentId ORDER BY comment.upvotes DESC", nativeQuery = true)
    fun getSingleComment(@Param("commentId") commentId: Long) : List<Array<Any>>
}