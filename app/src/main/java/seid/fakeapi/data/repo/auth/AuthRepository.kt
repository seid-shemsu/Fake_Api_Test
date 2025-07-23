package seid.fakeapi.data.repo.auth

import seid.fakeapi.model.auth.LoginRequest
import seid.fakeapi.model.auth.LoginResponse

interface AuthRepository {
    suspend fun login(request: LoginRequest): LoginResponse
}
