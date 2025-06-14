package com.biteSized.bitesizedv4.service

import com.biteSized.bitesizedv4.DTO.*
import com.biteSized.bitesizedv4.controller.UserController
import com.biteSized.bitesizedv4.model.User
import com.biteSized.bitesizedv4.repository.UserRepository
import com.biteSized.bitesizedv4.repository.UserTotalVotesRepository
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
    private val userTotalVotesRepo: UserTotalVotesRepository,
    private val jwtUtil: JwtUtil,
    private val tokenService: TokenService,
    private val stringArrayIntoDTO: StringArrayIntoDTO
) : UserService {
    private val logger: Logger = Logger.getLogger(UserController::class.java.name)
    override fun createUser(newUser: User): ResponseEntity<User> {

        val existingUser = userRepository.existsByEmail(newUser.email)
        // Email already exists
        if (existingUser) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null)
        }else{
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


    }

    override fun login(loginRequest: LoginRequest): ResponseEntity<String> {
        val (email, password) = loginRequest

        val user = userRepository.findByEmail(email)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.")

        if (!BCrypt.checkpw(password, user.password)) {
            logger.warning("Failed login attempt for user: $email")
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.")
        }

        val claims = mapOf(
            "id" to user.id,
            "username" to user.username,
            "profilePicture" to user.profilePicture,
            "email" to user.email
        )

        val token = jwtUtil.generateToken(claims)

        logger.info("User logged in: $email")
        return ResponseEntity.ok(token)
    }

    override fun userInfo(authorizationHeader: String?, userId: Int?): ResponseEntity<Any> {
        try {
            var queryResult: Array<String> = emptyArray()
            if(userId != null){
                val queryResultById = userRepository.getUserBasicInfoById(userId.toLong())
                queryResult = queryResultById
            }
            else{
                val userId = authorizationHeader?.let { tokenService.getUserId(it) }
                val queryResultByToken = userId?.let { userRepository.getUserBasicInfoById(it.toLong()) }
                if (queryResultByToken != null) {
                    queryResult = queryResultByToken
                }
            }

            if (queryResult.isNotEmpty()) {
                val resultString = queryResult[0]
                val parts = resultString.split(",")

                if (parts.size >= 4) {
                    val username = parts[0]
                    val email = parts[1]
                    val profilePicture = parts[2]
                    val id = parts[3]
                    val userBasicInfo = UserBasicInfo(username, email, profilePicture, id)
                    return ResponseEntity.ok(userBasicInfo)
                }
            }

            return ResponseEntity.notFound().build()
        } catch (e: Exception) {
            val errorMessage = "An error occurred: ${e.message}"
            println(errorMessage)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage)
        }
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

    override fun getTotalVotes (userId: Int) : ResponseEntity<UserTotalVotesDto> {
        val userTotalVotesEntity = userTotalVotesRepo.findByUserId(userId.toLong())
            ?: return ResponseEntity.notFound().build()

        val userTotalVotesDto = userTotalVotesEntity.upvotes?.let {
            userTotalVotesEntity.downvotes?.let { it1 ->
                UserTotalVotesDto(
                    userId = userId,
                    upvotes = it,
                    downvotes = it1
                )
            }
        }

        return ResponseEntity.ok(userTotalVotesDto)
    }

}