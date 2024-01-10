package org.sopt.pingle.data.datasourceimpl.remote

import org.sopt.pingle.data.datasource.remote.AuthDataSource
import org.sopt.pingle.data.model.remote.request.RequestAuthDto
import org.sopt.pingle.data.model.remote.response.ResponseAuthDto
import org.sopt.pingle.data.service.AuthService
import javax.inject.Inject

class AuthDataSourceImpl @Inject constructor(
    private val authService: AuthService
) : AuthDataSource {
    override suspend fun login(
        kakaoAccessToken: String,
        requestAuthDto: RequestAuthDto
    ): ResponseAuthDto =
        authService.postLogin(kakaoAccessToken, requestAuthDto).data
}
