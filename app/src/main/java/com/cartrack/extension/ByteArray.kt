package com.cartrack.extension

/**
 * Convert byte array to hexString
 * example: byte[]{0x30,0x34,0xAA}.toHexString -> "3034AA"
 */
fun ByteArray.toHexString() = joinToString("") { String.format("%02x", it) }