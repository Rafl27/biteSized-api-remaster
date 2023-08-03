package com.biteSized.bitesizedv4.service

import com.biteSized.bitesizedv4.DTO.DownvoteResponse
import com.biteSized.bitesizedv4.DTO.SingleStory
import com.biteSized.bitesizedv4.DTO.UpvoteResponse
import com.biteSized.bitesizedv4.DTO.UserStories
import com.biteSized.bitesizedv4.model.Story
import org.springframework.http.ResponseEntity

interface StoryService {
    fun createStory(newStory: Story, authorization: String): ResponseEntity<Story>
    fun getUserStories(authorization: String) : ResponseEntity<List<UserStories>>
    fun getStoryById(id : Long) : ResponseEntity<SingleStory>
    fun deleteStoryById(id : Long): ResponseEntity<String>
    fun updateStoryById (id: Long) : ResponseEntity<Story>
    fun storyUpvote(storyId: Long, authorization: String) : ResponseEntity<UpvoteResponse>
    fun storyDownvote(storyId: Long, authorization: String) : ResponseEntity<DownvoteResponse>
}