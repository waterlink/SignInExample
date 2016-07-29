class UserIsVerified : UserVerificationStatus {
    override fun getSignInOutcome(): SignInOutcome {
        return SuccessSignInOutcome()
    }
}