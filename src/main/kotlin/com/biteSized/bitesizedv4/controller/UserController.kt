package com.biteSized.bitesizedv4.controller

import com.biteSized.bitesizedv4.DTO.LoginRequest
import com.biteSized.bitesizedv4.model.User
import com.biteSized.bitesizedv4.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger


@RestController
@RequestMapping("/user")
class UserController(@Autowired private val userRepository: UserRepository) {

    private val logger: Logger = Logger.getLogger(UserController::class.java.name)
    @PostMapping
    fun createUser(@RequestBody newUser: User) : ResponseEntity<User> {
        val createdUser = userRepository.save(newUser)
        logger.info("New user created: ${createdUser.username}")
        return ResponseEntity(createdUser, HttpStatus.CREATED)
    }

    @GetMapping
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<String> {
        val username = loginRequest.username
        val password = loginRequest.password

        val user: User = userRepository.findByUsernameAndPassword(username, password)
        return if (user != null) {
            logger.info("User logged in: $username")
            ResponseEntity.ok("Login successful!")
        } else {
            logger.warning("Failed login attempt for user: $username")
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.")
        }
    }
}