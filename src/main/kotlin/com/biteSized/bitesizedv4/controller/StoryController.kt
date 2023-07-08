package com.biteSized.bitesizedv4.controller

import com.biteSized.bitesizedv4.model.Story
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
        val token = authorizationHeader.replace("Bearer ", "")

        if (jwtUtil.validateToken(token)) {
            val username = jwtUtil.extractUsername(token)
            // Check if the user exists and perform additional authorization if needed

            val createdStory = storyRepository.save(newStory)
            logger.info("New story created: ${createdStory.title}")
            return ResponseEntity(createdStory, HttpStatus.CREATED)
        } else {
            // Invalid or missing JWT
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }

}