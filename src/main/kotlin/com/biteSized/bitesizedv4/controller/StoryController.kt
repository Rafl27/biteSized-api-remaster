package com.biteSized.bitesizedv4.controller

import com.biteSized.bitesizedv4.model.Story
import com.biteSized.bitesizedv4.model.User
import com.biteSized.bitesizedv4.repository.StoryRepository
import com.biteSized.bitesizedv4.security.JwtUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.logging.Logger

@RestController
@RequestMapping("/story")
class StoryController(@Autowired private val storyRepository: StoryRepository, @Autowired private val jwtUtil: JwtUtil) {

    private val logger: Logger = Logger.getLogger(StoryController::class.java.name)
    @PostMapping
    fun createStory(
        @RequestBody newStory: Story,
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<Story> {
        // Extract the token from the authorization header
        val token = authorizationHeader.replace("Bearer ", "")

        // Decode the JWT token and retrieve the claims
        val claims = jwtUtil.decodeToken(token)

        // Extract the user information from the claims
        val userId = claims.get("id", Integer::class.java)
        val username = claims.get("username", String::class.java)
        val profilePicture = claims.get("profilePicture", String::class.java)
        // Set the user information in the new story object
        newStory.user = User(userId.toLong(), username, profilePicture = profilePicture)

        val createdStory = storyRepository.save(newStory)
        logger.info("New story created: ${createdStory.title}")
        return ResponseEntity(createdStory, HttpStatus.CREATED)
    }
}