package com.biteSized.bitesizedv4.model

import jakarta.persistence.*

@Entity
@Table(name = "User")
data class user(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "username")
    val username: String,

    @Column(name = "password")
    val password: String,

    @Column(name = "profilePicture")
    val profilePicture: String,
)
