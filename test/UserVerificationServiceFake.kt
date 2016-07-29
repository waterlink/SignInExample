class UserVerificationServiceFake(private val userVerificationStatusFactory: UserVerificationStatusFactory) : UserVerificationService {
    private var verified = false

    override fun verify(code: VerificationCode): UserVerificationOutcome {
        verified = true
        return SuccessUserVerificationOutcome()
    }

    override fun verificationStatusOf(signInModel: SignInModel): UserVerificationStatus {
        return userVerificationStatusFactory.create(verified)
    }
}