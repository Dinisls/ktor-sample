import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.http.*
import io.ktor.server.request.*
import kotlinx.html.*

// Definição da classe Book
data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val genre: String
)

// Lista de livros inicial
val books = mutableListOf(
    Book(1, "Kotlin in Action", "Dmitry Jemerov", "Programming"),
    Book(2, "Clean Code", "Robert C. Martin", "Programming")
)

fun main() {
    embeddedServer(Netty, port = 8081) {
        install(ContentNegotiation) {
            json()
        }

        routing {
            // Página inicial com botões
            get("/") {
                call.respondHtml {
                    head {
                        title { +"Bookstore" }
                    }
                    body {
                        h1 { +"Welcome to the Bookstore API!" }
                        p { +"Explore the collection or add new books." }

                        // Botão para listar os livros
                        form(action = "/books-page", method = FormMethod.get) {
                            button { +"View Books" }
                        }

                        // Botão para adicionar um novo livro
                        form(action = "/add-book", method = FormMethod.get) {
                            button { +"Add a New Book" }
                        }
                    }
                }
            }

            // Rota para listar os livros (HTML)
            get("/books-page") {
                call.respondHtml {
                    head {
                        title { +"Book List" }
                    }
                    body {
                        h1 { +"Book List" }
                        ul {
                            books.forEach { book ->
                                li { +"${book.title} by ${book.author} (Genre: ${book.genre})" }
                            }
                        }
                        a(href = "/") { +"Back to Home" }
                    }
                }
            }

            // Rota para adicionar um novo livro (HTML)
            get("/add-book") {
                call.respondHtml {
                    head {
                        title { +"Add a New Book" }
                    }
                    body {
                        h1 { +"Add a New Book" }
                        form(action = "/books", method = FormMethod.post) {
                            p {
                                label { +"ID: " }
                                textInput(name = "id") { required = true }
                            }
                            p {
                                label { +"Title: " }
                                textInput(name = "title") { required = true }
                            }
                            p {
                                label { +"Author: " }
                                textInput(name = "author") { required = true }
                            }
                            p {
                                label { +"Genre: " }
                                textInput(name = "genre") { required = true }
                            }
                            button(type = ButtonType.submit) { +"Add Book" }
                        }
                        a(href = "/") { +"Back to Home" }
                    }
                }
            }

            // Rota para listar os livros em formato JSON
            get("/books") {
                call.respond(HttpStatusCode.OK, books)
            }

            // Rota para adicionar um livro
            post("/books") {
                val parameters = call.receiveParameters()
                val id = parameters["id"]?.toIntOrNull()
                val title = parameters["title"]
                val author = parameters["author"]
                val genre = parameters["genre"]

                if (id == null || title.isNullOrBlank() || author.isNullOrBlank() || genre.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest, "All fields are required!")
                } else {
                    if (books.any { it.id == id }) {
                        call.respond(HttpStatusCode.Conflict, "A book with ID $id already exists!")
                    } else {
                        books.add(Book(id, title, author, genre))
                        call.respondHtml {
                            body {
                                h1 { +"Book Added Successfully!" }
                                a(href = "/") { +"Back to Home" }
                            }
                        }
                    }
                }
            }

            // Rotas genéricas para outros itens
            get("/items") {
                val items = listOf("Item 1", "Item 2", "Item 3")
                call.respond(HttpStatusCode.OK, items)
            }

            get("/items/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid or missing ID")
                } else {
                    call.respond(HttpStatusCode.OK, "Item $id")
                }
            }

            post("/items") {
                val item = call.receiveOrNull<String>()
                if (item.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid item")
                } else {
                    call.respond(HttpStatusCode.Created, "Item '$item' added successfully!")
                }
            }

            delete("/items/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid or missing ID")
                } else {
                    call.respond(HttpStatusCode.OK, "Item $id deleted successfully!")
                }
            }
        }
    }.start(wait = true)
}
