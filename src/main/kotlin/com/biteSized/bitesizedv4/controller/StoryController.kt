package com.biteSized.bitesizedv4.controller

import com.biteSized.bitesizedv4.model.Story
import com.biteSized.bitesizedv4.repository.StoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger

@RestController
@RequestMapping("/story")
class StoryController(@Autowired private val storyRepository: StoryRepository) {

    private val logger: Logger = Logger.getLogger(StoryController::class.java.name)
    @PostMapping
    fun createStory(@RequestBody newStory: Story): ResponseEntity<Story> {
        val createdStory = storyRepository.save(newStory)
        logger.info("New story created: ${createdStory.title}")
        return ResponseEntity(createdStory, HttpStatus.CREATED)
    }
}