package com.biteSized.bitesizedv4.model

import jakarta.persistence.*

@Entity
data class Followers(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

        @Column(name = "follower")
        val follower : Long,

        @Column(name = "main_user")
        val mainUser : Long
)
