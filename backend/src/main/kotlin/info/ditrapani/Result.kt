package info.ditrapani

sealed class Result {
    abstract fun flatMap(block: () -> Result): Result
}
object Success : Result() {
    override fun flatMap(block: () -> Result): Result = block()
}
object Failure : Result() {
    override fun flatMap(block: () -> Result): Result = Failure
}
