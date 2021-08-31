package com.cartrack.extension

import com.cartrack.ValidPasswordLength
import java.util.regex.Pattern



fun String.isPhoneValid(): Boolean {
    if (this.isEmpty() || this.length < ValidPasswordLength) {
        return false
    }

    val expressionCharacter = ".*[a-zA-Z].*"
    val expressionNumber = ".*[0-9].*"
    val patternCharacter = Pattern.compile(expressionCharacter, Pattern.CASE_INSENSITIVE)
    val matcherCharacter = patternCharacter.matcher(this)

    val patternNumber = Pattern.compile(expressionNumber, Pattern.CASE_INSENSITIVE)
    val matcherNumber = patternNumber.matcher(this)

    if (!matcherCharacter.matches() || !matcherNumber.matches()) {
        return false
    }

    return true
}