package com.example.plugins

import com.example.authentication.JwtService
import com.example.authentication.hash
import com.example.repository.Repo
import com.example.routes.NoteRoutes
import com.example.routes.userRoutes
import io.ktor.server.routing.*
import io.ktor.server.application.*

val db = Repo()
val jwtService = JwtService()
val hashFunction = {s: String -> hash(s) }

fun Application.configureRouting() {
    routing {
        userRoutes(db, jwtService, hashFunction)
        NoteRoutes(db)
    }
}
