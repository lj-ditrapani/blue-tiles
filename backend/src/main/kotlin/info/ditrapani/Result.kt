package info.ditrapani

sealed class Result {
    abstract fun flatMap(block: () -> Result): Result
}
object Success : Result() {
    override fun toString(): String = "Success"
    override fun flatMap(block: () -> Result): Result = block()
}
object Failure : Result() {
    override fun toString(): String = "Failure"
    override fun flatMap(block: () -> Result): Result = Failure
}
