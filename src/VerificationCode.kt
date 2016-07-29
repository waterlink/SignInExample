class VerificationCode(private val code: String) {
    override fun equals(other: Any?): Boolean {
        if (other is VerificationCode) {
            return code == other.code
        }

        return false
    }
}