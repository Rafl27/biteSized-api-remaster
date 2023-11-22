package com.biteSized.bitesizedv4.controller

import com.biteSized.bitesizedv4.DTO.CheckFollowers
import com.biteSized.bitesizedv4.DTO.FollowerCount
import com.biteSized.bitesizedv4.DTO.UnfollowResponse
import com.biteSized.bitesizedv4.model.Followers
import com.biteSized.bitesizedv4.service.FollowersService
import com.biteSized.bitesizedv4.service.TokenService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/followers")
@CrossOrigin(origins = arrayOf("http://localhost:5173"))
class FollowersController (private val followersService: FollowersService ,private val tokenService: TokenService) {

    @PostMapping("{userId}/follow")
    fun followUser(@PathVariable userId : Long, @RequestHeader("Authorization") authorizationHeader: String) : ResponseEntity<Followers> {
        val follower = tokenService.getUserId(authorizationHeader).toLong()
        return followersService.followUser(userId, follower)
    }

    @PostMapping("{userId}/unfollow")
    fun unfollowUser(@PathVariable userId : Long, @RequestHeader("Authorization") authorizationHeader: String) : ResponseEntity<UnfollowResponse>{
        val follower = tokenService.getUserId(authorizationHeader).toLong()
        return followersService.unfollowUser(userId, follower)
    }

    @GetMapping("{userId}/follower-count")
    fun followerCount(@PathVariable userId : Long) : ResponseEntity<FollowerCount>{
        return followersService.followerCount(userId)
    }

    @GetMapping("{userId}/check-followers")
    fun checkFollowers(@PathVariable userId : Long) : ResponseEntity<List<CheckFollowers>>{
        return followersService.checkFollowers(userId)
    }
}