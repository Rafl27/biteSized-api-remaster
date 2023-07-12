package com.biteSized.bitesizedv4.controller

import com.biteSized.bitesizedv4.DTO.SingleStory
import com.biteSized.bitesizedv4.DTO.UserStories
import com.biteSized.bitesizedv4.model.Story
import com.biteSized.bitesizedv4.model.User
import com.biteSized.bitesizedv4.repository.StoryRepository
import com.biteSized.bitesizedv4.repository.UserRepository
import com.biteSized.bitesizedv4.security.JwtUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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
}