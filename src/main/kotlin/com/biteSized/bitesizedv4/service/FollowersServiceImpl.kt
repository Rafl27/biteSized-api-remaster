package com.biteSized.bitesizedv4.service

import com.biteSized.bitesizedv4.DTO.CheckFollowers
import com.biteSized.bitesizedv4.DTO.CheckFollowing
import com.biteSized.bitesizedv4.DTO.FollowerCount
import com.biteSized.bitesizedv4.DTO.UnfollowResponse
import com.biteSized.bitesizedv4.controller.StoryController
import com.biteSized.bitesizedv4.model.Followers
import com.biteSized.bitesizedv4.repository.FollowersRepository
import org.hibernate.annotations.Check
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

    override fun followerCount(userId: Long) : ResponseEntity<FollowerCount> {
            val totalFollowers = FollowerCount(followersRepository.getFollowerCount(userId))
            return ResponseEntity(totalFollowers, HttpStatus.OK)
    }

    override fun checkFollowers(userId: Long): ResponseEntity<List<CheckFollowers>> {
        val queryResult = followersRepository.checkFollowers(userId)
        val followers = queryResult.map { follows ->
            CheckFollowers(
                    follows[0] as Long,
                    follows[1] as Long
            )
        }
        return ResponseEntity(followers, HttpStatus.OK)
    }

    override fun checkFollowing(userId: Long): ResponseEntity<List<CheckFollowing>> {
        val queryResult = followersRepository.checkFollowing(userId)
        val following = queryResult.map { follows ->
            CheckFollowing(
                    follows[0] as Long,
                    follows[1] as Long
            )
        }
        return ResponseEntity(following, HttpStatus.OK)
    }
}