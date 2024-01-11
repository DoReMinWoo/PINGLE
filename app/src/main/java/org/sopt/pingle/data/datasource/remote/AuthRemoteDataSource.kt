package org.sopt.pingle.data.datasource.remote

import org.sopt.pingle.data.model.remote.request.RequestAuthDto
import org.sopt.pingle.data.model.remote.response.ResponseAuthDto

interface AuthRemoteDataSource {
    suspend fun login(kakaoAccessToken: String, requestAuthDto: RequestAuthDto): ResponseAuthDto
}
