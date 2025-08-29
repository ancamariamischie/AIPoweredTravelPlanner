package com.example.aipoweredtravelplanner.data.mapper.response

import com.example.aipoweredtravelplanner.data.model.response.GeminiResponse
import com.example.aipoweredtravelplanner.domain.model.TravelItineraryDm
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.UUID

/**
 * Maps a [GeminiResponse] object to a list of [TravelItineraryDm] objects.
 */
object GeminiResponseMapper {
    fun map(from: GeminiResponse): List<TravelItineraryDm> {
        val json =
            extractJsonBlock(from.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text.orEmpty())
        return parseItinerariesFromJson(json).map {
            it.copy(
                id = UUID.randomUUID().toString()
            )
        }
    }

    private fun parseItinerariesFromJson(json: String): List<TravelItineraryDm> {
        val gson = Gson()
        val type = object : TypeToken<List<TravelItineraryDm>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun extractJsonBlock(response: String): String {
        val start = response.indexOf("```json")
        val end = response.indexOf("```", start + 7)
        return if (start != -1 && end != -1) {
            response.substring(start + 7, end).trim()
        } else {
            response
        }
    }
}