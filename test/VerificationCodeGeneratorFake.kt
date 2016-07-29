class VerificationCodeGeneratorFake(var code : String) : VerificationCodeGenerator {
    override fun generateCode(): VerificationCode {
        return VerificationCode(code)
    }

}