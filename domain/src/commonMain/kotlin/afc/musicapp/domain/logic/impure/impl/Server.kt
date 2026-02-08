package afc.musicapp.domain.logic.impure.impl

import afc.musicapp.domain.entities.dto.File
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.basic
import io.ktor.server.auth.principal
import io.ktor.server.cio.CIO
import io.ktor.server.engine.EngineConnectorBuilder
import io.ktor.server.engine.embeddedServer
import io.ktor.server.request.receive
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

class Server {
    fun startServer() {
        embeddedServer(CIO, configure = {
            connectors.add(EngineConnectorBuilder().apply {
                host = "127.0.0.1"
                port = 8080
            })
            connectionGroupSize = 2
            workerGroupSize = 5
            callGroupSize = 10
            shutdownGracePeriod = 2000
            shutdownTimeout = 3000
        }) {
            install(Authentication.Companion) {
                basic("auth-basic") {
                    realm = "Access to the '/' path"
                    validate { credentials ->
                        if (credentials.name == "admin" && credentials.password == "admin") {
                            UserIdPrincipal(credentials.name)
                        } else {
                            null
                        }
                    }
                }
            }
            install(ContentNegotiation) {
                json()
            }
            routing {
                get("/ping") {
                    call.respondText("pong")
                }
                authenticate("auth-basic") {
                    post("/files") {
                        call.receive<List<File>>()
                        call.respondText("Hello, ${call.principal<UserIdPrincipal>()?.name}!")
                    }
                }


            }
        }.start(wait = true)
    }
}