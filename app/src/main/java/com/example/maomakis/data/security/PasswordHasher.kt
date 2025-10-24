package com.example.maomakis.data.security

import java.security.MessageDigest

object PasswordHasher {

    /**
     * Convierte una contraseña en texto plano a un hash SHA-256.
     * @param password La contraseña a hashear.
     * @return El hash de la contraseña como un String hexadecimal.
     */
    fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(password.toByteArray(Charsets.UTF_8))
        return hashBytes.fold("") { str, it -> str + "%02x".format(it) }
    }
}
