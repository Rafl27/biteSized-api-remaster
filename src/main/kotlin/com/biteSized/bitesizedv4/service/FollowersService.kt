package com.biteSized.bitesizedv4.service

import com.biteSized.bitesizedv4.DTO.FollowerCount
import com.biteSized.bitesizedv4.DTO.UnfollowResponse
import com.biteSized.bitesizedv4.model.Followers
import org.springframework.http.ResponseEntity

interface FollowersService {
    fun followUser(mainUserId : Long, follower : Long) : ResponseEntity<Followers>
    fun unfollowUser(mainUserId : Long, follower : Long) : ResponseEntity<UnfollowResponse>
    fun followerCount(userId : Long) : ResponseEntity<FollowerCount>
}