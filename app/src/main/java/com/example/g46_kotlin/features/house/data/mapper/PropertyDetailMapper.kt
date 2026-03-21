package com.example.g46_kotlin.features.house.data.mapper

import com.example.g46_kotlin.features.house.data.remote.dto.PropertyDetailDto
import com.example.g46_kotlin.features.house.domain.model.PropertyDetail
import com.example.g46_kotlin.features.house.domain.model.PropertyStatus
import com.example.g46_kotlin.features.house.domain.model.PropertyType
import com.example.g46_kotlin.features.map.data.remote.dto.PropertyDto
import javax.inject.Inject

class PropertyDetailMapper @Inject constructor() {

    fun toDomain(dto: PropertyDetailDto): PropertyDetail {
        return PropertyDetail(
            id = dto.id,
            landlordId = dto.landlord.id,
            title = dto.title,
            description = dto.description,
            propertyType = dto.propertyType.toDomainPropertyType(),
            status = dto.status.toDomainPropertyStatus(),

            monthlyRent = dto.monthlyRent.toDouble(),
            depositAmount = dto.depositAmount?.toDouble(),
            includesUtilities = dto.includesUtilities,

            address = dto.address,
            neighborhood = dto.neighborhood,
            city = dto.city,
            latitude = dto.latitude,
            longitude = dto.longitude,

            sizeM2 = dto.sizeM2.toDouble(),
            bedrooms = dto.bedrooms,
            bathrooms = dto.bathrooms,
            furnished = dto.furnished,
            petFriendly = dto.petFriendly,
            hasParking = dto.hasParking,
            hasLaundry = dto.hasLaundry,
            hasWifi = dto.hasWifi,

            imageUrls = dto.imageUrls,

            publishedAt = dto.publishedAt,
            lastActiveAt = dto.publishedAt,
            expirationNotice = null,
            createdAt = dto.createdAt,
            updatedAt = dto.createdAt
        )
    }

    private fun String.toDomainPropertyType(): PropertyType {
        return when (trim().uppercase().replace("-", "_").replace(" ", "_")) {
            "APARTMENT" -> PropertyType.APARTMENT
            "ROOM" -> PropertyType.ROOM
            "STUDIO" -> PropertyType.STUDIO
            "HOUSE" -> PropertyType.HOUSE
            "SHARED_ROOM" -> PropertyType.SHARED_ROOM
            else -> PropertyType.APARTMENT
        }
    }

    private fun String.toDomainPropertyStatus(): PropertyStatus {
        return when (trim().uppercase()) {
            "ACTIVE" -> PropertyStatus.ACTIVE
            "RENTED" -> PropertyStatus.RENTED
            "HIDDEN" -> PropertyStatus.HIDDEN
            "EXPIRED" -> PropertyStatus.EXPIRED
            else -> PropertyStatus.ACTIVE
        }
    }
}
