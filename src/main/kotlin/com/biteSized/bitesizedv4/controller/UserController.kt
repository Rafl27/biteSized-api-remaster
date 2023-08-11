package com.biteSized.bitesizedv4.controller

import com.biteSized.bitesizedv4.DTO.LoginRequest
import com.biteSized.bitesizedv4.model.User
import com.biteSized.bitesizedv4.repository.UserRepository
import com.biteSized.bitesizedv4.security.JwtUtil
import com.biteSized.bitesizedv4.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.logging.Logger
import kotlin.random.Random


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
}