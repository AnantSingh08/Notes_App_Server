package com.example.data.models

import RegisterValidationService
import com.example.utils.Constants
import io.konform.validation.Validation
import io.konform.validation.jsonschema.maxLength
import io.konform.validation.jsonschema.minLength
import io.konform.validation.jsonschema.pattern

data class RegisterRequest(
    val email:String ,
    val name: String,
    val password: String
)

val validateRegisterRequest = Validation<RegisterRequest>{
    RegisterRequest::name required {}
    RegisterRequest::password required {
        minLength(4) hint "Password must be between 4 to 8 characters"
        maxLength(8) hint "Password must be between 4 to 8 characters"
    }
    RegisterRequest::email required {
        pattern(Constants.EMAIL_VALIDAION_PATTERN) hint Constants.VALID_EMAIL_ERROR_MESSAGE
    }

    run(RegisterValidationService)
}
