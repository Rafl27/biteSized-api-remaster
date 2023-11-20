package com.biteSized.bitesizedv4.repository

import com.biteSized.bitesizedv4.model.CommentVote
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CommentVoteRepository : JpaRepository<CommentVote, Long> {

    @Query(" SELECT comment.id, art, content, date, downvotes, upvotes, story_id, vote_type" +
            " FROM comment JOIN comment_vote ON comment_vote.comment_id = comment.id" +
            " WHERE comment_vote.user_id = :userId", nativeQuery = true)
    fun getAllUserVotes(@Param("userId") userId: Long) : List<Array<Any>>
}