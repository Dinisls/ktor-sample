package livro

import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.html.*

fun Route.configureTemplating() {
    // Rota principal
    get("/") {
        call.respondHtml {
            head {
                title { +"Bookstore" }
                style {
                    +"""
                        body {
                            font-family: 'Arial', sans-serif;
                            background-color: #f4f4f4;
                            margin: 0;
                            padding: 0;
                        }

                        h1, h2 {
                            color: #333;
                            text-align: center;
                        }

                        nav {
                            text-align: center;
                            margin-top: 20px;
                        }

                        nav ul {
                            list-style-type: none;
                            padding: 0;
                        }

                        nav ul li {
                            display: inline-block;
                            margin: 10px;
                        }

                        nav ul li a {
                            text-decoration: none;
                            color: #fff;
                            background-color: #4CAF50;
                            padding: 10px 20px;
                            border-radius: 5px;
                            transition: background-color 0.3s;
                        }

                        nav ul li a:hover {
                            background-color: #45a049;
                        }

                        .container {
                            max-width: 900px;
                            margin: 50px auto;
                            padding: 20px;
                            background-color: #fff;
                            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                            border-radius: 8px;
                        }

                        form p {
                            margin: 10px 0;
                        }

                        input[type="text"] {
                            width: 100%;
                            padding: 10px;
                            margin: 5px 0;
                            border-radius: 5px;
                            border: 1px solid #ddd;
                        }

                        button {
                            background-color: #4CAF50;
                            color: white;
                            padding: 10px 20px;
                            border: none;
                            border-radius: 5px;
                            cursor: pointer;
                            font-size: 16px;
                        }

                        button:hover {
                            background-color: #45a049;
                        }

                        .book-list li {
                            background-color: #f9f9f9;
                            padding: 10px;
                            margin: 10px 0;
                            border-radius: 5px;
                            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
                        }

                        .book-list {
                            padding-left: 0;
                            list-style-type: none;
                        }

                        .back-button {
                            display: block;
                            text-align: center;
                            margin-top: 20px;
                            font-size: 18px;
                            text-decoration: none;
                            color: #4CAF50;
                        }

                        .back-button:hover {
                            color: #45a049;
                        }

                        /* Centraliza o texto */
                        .intro-text {
                            text-align: center;
                            font-size: 20px;
                            margin-top: 20px;
                            color: #555;
                        }
                    """
                }
            }
            body {
                div("container") {
                    h1 { +"Welcome to the Bookstore!" }
                    p(classes = "intro-text") { +"Explore our collection or add new books." }

                    nav {
                        ul {
                            li { a(href = "/books-page") { +"View Books" } }
                            li { a(href = "/add-book") { +"Add a New Book" } }
                        }
                    }
                }
            }
        }
    }

    // Rota para a lista de livros
    get("/books-page") {
        call.respondHtml {
            head {
                title { +"Book List" }
            }
            body {
                div("container") {
                    h2 { +"Book List" }
                    ul("book-list") {
                        books.forEach { book ->
                            li { +"${book.title} by ${book.author} (Genre: ${book.genre})" }
                        }
                    }
                    a("/add-book") { +"Add a New Book" }
                    a("/back-to-home") { +"Back to Home" }
                }
            }
        }
    }

    // Rota para adicionar um novo livro
    get("/add-book") {
        call.respondHtml {
            head {
                title { +"Add a New Book" }
            }
            body {
                div("container") {
                    h2 { +"Add a New Book" }
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
                    a("/books-page") { +"Back to Book List" }
                    a("/") { +"Back to Home" }
                }
            }
        }
    }

    // Rota para adicionar um novo livro via POST
    post("/books") {
        val id = call.parameters["id"]?.toIntOrNull() ?: 0
        val title = call.parameters["title"] ?: ""
        val author = call.parameters["author"] ?: ""
        val genre = call.parameters["genre"] ?: ""

        if (id > 0 && title.isNotEmpty() && author.isNotEmpty() && genre.isNotEmpty()) {
            // Adiciona o livro à lista
            books.add(Book(id, title, author, genre))
            call.respondHtml {
                body {
                    h1 { +"Book Added Successfully" }
                    p { +"$title by $author has been added to the bookstore." }
                    a(href = "/") { +"Back to Home" }
                }
            }
        } else {
            call.respondHtml {
                body {
                    h1 { +"Error" }
                    p { +"Please provide valid book information." }
                    a(href = "/add-book") { +"Back to Add Book" }
                }
            }
        }
    }
}
