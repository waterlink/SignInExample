class StockVerificationCodeGenerator : VerificationCodeGenerator {
    override fun generateCode(): VerificationCode {
        return VerificationCode("THE CODE")
    }

}