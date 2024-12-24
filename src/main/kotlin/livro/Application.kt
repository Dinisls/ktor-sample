package livro

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.serialization.kotlinx.json.*
import org.slf4j.event.Level
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.http.HttpStatusCode

fun main() {
    embeddedServer(Netty, port = 8081) {
        module() // Certifique-se de que a função module() seja chamada
    }.start(wait = true)
}

fun Application.module() {
    // Configurações de logging
    install(CallLogging) {
        level = Level.INFO // Define o nível do log
    }

    // Configuração de negociação de conteúdo
    install(ContentNegotiation) {
        json() // Usa JSON para serialização/deserialização
    }

    // Configuração de tratamento de erros com StatusPages
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(
                text = "An error occurred: ${cause.localizedMessage}",
                status = HttpStatusCode.InternalServerError
            )
        }
    }

    // Configuração das rotas
    routing {
        // Chama a função de configuração das rotas do Templating.kt
        configureTemplating() // Certifique-se de importar corretamente essa função
    }
}
