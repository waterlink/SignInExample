class SignUpService(private val emailService: EmailService) {
    fun signUp(signUpModel: SignUpModel): SignUpOutcome {
        val code = VerificationCode("THE CODE")
        emailService.send(VerificationEmail(code))
        return UserVerificationIsRequired()
    }
}