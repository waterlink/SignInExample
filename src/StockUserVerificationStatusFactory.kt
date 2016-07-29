class StockUserVerificationStatusFactory : UserVerificationStatusFactory {
    override fun create(verified: Boolean): UserVerificationStatus {
        if (verified) return UserIsVerified()
        return UserIsNotVerified()
    }
}