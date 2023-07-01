package com.biteSized.bitesizedv4.repository

import com.biteSized.bitesizedv4.model.Story
import org.springframework.data.jpa.repository.JpaRepository

interface StoryRepository :JpaRepository<Story, Long> {
}