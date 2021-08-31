package com.cartrack.util

import android.util.Base64
import android.util.Log
import com.cartrack.extension.toHexString
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object PasswordEncryption {
    private const val key = "cartrack"
    fun encryptBody(password: String): String {
        val digest = MessageDigest.getInstance("MD5")

        val keySha = digest.digest(key.toByteArray()).toHexString()

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(
            Cipher.ENCRYPT_MODE,
            SecretKeySpec(keySha.toByteArray(), "AES"),
            IvParameterSpec(ByteArray(16))
        )
        val encryptedBody = cipher.doFinal(password.toByteArray())

        return Base64.encodeToString(encryptedBody, Base64.NO_WRAP)
    }

    fun decryptBody(encryptedString: String): String {
        val digest = MessageDigest.getInstance("MD5")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val keySha = digest.digest(key.toByteArray()).toHexString()
        cipher.init(
            Cipher.DECRYPT_MODE,
            SecretKeySpec(keySha.toByteArray(), "AES"),
            IvParameterSpec(ByteArray(16))
        )
        val result = cipher.doFinal(Base64.decode(encryptedString, Base64.NO_WRAP))
        return String(result)
    }

}