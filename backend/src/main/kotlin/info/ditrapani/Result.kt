package info.ditrapani

sealed class Result<out A> {
    abstract fun <B> flatMap(block: (A) -> Result<B>): Result<B>
    abstract fun <B> map(block: (A) -> B): Result<B>
}
data class Success<A>(val value: A) : Result<A>() {
    override fun <B> flatMap(block: (A) -> Result<B>): Result<B> = block(value)
    override fun <B> map(block: (A) -> B): Result<B> = Success(block(value))
}
object Failure : Result<Nothing>() {
    override fun <B> flatMap(block: (Nothing) -> Result<B>): Result<B> = Failure
    override fun <B> map(block: (Nothing) -> B): Result<B> = Failure
}
