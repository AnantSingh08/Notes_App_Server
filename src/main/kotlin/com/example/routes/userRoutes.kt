package com.example.routes

import com.example.authentication.JwtService
import com.example.data.models.*
import com.example.repository.Repo
import convertToErrorsModel
import io.konform.validation.Invalid
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoutes(
    db: Repo,
    jwtService: JwtService,
    hashFunction: (String)->String
){
    route("/v1/users/register", HttpMethod.Post){
        handle {
            val registerRequest= call.receive<RegisterRequest>()
//            logger

            if(registerRequest!=null){
                val validationResult = validateRegisterRequest.validate(registerRequest)

                if(validationResult is Invalid){
                    val response = convertToErrorsModel(validationResult)
                    call.respond(HttpStatusCode.BadRequest,response)
                }
            }
            try{
                val user = User(registerRequest.email, hashFunction(registerRequest.password), registerRequest.name)
                db.addUser(user)
                call.respond(HttpStatusCode.OK, SimpleResponse(true,jwtService.generateToken(user)))
            }catch (e: Exception){
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false,e.message ?: "Some Problem Occurred"))
            }
        }
    }

    route("/v1/users/login", HttpMethod.Post){
        handle {
            val loginRequest= call.receive<LoginRequest>()
//            logger
            try{
                if(loginRequest!=null){
                    val validationResult = validateLoginRequest.validate(loginRequest)

                    if(validationResult is Invalid){
                        val response = convertToErrorsModel(validationResult)
                        call.respond(HttpStatusCode.BadRequest,response)
                    }
                }
                val user = db.findUserByEmail(loginRequest.email)

                if(user == null){
                    call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Wrong Email Id Entered user doesn't exist"))
                }else{
                    if(user.hashPassword == hashFunction(loginRequest.password)){
                        call.respond(HttpStatusCode.OK,SimpleResponse(true, jwtService.generateToken(user)))
                    }else{
                        call.respond(HttpStatusCode.BadRequest,SimpleResponse(false, "Password Incorrect"))
                    }
                }
            }catch(e:Exception){
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false,e.message ?: "Some Problem Occurred"))

            }
        }
    }

}