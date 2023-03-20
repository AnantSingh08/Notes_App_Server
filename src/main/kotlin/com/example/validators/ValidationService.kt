import com.example.data.models.LoginRequest
import com.example.data.models.NoteAppError
import com.example.data.models.NoteAppErrors
import com.example.data.models.RegisterRequest
import io.konform.validation.Invalid
import io.konform.validation.Valid
import io.konform.validation.Validation
import io.konform.validation.ValidationResult

object LoginValidationService: Validation<LoginRequest>{
    override fun validate(value: LoginRequest): ValidationResult<LoginRequest> {
        val errors = HashMap<String , List<String>>()

        if(value.email==""){
            errors["email"] = listOf("No email Entered")
        }
        if(value.password==""){
            errors["password"] = listOf("No password Entered")
        }
        return if(errors.keys.isEmpty()){
            Valid(value)
        }else{
            Invalid(errors)
        }
    }
}

object RegisterValidationService: Validation<RegisterRequest>{
    override fun validate(value: RegisterRequest): ValidationResult<RegisterRequest> {
        val errors = HashMap<String , List<String>>()

        if(value.email==""){
            errors["email"] = listOf("No email Entered")
        }

        return if(errors.keys.isEmpty()){
            Valid(value)
        }else{
            Invalid(errors)
        }
    }
}

fun convertToErrorsModel(validation: Invalid<out Any>): NoteAppErrors {
    return NoteAppErrors(listOf(validation).flatMap { it.errors }.map {
        val field = it.dataPath
        NoteAppError("$field: ${it.message}")
    })
}
