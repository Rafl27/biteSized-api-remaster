package com.biteSized.bitesizedv4.repository

import com.biteSized.bitesizedv4.model.Story
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface StoryRepository :JpaRepository<Story, Long> {
    @Query("SELECT story.title as titleStory, story.content as contentStory, story.art as artStory, " +
            "story.date as dateStory, story.upvotes as upvotesStory, story.downvotes as downvotesStory, " +
            "comment.content as contentComment, comment.art as artComment, comment.date as dateComment, " +
            "comment.upvotes as upvotesComment, comment.downvotes as downvotesComment " +
            "FROM story JOIN comment ON story.id = comment.story_id WHERE story.id = :storyId ORDER BY comment.upvotes DESC", nativeQuery = true)
    fun getCompleteStoryAndComments(@Param("storyId") storyId: Long): List<Array<Any>>
}