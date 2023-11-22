package com.biteSized.bitesizedv4.repository

import com.biteSized.bitesizedv4.DTO.CheckFollowers
import com.biteSized.bitesizedv4.DTO.FollowerCount
import com.biteSized.bitesizedv4.model.Followers
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface FollowersRepository : JpaRepository<Followers, Long> {
    fun findByMainUserAndFollower(mainUser: Long, follower: Long): Optional<Followers>
    fun findByMainUser(mainUser: Long) : Optional<Followers>
    @Query("SELECT COUNT(id) FROM followers WHERE main_user = :userId", nativeQuery = true)
    fun getFollowerCount(@Param("userId") userId : Long) : Long

    @Query("SELECT follower, main_user FROM followers WHERE main_user = :userId", nativeQuery = true)
    fun checkFollowers(@Param("userId") userId: Long) : List<Array<Any>>
}