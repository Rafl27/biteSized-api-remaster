package com.biteSized.bitesizedv4.repository

import com.biteSized.bitesizedv4.DTO.CompleteStoryNoComments
import com.biteSized.bitesizedv4.model.Story
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface StoryRepository :JpaRepository<Story, Long> {
    @Query("SELECT story.id as idStory, comment.id as idComment, comment.content as contentComment," +
            "comment.art as artComment, comment.parent_id as parentCommentId, comment.date as dateComment," +
            "comment.downvotes as downvotesComment, comment.upvotes as upvotesCommment, comment.user_id as useridComment " +
            "FROM story JOIN comment ON story.id = comment.story_id WHERE story.id = :storyId ORDER BY comment.upvotes DESC", nativeQuery = true)
    fun getStoryComments(@Param("storyId") storyId: Long): List<Array<Any>>

    @Query("SELECT profile_picture, username, art, date, downvotes, upvotes, title, content, story.id as storyId from bitesized.user\n" +
            "join story on story.user_id = user.id;", nativeQuery = true)
    fun getStoryAndUserNoComment() : List<Array<Any>>

}