package com.biteSized.bitesizedv4.repository

import com.biteSized.bitesizedv4.model.UserTotalVotes
import org.springframework.data.jpa.repository.JpaRepository

interface UserTotalVotesRepository : JpaRepository<UserTotalVotes, Long> {
    fun findByUserId(userId: Long): UserTotalVotes?
}