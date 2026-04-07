package com.example.g46_kotlin.features.roomie.data.mapper

import com.example.g46_kotlin.features.roomie.data.remote.dto.MatchInfoDto
import com.example.g46_kotlin.features.roomie.data.remote.dto.SwipeResponseDto
import com.example.g46_kotlin.features.roomie.domain.model.SwipeResult
import javax.inject.Inject

class SwipeMapper @Inject constructor(){
    fun toDomain(dto: MatchInfoDto): SwipeResult {
        return SwipeResult(
            swiped = dto.swiped,
            matched = dto.matched,
            matchId = dto.match?.id ?: "",
            chatId = dto.match?.chatId ?: ""
        )
    }
}