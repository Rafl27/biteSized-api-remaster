package com.biteSized.bitesizedv4.repository

import com.biteSized.bitesizedv4.DTO.Bio
import com.biteSized.bitesizedv4.DTO.UserBasicInfo
import com.biteSized.bitesizedv4.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsernameAndPassword(username: String, password: String): User
    fun findByUsername(username: String): User
    fun findByEmail(email: String) : User?
    fun existsByEmail(email: String) : Boolean
    @Query("SELECT u.username, u.email, u.profilePicture, u.id FROM User u WHERE u.id = :userId")
    fun getUserBasicInfoById(userId: Long): Array<String>
    @Query("SELECT username, email, profile_picture, user.id from story\n" +
            "join user on story.user_id = user.id\n" +
            "where story.id = :storyId", nativeQuery = true)
    fun getUserBasicInfoByStoryId(storyId: Int) : Array<String>
    @Query("SELECT username, email, profile_picture from comment\n" +
            "join user on comment.user_id = user.id\n" +
            "where user.id = :commentId", nativeQuery = true)
    fun getUserBasicInfoByCommentId(commentId: Int) : Array<String>
    @Modifying
    @Query("UPDATE user SET bio = :bio WHERE id = :userId", nativeQuery = true)
    fun postUserBio(userId : Int, bio : String) : Int
    @Query("SELECT bio FROM user WHERE id = :userId", nativeQuery = true)
    fun getUserBio(userId: Int) : String
}
