class SignInService(private val userVerificationService: UserVerificationService) {
    fun signIn(signInModel: SignInModel): SignInOutcome {
        return userVerificationService.verificationStatusOf(signInModel).getSignInOutcome()
    }
}