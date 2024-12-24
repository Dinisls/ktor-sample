// Data.kt
package livro

// Classe de dados para representar um livro
import kotlinx.serialization.Serializable

@Serializable
data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val genre: String
)

// Dados temporários (em memória) - lista de livros
val books = mutableListOf(
    Book(1, "Kotlin in Action", "Dmitry Jemerov", "Programming"),
    Book(2, "Clean Code", "Robert C. Martin", "Programming"),
    Book(3, "The Pragmatic Programmer", "Andrew Hunt", "Programming"),
    Book(4, "Design Patterns", "Erich Gamma", "Software Engineering")
)
