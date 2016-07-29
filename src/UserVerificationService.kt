interface UserVerificationService {
    fun verificationStatusOf(signInModel: SignInModel): UserVerificationStatus

    fun verify(code: VerificationCode): UserVerificationOutcome
}