package com.example.aipoweredtravelplanner.data.model.request

import com.example.aipoweredtravelplanner.data.model.Content

/**
 * The request model for the Gemini API.
 *
 * @property contents The list of [Content] to send to the model.
 */
data class GeminiRequest(
    val contents: List<Content>
)


