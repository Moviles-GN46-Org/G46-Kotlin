package com.example.g46_kotlin.features.map.presentation.mapper

import com.example.g46_kotlin.features.map.domain.model.Property
import com.example.g46_kotlin.features.map.presentation.PropertyPinUi
import com.example.g46_kotlin.features.map.presentation.formatCopToThousandsLabel
import com.example.g46_kotlin.features.map.presentation.shortenTitleForMap
import java.security.ProtectionDomain
import javax.inject.Inject

class PropertyPinUiMapper @Inject constructor(){

    fun toUi(apt: Property): PropertyPinUi = PropertyPinUi(
        id = apt.id,
        title = shortenTitleForMap(apt.title, maxWords = 3),
        description = apt.description,
        rating = apt.rating,
        lat = apt.lat,
        lon = apt.lon,
        price = formatCopToThousandsLabel(apt.price),
        imageUrl = apt.image
    )
}