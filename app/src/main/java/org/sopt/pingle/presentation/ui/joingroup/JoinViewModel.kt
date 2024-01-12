package org.sopt.pingle.presentation.ui.joingroup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sopt.pingle.data.datasource.local.PingleLocalDataSource
import org.sopt.pingle.domain.model.JoinGroupInfoEntity
import org.sopt.pingle.domain.model.JoinGroupSearchEntity
import org.sopt.pingle.domain.model.RequestJoinGroupCodeEntity
import org.sopt.pingle.domain.model.ResponseJoinGroupCodeEntity
import org.sopt.pingle.domain.usecase.GetJoinGroupInfoUseCase
import org.sopt.pingle.domain.usecase.GetJoinGroupSearchUseCase
import org.sopt.pingle.domain.usecase.PostJoinGroupCodeUseCase
import org.sopt.pingle.util.view.UiState

@HiltViewModel
class JoinViewModel @Inject constructor(
    private val localStorage: PingleLocalDataSource,
    private val getJoinGroupSearchUseCase: GetJoinGroupSearchUseCase,
    private val getJoinGroupInfoUseCase: GetJoinGroupInfoUseCase,
    private val postJoinGroupCodeUseCase: PostJoinGroupCodeUseCase
) : ViewModel() {
    private val _joinGroupSearchState =
        MutableSharedFlow<UiState<List<JoinGroupSearchEntity>>>()
    val joinGroupSearchState get() = _joinGroupSearchState.asSharedFlow()
    private val _selectedJoinGroup = MutableStateFlow<JoinGroupSearchEntity?>(null)
    val selectedJoinGroup get() = _selectedJoinGroup.asStateFlow()
    private val _joinGroupSearchData = MutableStateFlow<List<JoinGroupSearchEntity>>(emptyList())
    val joinGroupSearchData get() = _joinGroupSearchData

    private val _joinGroupInfoState =
        MutableStateFlow<UiState<JoinGroupInfoEntity>>(UiState.Empty)
    val joinGroupInfoState get() = _joinGroupInfoState.asStateFlow()
    private var _joinGroupCodeState =
        MutableStateFlow<UiState<ResponseJoinGroupCodeEntity>>(UiState.Empty)
    val joinGroupCodeState get() = _joinGroupCodeState
    val joinGroupCodeEditText = MutableLiveData<String>()

    fun joinGroupSearchState(teamName: String) {
        viewModelScope.launch {
            _joinGroupSearchState.emit(UiState.Loading)
            _joinGroupSearchData.value = emptyList()
            _selectedJoinGroup.value = null
            runCatching {
                getJoinGroupSearchUseCase(teamName = teamName).collect { joinGroupSearch ->
                    if (joinGroupSearch.isEmpty()) {
                        _joinGroupSearchState.emit(UiState.Empty)
                    } else {
                        _joinGroupSearchData.value = joinGroupSearch
                        _joinGroupSearchState.emit(UiState.Success(joinGroupSearch))
                    }
                }
            }.onFailure {
                _joinGroupSearchState.emit(UiState.Error(it.message))
            }
        }
    }

    private var oldPosition = DEFAULT_OLD_POSITION
    fun updateJoinGroupSearchList(newPosition: Int) {
        when (oldPosition) {
            DEFAULT_OLD_POSITION -> setIsSelected(newPosition)

            newPosition -> {
                setIsSelected(newPosition)
                oldPosition = DEFAULT_OLD_POSITION
            }

            else -> {
                if (getIsSelected(oldPosition)) setIsSelected(oldPosition)
                setIsSelected(newPosition)
            }
        }
        _selectedJoinGroup.value =
            if (getIsSelected(newPosition)) _joinGroupSearchData.value[newPosition] else null
        oldPosition = newPosition
    }

    private fun setIsSelected(position: Int) {
        _joinGroupSearchData.value[position].isSelected.set(
            !_joinGroupSearchData.value[position].isSelected.get()
        )
    }

    private fun getIsSelected(position: Int) = _joinGroupSearchData.value[position].isSelected.get()

    fun joinGroupInfoState(teamId: Int) {
        _joinGroupInfoState.value = UiState.Loading
        viewModelScope.launch {
            runCatching {
                getJoinGroupInfoUseCase(teamId = teamId).collect { joinGroupInfo ->
                    _joinGroupInfoState.value = UiState.Success(joinGroupInfo)
                }
            }.onFailure {
                _joinGroupInfoState.value = UiState.Error(it.message)
            }
        }
    }

    fun joinGroupCodeState(teamId: Int, code: RequestJoinGroupCodeEntity) {
        _joinGroupCodeState.value = UiState.Loading
        viewModelScope.launch {
            runCatching {
                postJoinGroupCodeUseCase(teamId = teamId, requestJoinGroupCode = code)
                    .collect { joinGroupCode ->
                        _joinGroupCodeState.value = UiState.Success(joinGroupCode)
                        with(localStorage) {
                            groupId = joinGroupCode.id
                            groupName = joinGroupCode.name
                        }
                    }
            }.onFailure {
                _joinGroupCodeState.value = UiState.Error(it.message)
            }
        }
    }

    companion object {
        private const val DEFAULT_OLD_POSITION = -1
    }
}
