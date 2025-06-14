package com.biteSized.bitesizedv4.controller

import com.biteSized.bitesizedv4.DTO.*
import com.biteSized.bitesizedv4.model.User
import com.biteSized.bitesizedv4.repository.UserRepository
import com.biteSized.bitesizedv4.security.JwtUtil
import com.biteSized.bitesizedv4.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/user")
@CrossOrigin(origins = arrayOf("http://localhost:5173"))
class UserController(@Autowired private val userRepository: UserRepository, @Autowired private val jwtUtil: JwtUtil, private val userService: UserService) {
    @PostMapping
    @RequestMapping("/signup")
    fun createUser(@RequestBody newUser: User): ResponseEntity<User> {
        return userService.createUser(newUser)
    }
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<String> {
        return userService.login(loginRequest)
    }
    @GetMapping("/info")
    fun userInfo(@RequestHeader("Authorization") authorizationHeader: String) : ResponseEntity<Any>{
        return userService.userInfo(authorizationHeader, null)
    }
    @GetMapping("{userId}/info-visit-profile")
    fun userInfoVisit(@PathVariable userId: Int) : ResponseEntity<Any>{
        return userService.userInfo(null, userId)
    }
    @GetMapping("/info/story/{storyId}")
    fun userInfoBasedOnStoryId(@PathVariable storyId: Int): ResponseEntity<UserBasicInfo>{
        return userService.userInfoBasedOnStoryId(storyId)
    }
    @GetMapping("/info/comment/{userId}")
    fun userInfoBasedOnCommentId(@PathVariable userId: Int) : ResponseEntity<UserBasicInfo>{
        return userService.userInfoBasedOnCommentId(userId)
    }

    @PostMapping("/bio/{userId}")
    fun createBio(@PathVariable userId : Int, @RequestBody bio : Bio, @RequestHeader("Authorization") authorizationHeader: String) : ResponseEntity<UserBio>{
        return userService.createBio(userId, bio, authorizationHeader)
    }

    @GetMapping("/bio/{userId}")
    fun getBio(@PathVariable userId : Int) : ResponseEntity<UserBio>{
        return userService.getBio(userId)
    }

    @GetMapping("/info/votes/{userId}")
    fun getVotes(@PathVariable userId : Int) : ResponseEntity<UserTotalVotesDto>{
        return userService.getTotalVotes(userId)
    }
}