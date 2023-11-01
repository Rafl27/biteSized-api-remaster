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

    @Query("WITH RECURSIVE CommentHierarchy AS (\n" +
            "    SELECT user.id as userId, email, profile_picture, username, comment.id as commentId, art,content, date, downvotes, upvotes, parent_id, story_id\n" +
            "    FROM comment\n" +
            "    join user on comment.user_id = user.id\n" +
            "    WHERE comment.id = :commentId\n" +
            "\n" +
            "    UNION ALL\n" +
            "\n" +
            "    SELECT u.id as userId, u.email, u.profile_picture, u.username, c.id as commentId, c.art, c.content, c.date, c.downvotes, c.upvotes, c.parent_id, c.story_id\n" +
            "    FROM comment c\n" +
            "        join user u on c.user_id = u.id\n" +
            "             JOIN CommentHierarchy ch ON c.parent_id = ch.commentId\n" +
            ")\n" +
            "SELECT * FROM CommentHierarchy", nativeQuery = true)
    fun threadSegmentation(@Param("commentId") commentId: Long) : List<Array<Any>>
}