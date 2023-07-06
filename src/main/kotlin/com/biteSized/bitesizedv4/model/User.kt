package com.biteSized.bitesizedv4.model

import jakarta.persistence.*

@Entity
@Table(name = "User")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "username")
    val username: String,

    @Column(name = "password")
    var password: String,

    @Column(name = "profilePicture")
    var profilePicture: String?,
)