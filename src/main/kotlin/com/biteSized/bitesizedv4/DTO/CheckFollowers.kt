package com.biteSized.bitesizedv4.DTO

data class CheckFollowers(
        val followerUserName : String,
        val followerProfilePicture : String,
        val followerId : Long,
        val main_user : Long
)
