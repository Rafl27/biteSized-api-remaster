package com.biteSized.bitesizedv4.model

import jakarta.persistence.*

    @Entity
    @Table(name = "story")
    data class Story(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        @Column(name = "title")
        var title: String,

        @Column(name = "content")
        var content: String,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        var user: User? = null,

        @OneToMany(mappedBy = "story", cascade = [CascadeType.ALL], orphanRemoval = true)
        var comments: MutableList<Comment> = mutableListOf()
        
    )
