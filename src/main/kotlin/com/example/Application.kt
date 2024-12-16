import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.http.*
import io.ktor.server.request.*

// Definição da classe Books
@kotlinx.serialization.Serializable
data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val genre: String
)

// Dados temporários (em memória)
val books = mutableListOf(
    Books(1, "Kotlin in Action", "Dmitry Jemerov", "Programming"),
    Books(2, "Clean Code", "Robert C. Martin", "Programming")
)

fun main() {
    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            json() // Configura o Ktor para usar JSON como formato de resposta
        }

        routing {
            get("/") {
                call.respondText("Welcome to the Bookstore API!")
            }

            // Endpoint para listar livros
            get("/books") {
                call.respond(HttpStatusCode.OK, books) // Retorna a lista de livros
            }

            // Endpoint para adicionar um livro
            post("/books") {
                val book = try {
                    call.receive<Books>() // Recebe o JSON e desserializa para um objeto Books
                } catch (e: Exception) {
                    return@post call.respond(HttpStatusCode.BadRequest, "Invalid book format!")
                }

                // Verifica se o ID já existe
                if (books.any { it.id == book.id }) {
                    call.respond(HttpStatusCode.Conflict, "A book with ID ${book.id} already exists!")
                } else {
                    books.add(book) // Adiciona o livro à lista em memória
                    call.respond(HttpStatusCode.Created, "Book '${book.title}' added successfully!")
                }
            }

            // Endpoint para listar itens genéricos
            get("/items") {
                val items = listOf("Item 1", "Item 2", "Item 3") // Exemplo de lista de itens
                call.respond(HttpStatusCode.OK, items)
            }

            // Rota para obter um item específico
            get("/items/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid or missing ID")
                } else {
                    call.respond(HttpStatusCode.OK, "Item $id")
                }
            }

            // Rota para adicionar um novo item
            post("/items") {
                val item = call.receiveOrNull<String>()
                if (item.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid item")
                } else {
                    call.respond(HttpStatusCode.Created, "Item '$item' added successfully!")
                }
            }

            // Rota para deletar um item
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
