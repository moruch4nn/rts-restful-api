package dev.mr3n

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import dev.mr3n.plugins.*
import java.io.File

fun main() {
    File("files").mkdirs()
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
            .start(wait = true)
}

fun Application.module() {
    configureSecurity()
    configureHTTP()
    configureSerialization()
    // configureSockets()
    // configureDatabases()
    configureRouting()
}
