package com.biteSized.bitesizedv4.controller

import com.biteSized.bitesizedv4.DTO.LoginRequest
import com.biteSized.bitesizedv4.DTO.UserBasicInfo
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
    fun userInfo(@RequestHeader("Authorization") authorizationHeader: String) : ResponseEntity<UserBasicInfo>{
        return userService.userInfo(authorizationHeader)
    }
    @GetMapping("/info/story/{storyId}")
    fun userInfoBasedOnStoryId(@PathVariable storyId: Int): ResponseEntity<UserBasicInfo>{
        return userService.userInfoBasedOnStoryId(storyId)
    }
}