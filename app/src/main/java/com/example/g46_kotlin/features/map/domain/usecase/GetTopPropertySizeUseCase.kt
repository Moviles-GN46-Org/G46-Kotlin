package com.example.g46_kotlin.features.map.domain.usecase

import com.example.g46_kotlin.features.map.domain.model.PopularSize
import com.example.g46_kotlin.features.map.domain.repository.MapRepository

class GetTopPropertySizeUseCase(
    private val repository: MapRepository
) {
    suspend operator fun invoke (): PopularSize {
        return repository.getTopPropertySize()
    }
}