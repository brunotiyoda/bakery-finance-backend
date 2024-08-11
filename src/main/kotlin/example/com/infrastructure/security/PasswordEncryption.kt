package example.com.infrastructure.security

import at.favre.lib.crypto.bcrypt.BCrypt

object PasswordEncryption {
    private const val BCRYPT_COST = 12

    fun hashPassword(password: String): String {
        return BCrypt.withDefaults().hashToString(BCRYPT_COST, password.toCharArray())
    }

    fun verifyPassword(password: String, hashedPassword: String): Boolean {
        return BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified
    }
}