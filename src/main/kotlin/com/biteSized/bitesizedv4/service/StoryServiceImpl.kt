package com.biteSized.bitesizedv4.service

import com.biteSized.bitesizedv4.DTO.DownvoteResponse
import com.biteSized.bitesizedv4.DTO.SingleStory
import com.biteSized.bitesizedv4.DTO.UpvoteResponse
import com.biteSized.bitesizedv4.DTO.UserStories
import com.biteSized.bitesizedv4.controller.StoryController
import com.biteSized.bitesizedv4.model.Story
import com.biteSized.bitesizedv4.repository.StoryRepository
import com.biteSized.bitesizedv4.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*
import java.util.logging.Logger

@Service
class StoryServiceImpl (private val storyRepository: StoryRepository,
                        private val tokenService: TokenService,
                        private val userRepository: UserRepository
) : StoryService{
    private val logger: Logger = Logger.getLogger(StoryController::class.java.name)
    override fun createStory(newStory: Story, authorization: String): ResponseEntity<Story> {
        newStory.user = tokenService.getUserTokenClaims(authorization)
        newStory.date = Date()
        val createdStory = storyRepository.save(newStory)
        logger.info("New story created: ${createdStory.title}")
        return ResponseEntity(createdStory, HttpStatus.CREATED)
    }

    override fun getCurrentUserStories(authorization: String): ResponseEntity<List<UserStories>> {
        val user = userRepository.findById(tokenService.getUserId(authorization).toLong())
        if (user.isPresent) {
            val stories = user.get().stories.map { story ->
                UserStories(story.id, story.title, story.content)
            }
            return ResponseEntity(stories, HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    //TODO make an endpoint to find all of the stories of a certain user (by the id)

    override fun getStoryById(id: Long): ResponseEntity<SingleStory> {
        val story = storyRepository.findById(id)
        if (story.isPresent) {
            val storyEntity = story.get()
            val storyResponse = SingleStory(storyEntity.id, storyEntity.title, storyEntity.content)
            return ResponseEntity(storyResponse, HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    override fun deleteStoryById(id: Long): ResponseEntity<String> {
        val story = storyRepository.findById(id)
        if (story.isPresent) {
            val deletedStory = story.get()
            storyRepository.deleteById(id)
            val responseMessage = "Story with ID $id (${deletedStory.title}) has been deleted."
            return ResponseEntity(responseMessage, HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    override fun updateStoryById(id: Long, updatedStory : Story): ResponseEntity<Story> {
        val existingStory = storyRepository.findById(id)
        if (existingStory.isPresent) {
            val story = existingStory.get()
            story.title = updatedStory.title
            story.content = updatedStory.content
            val updatedStoryEntity = storyRepository.save(story)
            return ResponseEntity(updatedStoryEntity, HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    override fun storyUpvote(storyId: Long, authorization: String): ResponseEntity<UpvoteResponse> {
        val story = storyRepository.findById(storyId)
        if (story.isPresent) {
            val storyEntity = story.get()
            //?: elvis operator, if upvotes is null it returns 0
            storyEntity.upvotes = (storyEntity.upvotes ?: 0) + 1
            storyRepository.save(storyEntity)
            val upvoteResponse = UpvoteResponse(id = storyId, upvote = storyEntity.upvotes)
            return ResponseEntity(upvoteResponse, HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    override fun storyDownvote(storyId: Long, authorization: String): ResponseEntity<DownvoteResponse> {
        val story = storyRepository.findById(storyId)
        if (story.isPresent) {
            val storyEntity = story.get()
            //?: elvis operator, if upvotes is null it returns 0
            storyEntity.downvotes = (storyEntity.downvotes ?: 0) + 1
            storyRepository.save(storyEntity)
            val downVoteResponse = DownvoteResponse(id = storyId, downvote = storyEntity.downvotes)
            return ResponseEntity(downVoteResponse, HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}