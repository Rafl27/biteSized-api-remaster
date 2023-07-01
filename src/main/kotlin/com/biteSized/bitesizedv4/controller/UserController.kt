package com.biteSized.bitesizedv4.controller

import com.biteSized.bitesizedv4.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import com.biteSized.bitesizedv4.model.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(@Autowired private val userRepository: UserRepository) {

    @PostMapping
    fun createUser(@RequestBody newUser: User) : ResponseEntity<User> {
        val createdUser = userRepository.save(newUser)
        return ResponseEntity(createdUser, HttpStatus.CREATED)
    }
}