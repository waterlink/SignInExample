class SignUpService(private val emailService: EmailService, private val verificationCodeGenerator: VerificationCodeGenerator) {
    fun signUp(signUpModel: SignUpModel): SignUpOutcome {
        val code = verificationCodeGenerator.generateCode()
        emailService.send(VerificationEmail(code, signUpModel.email))
        return UserVerificationIsRequired()
    }
}