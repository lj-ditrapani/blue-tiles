package info.ditrapani

class Server {
    fun hi(): String = "hello"
}

fun main(args: Array<String>) {
    println(Server().hi())
}
