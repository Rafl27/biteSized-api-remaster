package com.biteSized.bitesizedv4.model

import jakarta.persistence.*
import java.util.*

@Entity
    @Table(name = "story")
    data class Story(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        @Column(name = "title")
        var title: String,

        @Column(name = "content", length = 5000)
        var content: String,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        var user: User? = null,

        @OneToMany(mappedBy = "story", cascade = [CascadeType.ALL], orphanRemoval = true)
        var comments: MutableList<Comment> = mutableListOf(),

        @Column(name = "date")
        var date: Date?,

        @Column(name = "upvotes")
        var upvotes: Int? = 0,

        @Column(name = "downvotes")
        var downvotes: Int? = 0,

        @Column(name = "art", length = 5000)
        val art: String = ""

    )
