class UserIsNotVerified : UserVerificationStatus {
    override fun getSignInOutcome(): SignInOutcome {
        return UnverifiedUserFailureSignInOutcome()
    }
}