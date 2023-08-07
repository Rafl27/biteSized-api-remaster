package com.biteSized.bitesizedv4.controller

import com.biteSized.bitesizedv4.DTO.LoginRequest
import com.biteSized.bitesizedv4.model.User
import com.biteSized.bitesizedv4.repository.UserRepository
import com.biteSized.bitesizedv4.security.JwtUtil
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
class UserController(@Autowired private val userRepository: UserRepository, @Autowired private val jwtUtil: JwtUtil) {

    private val logger: Logger = Logger.getLogger(UserController::class.java.name)

    @PostMapping
    @RequestMapping("/signup")
    fun createUser(@RequestBody newUser: User): ResponseEntity<User> {
        val hashedPassword = BCrypt.hashpw(newUser.password, BCrypt.gensalt())
        newUser.password = hashedPassword

        //generate bot avatar
        if (newUser.profilePicture == "") {
            val randomNumber = Random.nextInt(1, 5001)
            val profilePictureUrl = "https://api.dicebear.com/6.x/bottts/svg?seed=$randomNumber"
            newUser.profilePicture = profilePictureUrl
        }

        val createdUser = userRepository.save(newUser)
        logger.info("New user created: ${createdUser.username}")
        return ResponseEntity(createdUser, HttpStatus.CREATED)
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<String> {
        val email = loginRequest.email
        val password = loginRequest.password

        val user: User = userRepository.findByEmail(email)

        if (BCrypt.checkpw(password, user.password)) {
            logger.info("User logged in: $email")

            // Create claims for the JWT token
            val claims = HashMap<String, Any>()
            claims["id"] = user.id
            claims["username"] = user.username
            claims["profilePicture"] = user.profilePicture

            val token = jwtUtil.generateToken(claims)

            return ResponseEntity.ok(token)
        } else {
            logger.warning("Failed login attempt for user: $email")
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.")
        }
    }
}