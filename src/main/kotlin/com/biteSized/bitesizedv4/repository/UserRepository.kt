package com.biteSized.bitesizedv4.repository

import com.biteSized.bitesizedv4.DTO.UserBasicInfo
import com.biteSized.bitesizedv4.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsernameAndPassword(username: String, password: String): User
    fun findByUsername(username: String): User
    fun findByEmail(email: String) : User
    @Query("SELECT u.username, u.email, u.profilePicture FROM User u WHERE u.id = :userId")
    fun getUserBasicInfoById(userId: Long): Array<String>
}
