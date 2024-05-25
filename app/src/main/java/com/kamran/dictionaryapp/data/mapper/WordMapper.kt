package com.kamran.dictionaryapp.data.mapper

import com.kamran.dictionaryapp.data.dto.DefinitionDto
import com.kamran.dictionaryapp.data.dto.MeaningDto
import com.kamran.dictionaryapp.data.dto.WordItemDto
import com.kamran.dictionaryapp.domain.models.Definition
import com.kamran.dictionaryapp.domain.models.Meaning
import com.kamran.dictionaryapp.domain.models.WordItem

fun WordItemDto.toWordItem() = WordItem (
    word = word ?: "",
    meanings = meanings?.map {
        it.toMeaning()
    } ?: emptyList(),
    phonetic = phonetic ?: ""

)

fun MeaningDto.toMeaning() = Meaning(
    definitions = definitionDtoToDefinition(definitions?.get(0)),
    partOfSpeech = partOfSpeech ?: ""
)

fun definitionDtoToDefinition(definitionDto : DefinitionDto?) = Definition(
    definition = definitionDto?.definition ?: "",
    example = definitionDto?.example ?: ""
)