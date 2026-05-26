package com.example.govix.queue.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.govix.queue.domain.model.BookQueueRequest
import com.example.govix.queue.domain.model.Queue
import com.example.govix.queue.domain.usecase.BookQueueUseCase
import com.example.govix.queue.domain.usecase.GetMyQueuesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MyQueuesState{
    object Idle: MyQueuesState()
    object Loading: MyQueuesState()
    data class Success(val queues: List<Queue>): MyQueuesState()
    data class Error(val message: String?): MyQueuesState()
}

sealed class BookQueueState{
    object Idle: BookQueueState()
    object Loading: BookQueueState()
    data class Success(val queue: Queue): BookQueueState()
    data class Error(val message:  String?): BookQueueState()
}

@HiltViewModel
class QueueViewModel @Inject constructor(
    private val bookQueueUseCase: BookQueueUseCase,
    private val getMyQueuesUseCase: GetMyQueuesUseCase
): ViewModel(){
    private val _myQueuesState = MutableStateFlow<MyQueuesState>(MyQueuesState.Idle)
    val myQueuesState: StateFlow<MyQueuesState> = _myQueuesState.asStateFlow()

    private val _bookState = MutableStateFlow<BookQueueState>(BookQueueState.Idle)
    val bookState: StateFlow<BookQueueState> = _bookState.asStateFlow()

    fun loadMyQueues(){
        viewModelScope.launch {
            _myQueuesState.value = MyQueuesState.Loading
            getMyQueuesUseCase()
                .onSuccess { _myQueuesState.value = MyQueuesState.Success(it) }
                .onFailure { _myQueuesState.value = MyQueuesState.Error(it.message) }
        }
    }

    fun bookQueue(request: BookQueueRequest) {
        viewModelScope.launch {
            _bookState.value = BookQueueState.Loading
            bookQueueUseCase(request)
                .onSuccess { _bookState.value = BookQueueState.Success(it) }
                .onFailure { _bookState.value = BookQueueState.Error(it.message) }
        }
    }
    fun resetBookState(){
        _bookState.value = BookQueueState.Idle
    }
}