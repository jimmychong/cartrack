package com.cartrack.model

import android.content.Context

data class ErrorResponse(
    private val message: Any? = null,
    val result: String? = null
) {
    /**
     * Get message WITHOUT context for display purpose is UNEXPECTED
     * Message can be string res, get display message with context
     */
    fun getDisplayMessage(context: Context?): String {
        return message?.toString().orEmpty()
    }
}
