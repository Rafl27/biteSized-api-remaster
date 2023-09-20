package com.biteSized.bitesizedv4.repository

import com.biteSized.bitesizedv4.DTO.ThreadsTotalUpvoteDownvote
import com.biteSized.bitesizedv4.model.Story
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface StoryRepository :JpaRepository<Story, Long> {
    @Query("SELECT story.id as idStory, comment.id as idComment, comment.content as contentComment," +
            "comment.art as artComment, comment.parent_id as parentCommentId, comment.date as dateComment," +
            "comment.downvotes as downvotesComment, comment.upvotes as upvotesCommment, comment.user_id as useridComment, profile_picture as userProfilePic, username as userUsername, email as userEmail " +
            "FROM story JOIN comment ON story.id = comment.story_id JOIN user ON comment.user_id = user.id WHERE story.id = :storyId ORDER BY comment.upvotes DESC", nativeQuery = true)
    fun getStoryComments(@Param("storyId") storyId: Long): List<Array<Any>>

    @Query("SELECT profile_picture, username, art, date, downvotes, upvotes, title, content, story.id as storyId FROM bitesized.user\n" +
            "JOIN story ON story.user_id = user.id\n" +
            "ORDER BY upvotes DESC " +
            "LIMIT :size OFFSET :offset ", nativeQuery = true)
    fun getStoryAndUserNoComment(@Param("size") size: Int,
                                 @Param("offset") offset: Int) : List<Array<Any>>

    @Query("SELECT COUNT(id) as numberOfStories from story", nativeQuery = true)
    fun getNumberOfStories() : Long

    @Query(
        value = "select story_id, sum(upvotes) as totalUpvotes, sum(downvotes) as totalDownvotes from comment where story_id = :storyId",
        nativeQuery = true)
    fun getThreadsTotalUpvotesDownvotes(@Param("storyId") storyId: Long) : List<Array<Any>>

}