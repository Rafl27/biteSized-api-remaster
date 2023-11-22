package com.biteSized.bitesizedv4.model

import jakarta.persistence.*

@Entity
data class Followers(
        @Id
        @GeneratedValue
        val id: Long? = null,

        @Column(name = "follower")
        val follower : Int,

        @Column(name = "main_user")
        val mainUser : Int
)
