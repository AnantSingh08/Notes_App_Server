package com.example.data.models

data class NoteAppErrors(
    var errors: List<NoteAppError>
)

data class NoteAppError(
    val errorMessage: String
)
