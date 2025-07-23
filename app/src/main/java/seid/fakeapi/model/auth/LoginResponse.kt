package seid.fakeapi.model.auth

import java.io.Serializable

data class LoginResponse(
    val token: String
): Serializable