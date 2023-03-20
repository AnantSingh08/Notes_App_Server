package com.example.routes

import com.example.data.models.Note
import com.example.data.models.SimpleResponse
import com.example.data.models.User
import com.example.repository.Repo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.NoteRoutes(
    db: Repo
){
    authenticate("jwt") {
        //Create Note
        route("/v1/notes/create", HttpMethod.Post) {
            handle {
                val note = call.receive<Note>()
                //add some validation
                try{
                    val email = call.principal<User>()!!.email
                    db.addNote(note,email)
                    call.respond(HttpStatusCode.OK, SimpleResponse(true,"Note Added Successfully"))

                }catch (e:Exception){
                    call.respond(HttpStatusCode.Conflict, SimpleResponse(false,e.message ?: "Some Problem Occurred"))
                }
            }
        }

        //Get Notes
        route("v1/notes", HttpMethod.Get){
            handle {
                val email = call.principal<User>()!!.email
                val notes = db.getAllNotes(email)
                if(notes!=null){
                    call.respond(HttpStatusCode.OK,notes)
                }else{
                    call.respond(HttpStatusCode.Conflict , emptyList<Note>())
                }

            }
        }

        //Update Note
        route("/v1/notes/update", HttpMethod.Post) {
            handle {
                val note = call.receive<Note>()
                //add some validation
                try{
                    val email = call.principal<User>()!!.email
                    db.updateNote(note,email)
                    call.respond(HttpStatusCode.OK, SimpleResponse(true,"Note Updated Successfully"))

                }catch (e:Exception){
                    call.respond(HttpStatusCode.Conflict, SimpleResponse(false,e.message ?: "Some Problem Occurred"))
                }
            }
        }

        //Delete
        route("/v1/notes/delete", HttpMethod.Delete) {
            handle {
                val noteId = call.request.queryParameters["id"]!!
                //add some validation
                try{
                    val email = call.principal<User>()!!.email
                    db.deleteNote(noteId,email)
                    call.respond(HttpStatusCode.OK, SimpleResponse(true,"Note Deleted Successfully"))

                }catch (e:Exception){
                    call.respond(HttpStatusCode.Conflict, SimpleResponse(false,e.message ?: "Some Problem Occurred"))
                }
            }
        }

    }

}