package dev.mr3n.plugins

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import java.io.File

fun Application.configureRouting() {
    routing {
        route("files") {
            authenticate {
                post("{fileName}") {
                    val principal = call.principal<JWTPrincipal>()?:return@post call.respond(HttpStatusCode.Unauthorized)
                    if("file-uploader" !in principal.audience) { return@post }
                    val fileName = call.parameters["fileName"]
                    val multipartData = call.receiveMultipart()
                    multipartData.forEachPart { part ->
                        when(part) {
                            is PartData.FileItem -> {
                                val fileBytes = part.streamProvider().readBytes()
                                File("files/$fileName").writeBytes(fileBytes)
                            }
                            else -> {}
                        }
                    }
                    call.respond(HttpStatusCode.OK)
                }
            }
            get("{fileName}") {
                val fileName = call.parameters["fileName"]
                val file = File("files/${fileName}")
                if(!file.exists()) { return@get call.respond(HttpStatusCode.NotFound) }
                call.respondFile(file)
            }
        }
    }
}
