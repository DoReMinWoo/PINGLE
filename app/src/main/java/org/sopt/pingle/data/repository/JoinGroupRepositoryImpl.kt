package org.sopt.pingle.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.sopt.pingle.data.datasource.remote.JoinGroupRemoteDataSource
import org.sopt.pingle.data.mapper.toRequestJoinGroupCode
import org.sopt.pingle.domain.model.JoinGroupInfoEntity
import org.sopt.pingle.domain.model.RequestJoinGroupCodeEntity
import org.sopt.pingle.domain.model.ResponseJoinGroupCodeEntity
import org.sopt.pingle.domain.repository.JoinGroupRepository
import javax.inject.Inject

class JoinGroupRepositoryImpl @Inject constructor(
    private val joinGroupRemoteDataSource: JoinGroupRemoteDataSource
) : JoinGroupRepository {
    override fun getJoinGroupInfo(teamId: Int): Flow<JoinGroupInfoEntity> = flow {
        val result = runCatching {
            joinGroupRemoteDataSource.getJoinGroupInfo(teamId = teamId).data.toJoinGroupCodeEntity()
        }
        emit(result.getOrThrow())
    }

    override fun postJoinGroupCode(
        teamId: Int,
        code: RequestJoinGroupCodeEntity
    ): Flow<ResponseJoinGroupCodeEntity> = flow {
        val result = runCatching {
            joinGroupRemoteDataSource.postJoinGroupCode(
                teamId = teamId,
                code = code.toRequestJoinGroupCode()
            ).data.toResponseJoinGroupCode()
        }
        emit(result.getOrThrow())
    }
}