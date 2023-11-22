package com.biteSized.bitesizedv4.service

import com.biteSized.bitesizedv4.DTO.UnfollowResponse
import com.biteSized.bitesizedv4.controller.StoryController
import com.biteSized.bitesizedv4.model.Followers
import com.biteSized.bitesizedv4.repository.FollowersRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class FollowersServiceImpl (private val followersRepository: FollowersRepository) : FollowersService {
    private val logger: Logger = Logger.getLogger(StoryController::class.java.name)
    override fun followUser(mainUserId: Long, follower: Long): ResponseEntity<Followers> {
        val newFollower = Followers(mainUser = mainUserId, follower = follower)
        val followDone = followersRepository.save(newFollower)
        logger.info("$follower just followed $mainUserId")
        return ResponseEntity(followDone, HttpStatus.OK)
    }

    override fun unfollowUser(mainUserId: Long, follower: Long): ResponseEntity<UnfollowResponse> {

        val existingFollower = followersRepository.findByMainUserAndFollower(mainUserId, follower)

        return if (existingFollower.isPresent) {
            followersRepository.delete(existingFollower.get())
            logger.info("$follower just unfollowed $mainUserId")
            ResponseEntity(UnfollowResponse(mainUserId, follower), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }
}