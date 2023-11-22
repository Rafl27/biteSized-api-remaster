package com.biteSized.bitesizedv4.repository

import com.biteSized.bitesizedv4.model.Followers
import org.springframework.data.jpa.repository.JpaRepository

interface FollowersRepository : JpaRepository<Followers, Long> {
}