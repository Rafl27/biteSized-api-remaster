package com.biteSized.bitesizedv4.service

import com.biteSized.bitesizedv4.DTO.Bio
import com.biteSized.bitesizedv4.DTO.LoginRequest
import com.biteSized.bitesizedv4.DTO.UserBasicInfo
import com.biteSized.bitesizedv4.DTO.UserBio
import com.biteSized.bitesizedv4.controller.UserController
import com.biteSized.bitesizedv4.model.User
import com.biteSized.bitesizedv4.repository.StoryRepository
import com.biteSized.bitesizedv4.repository.UserRepository
import com.biteSized.bitesizedv4.security.JwtUtil
import com.biteSized.bitesizedv4.util.StringArrayIntoDTO
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service
import java.util.logging.Logger
import kotlin.random.Random

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val jwtUtil: JwtUtil,
    private val tokenService: TokenService,
    private val stringArrayIntoDTO: StringArrayIntoDTO
) : UserService {
    private val logger: Logger = Logger.getLogger(UserController::class.java.name)
    override fun createUser(newUser: User): ResponseEntity<User> {
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

    override fun login(loginRequest: LoginRequest): ResponseEntity<String> {
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
            claims["email"] = user.email

            val token = jwtUtil.generateToken(claims)

            return ResponseEntity.ok(token)
        } else {
            logger.warning("Failed login attempt for user: $email")
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.")
        }
    }

    override fun userInfo(authorizationHeader: String): ResponseEntity<UserBasicInfo> {
        val userId = tokenService.getUserId(authorizationHeader)
        val queryResult = userRepository.getUserBasicInfoById(userId.toLong())

        if (queryResult.isNotEmpty()) {
            val resultString = queryResult[0]
            val parts = resultString.split(",")

            if (parts.size == 3) {
                val username = parts[0]
                val email = parts[1]
                val profilePicture = parts[2]
                val userBasicInfo = UserBasicInfo(username, email, profilePicture)
                return ResponseEntity.ok(userBasicInfo)
            }
        }

        return ResponseEntity.notFound().build()
    }

    override fun userInfoBasedOnStoryId(storyId: Int): ResponseEntity<UserBasicInfo> {
        val queryResult = userRepository.getUserBasicInfoByStoryId(storyId)

        if (queryResult.isNotEmpty()) {
            return ResponseEntity.ok(stringArrayIntoDTO.userBasicInfo(queryResult))
        }
        return ResponseEntity.notFound().build()
    }

    override fun userInfoBasedOnCommentId(userId: Int): ResponseEntity<UserBasicInfo> {
        val queryResult = userRepository.getUserBasicInfoByCommentId(userId)

        if (queryResult.isNotEmpty()) {
            return ResponseEntity.ok(stringArrayIntoDTO.userBasicInfo(queryResult))
        }
        return ResponseEntity.notFound().build()
    }

    @Transactional
    override fun createBio(userId: Int, bio: Bio, authorizationHeader : String): ResponseEntity<UserBio> {
        val rowsUpdated = userRepository.postUserBio(userId, bio.bio)
        val user = tokenService.getUserTokenClaims(authorizationHeader)
        return if (rowsUpdated > 0  && userId == user.id.toInt()) {
            val userBio = UserBio(
                    userId,
                    bio.bio
            )
            ResponseEntity.ok(userBio)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    override fun getBio(userId: Int): ResponseEntity<UserBio> {
        val queryResult = userRepository.getUserBio(userId)

        if(queryResult.isNotEmpty()){
            val userBio = UserBio(
                    userId, queryResult
            )
            return ResponseEntity.ok(userBio)
        }
        return ResponseEntity.notFound().build()
    }

}