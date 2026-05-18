package com.example.g46_kotlin.features.map.data.mapper

import com.example.g46_kotlin.features.map.data.remote.dto.PopularSizeDto
import com.example.g46_kotlin.features.map.data.remote.dto.PropertiesData
import com.example.g46_kotlin.features.map.data.remote.dto.PropertyDto
import com.example.g46_kotlin.features.map.domain.model.NearbyPropertiesResult
import com.example.g46_kotlin.features.map.domain.model.PopularSize
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

    fun toNearbyPropertiesResult(dto: PropertiesData): NearbyPropertiesResult {
        return NearbyPropertiesResult(
            properties = dto.properties.map { toDomain(it) },
            avgRent = dto.avgRent
        )
    }

    fun toPopularSize(popularSize: PopularSizeDto): PopularSize {
        return PopularSize(
            minM2 = popularSize.bucketMinM2,
            maxM2 = popularSize.bucketMaxM2,
            count = popularSize.count,
            avgSizeM2 = popularSize.averageSizeM2
        )
    }
}