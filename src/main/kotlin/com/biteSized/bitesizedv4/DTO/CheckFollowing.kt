package com.biteSized.bitesizedv4.DTO

data class CheckFollowing(
        val followingUserName : String,
        val followingProfilePicture : String,
        val followingId : Long,
        val main_user : Long
)
