package com.kamran.dictionaryapp.domain.repository

import com.kamran.dictionaryapp.domain.models.WordItem
import com.kamran.dictionaryapp.utils.Result
import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {

    suspend fun getWordResult(word : String) : Flow<Result<WordItem>>

}