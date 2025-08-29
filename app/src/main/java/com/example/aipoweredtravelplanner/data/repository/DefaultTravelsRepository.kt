package com.example.aipoweredtravelplanner.data.repository

import com.example.aipoweredtravelplanner.BuildConfig
import com.example.aipoweredtravelplanner.data.mapper.response.GeminiResponseMapper
import com.example.aipoweredtravelplanner.data.model.Content
import com.example.aipoweredtravelplanner.data.model.Part
import com.example.aipoweredtravelplanner.data.model.request.GeminiRequest
import com.example.aipoweredtravelplanner.data.remote.GeminiApiService
import com.example.aipoweredtravelplanner.domain.model.TravelItineraryDm
import com.example.aipoweredtravelplanner.domain.repository.TravelsRepository
import javax.inject.Inject

/**
 * Fetches a list of personalized travel itineraries from the Gemini API.
 *
 * @property geminiApi Service to interact with the Gemini API.
 */
class DefaultTravelsRepository @Inject constructor(
    private val geminiApi: GeminiApiService
) : TravelsRepository {
    private val geminiApiKey = BuildConfig.GEMINI_API_KEY
    private var cachedTravelItineraries: List<TravelItineraryDm> = emptyList()

    override suspend fun getCachedTravelItineraries(): List<TravelItineraryDm> {
        return cachedTravelItineraries
    }

    override suspend fun getTravelItineraries(query: String, timeQuery: String): List<TravelItineraryDm> {
        val response = geminiApi.generateContent(
            apiKey = geminiApiKey,
            request = GeminiRequest(
                contents = listOf(
                    Content(
                        parts = listOf(
                            Part(
                                text = "Return a valid JSON array containing exactly 3 travel " +
                                        "itineraries for $timeQuery days in $query with top " +
                                        "attractions of the destination.  \n" +
                                        "Each itinerary object must include:\n" +
                                        "- title (string)\n" +
                                        "- level (string): type of vacation (e.g., cultural, adventure, relaxing)\n" +
                                        "- program (array of strings), where each string is " +
                                        "the structured, but detailed plan for one day\n" +
                                        "Return only the JSON, with no explanations or extra text.\n"
                            )
                        )
                    )
                )
            )
        )
        response.body()?.let {
            val mappedItineraries = GeminiResponseMapper.map(it)
            cachedTravelItineraries = mappedItineraries
            return mappedItineraries
        }
        return emptyList()
    }
}
