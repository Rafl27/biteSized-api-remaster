package com.biteSized.bitesizedv4.model

import jakarta.persistence.*

@Entity
@Table(name = "story")
data class story(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "title")
    val title: String,

    @Column(name = "content")
    val content: String,
)
