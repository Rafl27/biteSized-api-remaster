package com.biteSized.bitesizedv4.controller

import com.biteSized.bitesizedv4.DTO.LoginRequest
import com.biteSized.bitesizedv4.model.User
import com.biteSized.bitesizedv4.repository.UserRepository
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
class UserController(@Autowired private val userRepository: UserRepository) {

    private val logger: Logger = Logger.getLogger(UserController::class.java.name)
    @PostMapping
    fun createUser(@RequestBody newUser: User) : ResponseEntity<User> {

        val hashedPassword = BCrypt.hashpw(newUser.password, BCrypt.gensalt())
        newUser.password = hashedPassword

        //generate bot avatar
        if(newUser.profilePicture == null) {
            val randomNumber = Random.nextInt(1, 5001)
            val profilePictureUrl = "https://api.dicebear.com/6.x/bottts/svg?seed=$randomNumber"
            newUser.profilePicture = profilePictureUrl
        }

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