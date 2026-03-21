package com.example.g46_kotlin.features.map.data.mapper

import com.example.g46_kotlin.features.map.data.remote.dto.PropertyDto
import com.example.g46_kotlin.features.map.domain.model.Property
import javax.inject.Inject

class PropertyMapper @Inject constructor() {
    fun toDomain(dto: PropertyDto): Property {
        return Property(
            id = dto.id,
            title = dto.title,
            description = dto.description,
            rating = dto.averageRating ?: 0.0,
            lat = dto.latitude,
            lon = dto.longitude,
            price = "$${dto.monthlyRent}",
            image = dto.imageUrls.firstOrNull() ?: ""
        )
    }
}