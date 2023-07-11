package com.biteSized.bitesizedv4.model

import jakarta.persistence.*

    @Entity
    @Table(name = "story")
    data class Story(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        @Column(name = "title")
        val title: String,

        @Column(name = "content")
        val content: String,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        var user: User? = null
    )
