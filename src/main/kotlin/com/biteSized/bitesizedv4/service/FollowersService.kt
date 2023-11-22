package com.biteSized.bitesizedv4.service

import com.biteSized.bitesizedv4.DTO.FollowResponse
import com.biteSized.bitesizedv4.model.Followers
import org.springframework.http.ResponseEntity

interface FollowersService {
    fun followUser(mainUserId : Long, follower : Long) : ResponseEntity<Followers>
}