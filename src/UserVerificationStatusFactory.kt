interface UserVerificationStatusFactory {
    fun create(verified: Boolean): UserVerificationStatus
}