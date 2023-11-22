package com.biteSized.bitesizedv4.repository

import com.biteSized.bitesizedv4.DTO.FollowerCount
import com.biteSized.bitesizedv4.model.Followers
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface FollowersRepository : JpaRepository<Followers, Long> {
    fun findByMainUserAndFollower(mainUser: Long, follower: Long): Optional<Followers>
    fun findByMainUser(mainUser: Long) : Optional<Followers>
    @Query("SELECT COUNT(*) from followers WHERE follower = :userId", nativeQuery = true)
    fun getFollowerCount(@Param("userId") userId : Long) : FollowerCount
}