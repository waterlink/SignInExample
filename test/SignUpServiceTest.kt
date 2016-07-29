import org.junit.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.CoreMatchers.*

class SignUpServiceTest {
    private val emailService = EmailServiceFake()
    private var verificationCodeGenerator = VerificationCodeGeneratorFake("")
    private val signUpService = SignUpService(emailService, verificationCodeGenerator)
    private val email = Email("john@example.org")
    private val password = Password("welcome")
    private val signUpModel = SignUpModel(email, password)
    private val otherUsersEmail = Email("sarah@example.org")
    private val otherUsersSignUpModel = SignUpModel(otherUsersEmail, password)

    @Test
    fun signUp_resultsInUserVerificationIsRequired() {
        val signUpOutcome = signUpService.signUp(signUpModel)
        assertThat(signUpOutcome is UserVerificationIsRequired, equalTo(true))
    }

    @Test
    fun signUp_sendsEmailWithVerificationCode() {
        verificationCodeGenerator.code = "THE CODE"
        signUpService.signUp(signUpModel)
        val lastVerificationEmail = emailService.lastEmailReceived
        assertThat(lastVerificationEmail!!.code, equalTo(VerificationCode("THE CODE")))
    }

    @Test
    fun signUp_sendsEmailWithDifferentVerificationCode() {
        verificationCodeGenerator.code = "DIFFERENT CODE"
        signUpService.signUp(signUpModel)
        val lastVerificationEmail = emailService.lastEmailReceived
        assertThat(lastVerificationEmail!!.code, equalTo(VerificationCode("DIFFERENT CODE")))
    }

    @Test
    fun signUp_sendsEmailToUsersEmail() {
        signUpService.signUp(signUpModel)
        val lastVerificationEmail = emailService.lastEmailReceived
        assertThat(lastVerificationEmail!!.email, equalTo(email))
    }

    @Test
    fun signUp_sendsEmailToOtherUsersEmail() {
        signUpService.signUp(otherUsersSignUpModel)
        val lastVerificationEmail = emailService.lastEmailReceived
        assertThat(lastVerificationEmail!!.email, equalTo(otherUsersEmail))
    }
}
