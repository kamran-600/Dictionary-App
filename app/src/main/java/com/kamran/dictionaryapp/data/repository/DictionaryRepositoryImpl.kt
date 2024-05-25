package com.kamran.dictionaryapp.data.repository

import com.kamran.dictionaryapp.data.api.DictionaryApi
import com.kamran.dictionaryapp.data.mapper.toWordItem
import com.kamran.dictionaryapp.domain.models.WordItem
import com.kamran.dictionaryapp.domain.repository.DictionaryRepository
import com.kamran.dictionaryapp.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DictionaryRepositoryImpl @Inject constructor(
    private val dictionaryApi: DictionaryApi
) : DictionaryRepository {
    override suspend fun getWordResult(word: String): Flow<Result<WordItem>> {
        return flow {
            emit(Result.Loading(true))

            val remoteWordResultDto = try {
                dictionaryApi.searchWordResult((word))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error("Can't get result\n".plus(e.message)))
                emit(Result.Loading(false))
                return@flow
            }

            remoteWordResultDto?.let { wordResultDto ->
                wordResultDto[0].let {
                    emit(Result.Success(it?.toWordItem()))
                    emit((Result.Loading(false)))
                    return@flow
                }
            }

            emit(Result.Loading(false))
        }
    }

}