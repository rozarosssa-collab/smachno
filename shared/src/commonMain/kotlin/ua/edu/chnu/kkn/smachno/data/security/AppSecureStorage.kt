package ua.edu.chnu.kkn.smachno.data.security

import eu.anifantakis.lib.ksafe.KSafe

/**
 * KSafe сам шифрує те, що в нього кладуть, і зберігає ключ шифрування
 * у сховищі платформи (Keystore на Android, Keychain на iOS).
 */
internal class AppSecureStorage(
    private val ksafe: KSafe,
) : SecureStorage {

    override suspend fun getAccessToken(): String = ksafe.get(ACCESS_TOKEN_KEY, "")

    override suspend fun setAccessToken(token: String) = ksafe.put(ACCESS_TOKEN_KEY, token)

    override suspend fun getRefreshToken(): String = ksafe.get(REFRESH_TOKEN_KEY, "")

    override suspend fun setRefreshToken(token: String) = ksafe.put(REFRESH_TOKEN_KEY, token)

    override suspend fun getUsername(): String = ksafe.get(USERNAME_KEY, "")

    override suspend fun setUsername(username: String) = ksafe.put(USERNAME_KEY, username)

    override suspend fun clear() {
        ksafe.put(ACCESS_TOKEN_KEY, "")
        ksafe.put(REFRESH_TOKEN_KEY, "")
        ksafe.put(USERNAME_KEY, "")
    }
}
