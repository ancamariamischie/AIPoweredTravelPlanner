package com.example.aipoweredtravelplanner.data.model

/**
 * Represents the content of a request or response, made up of a list of individual parts.
 *
 * @property parts A list of `Part` objects that make up the content. Can be empty.
 */
data class Content(
    val parts: List<Part>
)

/**
 * Represents a piece of text.
 *
 * @property text The text content.
 */
data class Part(
    val text: String
)