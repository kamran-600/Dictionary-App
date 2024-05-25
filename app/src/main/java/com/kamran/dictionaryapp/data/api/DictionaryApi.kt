package com.kamran.dictionaryapp.data.api

import com.kamran.dictionaryapp.data.dto.WordResultDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApi {

    @GET("{word}")
    suspend fun searchWordResult(@Path("word") word : String) : WordResultDto?
}