package com.biteSized.bitesizedv4.repository

import com.biteSized.bitesizedv4.model.Followers
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface FollowersRepository : JpaRepository<Followers, Long> {
    fun findByMainUserAndFollower(mainUser: Long, follower: Long): Optional<Followers>
}