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

    @Query("SELECT profile_picture, username, user_id,  art, date, downvotes, upvotes, title, content, story.id as storyId, language, (SELECT COUNT(id) FROM followers WHERE main_user = user.id) as follower_count  FROM user\n" +
            "JOIN story ON story.user_id = user.id\n" +
            "ORDER BY upvotes DESC " +
            "LIMIT :size OFFSET :offset ", nativeQuery = true)
    fun getStoryAndUserNoComment(@Param("size") size: Int,
                                 @Param("offset") offset: Int) : List<Array<Any>>

    @Query("SELECT profile_picture, username, user_id,  art, date, downvotes, upvotes, title, content, story.id as storyId, language, (SELECT COUNT(id) FROM followers WHERE main_user = user.id) as follower_count  FROM user\n" +
            "JOIN story ON story.user_id = user.id\n" +
            "ORDER BY date DESC " +
            "LIMIT :size OFFSET :offset ", nativeQuery = true)
    fun getStoryAndUserNoCommentNewest(@Param("size") size: Int,
                                 @Param("offset") offset: Int) : List<Array<Any>>

    @Query("SELECT\n" +
            "    u.profile_picture, u.username, u.id, s.art, s.date, s.downvotes, s.upvotes, s.title, s.content, s.id as storyId, s.language,\n" +
            "    (SELECT COUNT(id) FROM followers WHERE main_user = u.id) as follower_count,\n" +
            "    c.totalComments\n" +
            "FROM\n" +
            "    user u\n" +
            "        JOIN\n" +
            "    story s ON s.user_id = u.id\n" +
            "        LEFT JOIN (\n" +
            "        SELECT\n" +
            "            COUNT(story_id) as totalComments, story_id\n" +
            "        FROM\n" +
            "            comment\n" +
            "        GROUP BY\n" +
            "            story_id\n" +
            "    ) c ON c.story_id = s.id\n" +
            "ORDER BY\n" +
            "    c.totalComments DESC\n" +
            "    LIMIT :size OFFSET :offset", nativeQuery = true)
    fun getStoryAndUserNoCommentHot(@Param("size") size: Int,
                                       @Param("offset") offset: Int) : List<Array<Any>>

    @Query("SELECT COUNT(id) as numberOfStories from story", nativeQuery = true)
    fun getNumberOfStories() : Long

    @Query(
        value = "select story_id, sum(upvotes) as totalUpvotes, sum(downvotes) as totalDownvotes from comment where story_id = :storyId",
        nativeQuery = true)
    fun getThreadsTotalUpvotesDownvotes(@Param("storyId") storyId: Long) : List<Array<Any>>

    @Query("SELECT COUNT(story_id) as totalComments, story_id as storyId from comment where story_id = :storyId", nativeQuery = true)
    fun getTotalComments(@Param("storyId") storyId: Long): List<Array<Any>>
}