package sat

/** Defines a literal as an integer value with a boolean truth value */
data class Literal(private val lit: Int, var truth: Boolean) {

    fun get(): Int {
        return lit
    }

    fun set(_truthValue: Boolean) {
        truth = _truthValue
    }

    override fun equals(other: Any?): Boolean {
        return lit == (other as Literal?)!!.get()
    }

    override fun toString(): String {
        return if (truth) {
            lit.toString()
        } else {
            (lit * -1).toString()
        }
    }

    override fun hashCode(): Int {
        return lit
    }
}