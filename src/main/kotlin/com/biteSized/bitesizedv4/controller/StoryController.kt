package com.biteSized.bitesizedv4.controller

import com.biteSized.bitesizedv4.DTO.*
import com.biteSized.bitesizedv4.model.Story
import com.biteSized.bitesizedv4.repository.StoryRepository
import com.biteSized.bitesizedv4.repository.UserRepository
import com.biteSized.bitesizedv4.security.JwtUtil
import com.biteSized.bitesizedv4.service.StoryService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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

    @PutMapping("/{storyId}/upvote")
    fun storyUpvote(@PathVariable storyId: Long,
                    @RequestHeader("Authorization") authorizationHeader: String) : ResponseEntity<UpvoteResponse>{
        return storyService.storyUpvote(storyId, authorizationHeader)
}
    @PutMapping("/{storyId}/downvote")
    @ApiOperation(value = "downvote stories")
    fun storyDownvote(@PathVariable storyId: Long,
                    @RequestHeader("Authorization") authorizationHeader: String) : ResponseEntity<DownvoteResponse>{
        return storyService.storyDownvote(storyId, authorizationHeader)
    }

    @GetMapping("/all")
    @ApiOperation(value = "Gets all the stories (no comments)")
    fun allStories(@RequestParam(defaultValue = "0") page: Int,
                   @RequestParam(defaultValue = "10") size: Int): ResponseEntity<Page<CompleteStoryNoComments>> {
        val storiesPage = storyService.allStories(page, size)
        return ResponseEntity.ok(storiesPage)
    }

    @GetMapping("/{storyId}/total-up-down")
    @ApiOperation("Total threads upvotes and downvotes for a certain story")
    fun threadsTotalUpvoteDownvote(@PathVariable storyId: Long) : ResponseEntity<List<ThreadsTotalUpvoteDownvote>> {
        return storyService.threadsTotalUpvoteDownvote(storyId)
    }
}