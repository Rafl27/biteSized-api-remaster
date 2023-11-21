package com.biteSized.bitesizedv4.service

import com.biteSized.bitesizedv4.DTO.*
import com.biteSized.bitesizedv4.model.Story
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity

interface StoryService {
    fun createStory(newStory: Story, authorization: String): ResponseEntity<Story>
    fun getCurrentUserStories(authorization: String) : ResponseEntity<List<UserStories>>
    fun getUserStories(userId: Long) : ResponseEntity<List<UserStories>>
    fun getStoryById(id : Long) : ResponseEntity<SingleStory>
    fun deleteStoryById(id : Long): ResponseEntity<String>
    fun updateStoryById (id: Long, updatedStory : Story) : ResponseEntity<Story>
    fun storyUpvote(storyId: Long, userId : Long, authorization: String) : ResponseEntity<UpvoteResponse>
    fun storyDownvote(storyId: Long, userId : Long, authorization: String) : ResponseEntity<DownvoteResponse>
    fun allStories(page: Int, size: Int) : Page<CompleteStoryNoComments>
    fun threadsTotalUpvoteDownvote(storyId : Long) : ResponseEntity<List<ThreadsTotalUpvoteDownvote>>
    fun totalComments(storyId: Long) : ResponseEntity<List<TotalComments>>
}