package com.kamran.dictionaryapp.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kamran.dictionaryapp.domain.models.WordItem
import com.kamran.dictionaryapp.domain.repository.DictionaryRepository
import com.kamran.dictionaryapp.utils.Constants.TAG
import com.kamran.dictionaryapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dictionaryRepository: DictionaryRepository
) : ViewModel() {

    private val _mainState = MutableStateFlow(MainState())
    val mainState = _mainState.asStateFlow()

    private var searchJob: Job? = null

    init {
        _mainState.update {
            it.copy(searchWord = "word")
        }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            loadWorkResult()
        }
    }

    fun onEvent(mainUiEvent: MainUiEvents) {
        when (mainUiEvent) {
            is MainUiEvents.OnSearchClick -> {
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    loadWorkResult()
                }
            }

            is MainUiEvents.OnSearchWordChange -> {
                _mainState.update {
                    it.copy(
                        searchWord = mainUiEvent.newWord.lowercase()
                    )
                }
            }
        }
    }

    private fun loadWorkResult() {
        viewModelScope.launch {
            dictionaryRepository.getWordResult(
                mainState.value.searchWord
            ).collect { result ->
                when (result) {
                    is Result.Error -> {
                        Log.d(TAG, result.message.toString())
                        _mainState.update {
                            it.copy(wordItem = WordItem(result.message.toString(), emptyList(), ""))
                        }
                    }
                    is Result.Loading -> {
                        _mainState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }

                    is Result.Success -> {
                        result.data?.let { wordItem ->
                            _mainState.update {
                                it.copy(
                                    wordItem = wordItem
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}