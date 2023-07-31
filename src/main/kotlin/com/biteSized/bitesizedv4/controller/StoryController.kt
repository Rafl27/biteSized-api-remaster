package com.biteSized.bitesizedv4.controller

import com.biteSized.bitesizedv4.DTO.*
import com.biteSized.bitesizedv4.model.Comment
import com.biteSized.bitesizedv4.model.Story
import com.biteSized.bitesizedv4.model.User
import com.biteSized.bitesizedv4.repository.StoryRepository
import com.biteSized.bitesizedv4.repository.UserRepository
import com.biteSized.bitesizedv4.security.JwtUtil
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.Authorization
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.util.*
import java.util.logging.Logger

@RestController
@RequestMapping("/story")
class StoryController(@Autowired private val storyRepository: StoryRepository, @Autowired private val jwtUtil: JwtUtil, @Autowired private val userRepository: UserRepository) {

    private val logger: Logger = Logger.getLogger(StoryController::class.java.name)
    @PostMapping
    fun createStory(
        @RequestBody newStory: Story,
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<Story> {
        val token = authorizationHeader.replace("Bearer ", "")
        val claims = jwtUtil.decodeToken(token)
        val userId = claims.get("id", Integer::class.java)
        val username = claims.get("username", String::class.java)
        val profilePicture = claims.get("profilePicture", String::class.java)
        newStory.user = User(userId.toLong(), username, profilePicture = profilePicture)
        newStory.date = Date()
        val createdStory = storyRepository.save(newStory)
        logger.info("New story created: ${createdStory.title}")
        return ResponseEntity(createdStory, HttpStatus.CREATED)
    }

    @GetMapping("/user")
    fun getUserStories(@RequestHeader("Authorization") authorizationHeader: String): ResponseEntity<List<UserStories>> {
        val token = authorizationHeader.replace("Bearer ", "")
        val claims = jwtUtil.decodeToken(token)
        val userId = claims.get("id", Integer::class.java)

        val user = userRepository.findById(userId.toLong())
        if (user.isPresent) {
            val stories = user.get().stories.map { story ->
                UserStories(story.id, story.title, story.content)
            }
            return ResponseEntity(stories, HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("/{id}")
    fun getStoryById(@PathVariable id: Long): ResponseEntity<SingleStory> {
        val story = storyRepository.findById(id)
        if (story.isPresent) {
            val storyEntity = story.get()
            val storyResponse = SingleStory(storyEntity.id, storyEntity.title, storyEntity.content)
            return ResponseEntity(storyResponse, HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    //TODO: delete should work only if the tokens userId matches the storyUserId
    @DeleteMapping("/{id}")
    fun deleteStoryById(@PathVariable id: Long): ResponseEntity<String> {
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

    @PutMapping("/{id}")
    fun updateStoryById(
        @PathVariable id: Long,
        @RequestBody updatedStory: Story
    ): ResponseEntity<Story> {
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

    @PostMapping("/{storyId}/upvote")
    fun storyUpvote(@PathVariable storyId: Long,
                    @RequestHeader("Authorization") authorizationHeader: String) : ResponseEntity<UpvoteResponse>{
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
    @PostMapping("/{storyId}/downvote")
    @ApiOperation(value = "downvote stories")
    fun storyDownvote(@PathVariable storyId: Long,
                    @RequestHeader("Authorization") authorizationHeader: String) : ResponseEntity<DownvoteResponse>{
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