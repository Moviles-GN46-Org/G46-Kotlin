package com.example.g46_kotlin.features.house.domain.model

data class PagedProperties(
    val properties: List<PropertyDetail>,
    val totalPages: Int,
    val currentPage: Int
)