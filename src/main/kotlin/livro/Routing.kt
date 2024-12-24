package com.example

import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.http.content.*
import kotlinx.serialization.Serializable
import org.slf4j.event.Level

// Classe de dados para representar um livro
@Serializable
data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val genre: String
)

// Dados temporários (em memória)
val books = mutableListOf(
    Book(1, "Kotlin in Action", "Dmitry Jemerov", "Programming"),
    Book(2, "Clean Code", "Robert C. Martin", "Programming")
)

fun Application.module() {
    // Instalação de Plugins
    install(ContentNegotiation) {
        json()
    }
    install(CallLogging) {
        level = Level.INFO
    }
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${cause.localizedMessage}")
        }
    }

    // Rotas
    routing {
        // Servir arquivos estáticos
        static("/static") {
            resources("static") // Diretório dentro de 'resources'
        }

        // Rota principal
        get("/") {
            call.respondText(
                """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Bookstore</title>
                    <link rel="stylesheet" href="/static/styles.css">
                </head>
                <body>
                    <h1>Welcome to the Bookstore!</h1>
                    <p>Explore our collection of books below.</p>
                </body>
                </html>
                """.trimIndent(),
                ContentType.Text.Html
            )
        }

        // Rota para listar livros
        get("/books") {
            call.respond(books)
        }

        // Rota para buscar um livro pelo ID
        get("/books/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid book ID.")
                return@get
            }

            val book = books.find { it.id == id }
            if (book == null) {
                call.respond(HttpStatusCode.NotFound, "Book not found.")
            } else {
                call.respond(book)
            }
        }

        // Rota para adicionar um novo livro
        post("/books") {
            val book = call.receive<Book>()
            books.add(book)
            call.respond(HttpStatusCode.Created, "Book '${book.title}' added successfully!")
        }

        // Rota para deletar um livro
        delete("/books/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid book ID.")
                return@delete
            }

            val removed = books.removeIf { it.id == id }
            if (removed) {
                call.respondText("Book with ID $id deleted successfully.")
            } else {
                call.respond(HttpStatusCode.NotFound, "Book not found.")
            }
        }
    }
}
