package seid.fakeapi.data.repo.auth

import seid.fakeapi.data.remote.ApiService
import seid.fakeapi.model.auth.LoginRequest
import seid.fakeapi.model.auth.LoginResponse
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: ApiService
) : AuthRepository {
    override suspend fun login(request: LoginRequest): LoginResponse = api.login(request)
}
