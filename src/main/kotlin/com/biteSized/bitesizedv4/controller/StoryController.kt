package com.biteSized.bitesizedv4.controller

import com.biteSized.bitesizedv4.DTO.*
import com.biteSized.bitesizedv4.model.Comment
import com.biteSized.bitesizedv4.model.Story
import com.biteSized.bitesizedv4.model.User
import com.biteSized.bitesizedv4.repository.StoryRepository
import com.biteSized.bitesizedv4.repository.UserRepository
import com.biteSized.bitesizedv4.security.JwtUtil
import com.biteSized.bitesizedv4.service.StoryService
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
@CrossOrigin(origins = arrayOf("http://localhost:5173"))
class StoryController(private val storyService : StoryService, @Autowired private val storyRepository: StoryRepository, @Autowired private val jwtUtil: JwtUtil, @Autowired private val userRepository: UserRepository) {

    private val logger: Logger = Logger.getLogger(StoryController::class.java.name)
    @PostMapping
    fun createStory(
        @RequestBody newStory: Story,
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<Story> {
        return storyService.createStory(newStory, authorizationHeader)
    }

    @GetMapping("/user")
    fun getCurrentUserStories(@RequestHeader("Authorization") authorizationHeader: String): ResponseEntity<List<UserStories>> {
        return storyService.getCurrentUserStories(authorizationHeader)
    }

    @GetMapping("/{id}")
    fun getStoryById(@PathVariable id: Long): ResponseEntity<SingleStory> {
        return storyService.getStoryById(id)
    }

    //TODO: delete should work only if the tokens userId matches the storyUserId
    @DeleteMapping("/{id}")
    fun deleteStoryById(@PathVariable id: Long): ResponseEntity<String> {
        return storyService.deleteStoryById(id)
    }

    @PutMapping("/{id}")
    fun updateStoryById(
        @PathVariable id: Long,
        @RequestBody updatedStory: Story
    ): ResponseEntity<Story> {
       return storyService.updateStoryById(id, updatedStory)
    }

    @PostMapping("/{storyId}/upvote")
    fun storyUpvote(@PathVariable storyId: Long,
                    @RequestHeader("Authorization") authorizationHeader: String) : ResponseEntity<UpvoteResponse>{
        return storyService.storyUpvote(storyId, authorizationHeader)
}
    @PostMapping("/{storyId}/downvote")
    @ApiOperation(value = "downvote stories")
    fun storyDownvote(@PathVariable storyId: Long,
                    @RequestHeader("Authorization") authorizationHeader: String) : ResponseEntity<DownvoteResponse>{
        return storyService.storyDownvote(storyId, authorizationHeader)
    }

    @GetMapping("/all")
    @ApiOperation(value = "Gets all the stories (no comments)")
    fun allStories(): ResponseEntity<List<CompleteStoryNoComments>> {
        return storyService.allStories()
    }
}