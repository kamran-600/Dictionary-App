package com.kamran.dictionaryapp.presentation

import com.kamran.dictionaryapp.domain.models.WordItem

data class MainState(
    val isLoading: Boolean = false,
    val searchWord: String = "",
    val wordItem : WordItem? = null
)