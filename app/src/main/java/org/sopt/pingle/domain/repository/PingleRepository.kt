package org.sopt.pingle.domain.repository

import kotlinx.coroutines.flow.Flow
import org.sopt.pingle.domain.model.MyPingleEntity

interface PingleRepository {
    suspend fun postPingleJoin(meetingId: Long): Flow<Unit?>
    suspend fun deletePingleCancel(meetingId: Long): Flow<Unit?>
    suspend fun getPingleList(
        teamId: Int,
        participation: Boolean
    ): Flow<List<MyPingleEntity>>

    suspend fun deletePingle(meetingId: Long): Flow<Unit?>
}
