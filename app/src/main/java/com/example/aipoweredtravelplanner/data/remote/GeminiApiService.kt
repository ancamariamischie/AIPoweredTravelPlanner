package com.example.aipoweredtravelplanner.data.remote

import com.example.aipoweredtravelplanner.data.model.request.GeminiRequest
import com.example.aipoweredtravelplanner.data.model.response.GeminiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Interface for the Gemini API.
 */
interface GeminiApiService {

    /**
     * Sends a request to the Gemini API to generate content.
     *
     * @param apiKey The API key for authentication.
     * @param request The input data for the content generation.
     * @return The API's response, including the generated content or error details.
     */
    @POST("v1beta/models/gemini-2.0-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): Response<GeminiResponse>
}


