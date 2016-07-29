class EmailServiceFake: EmailService {
    var lastEmailReceived: VerificationEmail? = null

    override fun send(verificationEmail: VerificationEmail) {
        lastEmailReceived = verificationEmail
    }
}