package com.biteSized.bitesizedv4.service

import com.biteSized.bitesizedv4.DTO.Bio
import com.biteSized.bitesizedv4.DTO.LoginRequest
import com.biteSized.bitesizedv4.DTO.UserBasicInfo
import com.biteSized.bitesizedv4.DTO.UserBio
import com.biteSized.bitesizedv4.model.User
import org.springframework.http.ResponseEntity

interface UserService {
    fun createUser(newUser : User) : ResponseEntity<User>
    fun login(loginRequest : LoginRequest) : ResponseEntity<String>
    fun userInfo(authorizationHeader: String?, userId: Int?) : ResponseEntity<Any>
    fun userInfoBasedOnStoryId(storyId: Int) : ResponseEntity<UserBasicInfo>
    fun userInfoBasedOnCommentId(userId: Int) : ResponseEntity<UserBasicInfo>
    fun createBio(userId : Int, bio: Bio, authorizationHeader : String) : ResponseEntity<UserBio>
    fun getBio(userId: Int) : ResponseEntity<UserBio>
}