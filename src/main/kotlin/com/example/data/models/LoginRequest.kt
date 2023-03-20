package com.example.data.models

import LoginValidationService
import io.konform.validation.Validation

data class LoginRequest(
    val email: String,
    val password: String
)

val validateLoginRequest = Validation<LoginRequest> {
    LoginRequest::password required {}
    LoginRequest::email required {}

    run(LoginValidationService)
}
