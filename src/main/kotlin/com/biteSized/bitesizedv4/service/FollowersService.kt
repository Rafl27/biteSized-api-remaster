package com.biteSized.bitesizedv4.service

import com.biteSized.bitesizedv4.DTO.CheckFollowers
import com.biteSized.bitesizedv4.DTO.CheckFollowing
import com.biteSized.bitesizedv4.DTO.FollowerCount
import com.biteSized.bitesizedv4.DTO.UnfollowResponse
import com.biteSized.bitesizedv4.model.Followers
import org.springframework.http.ResponseEntity

interface FollowersService {
    fun followUser(mainUserId : Long, follower : Long) : ResponseEntity<Followers>
    fun unfollowUser(mainUserId : Long, follower : Long) : ResponseEntity<UnfollowResponse>
    fun followerCount(userId : Long) : ResponseEntity<FollowerCount>
    fun checkFollowers(userId : Long) : ResponseEntity<List<CheckFollowers>>
    fun checkFollowing(userId : Long) : ResponseEntity<List<CheckFollowing>>
    fun followingCount(userId : Long) : ResponseEntity<FollowerCount>
}