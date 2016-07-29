import org.junit.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.CoreMatchers.*

class SignUpExperience {
    private lateinit var signUpModel: SignUpModel
    private val emailService = EmailServiceFake()
    private val validUserEmail = Email("john@example.org")
    private val validPassword = Password("welcome")
    private var userVerificationService = UserVerificationServiceFake(StockUserVerificationStatusFactory())
    private lateinit var userVerificationOutcome: UserVerificationOutcome
    private val verificationCodeGenerator = StockVerificationCodeGenerator()
    private val signUpService = SignUpService(emailService, verificationCodeGenerator)
    private val signInService: SignInService = SignInService(userVerificationService)
    private lateinit var signUpOutcome: SignUpOutcome
    private lateinit var signInOutcome: SignInOutcome

    @Test
    fun userSignsUp() {
        givenUserHasProvidedValidEmailAndPassword()

        whenUserInitiatesSignUpProcess()
        thenUserReceivesVerificationEmail()
        thenUserIsInformedAboutTheVerificationRequirement()

        whenUserSignsIn()
        thenSignInResultsInUnverifiedUserFailure()

        whenUserInitiatesSignUpVerificationWithCodeFromEmail()
        thenUserIsInformedAboutSuccessfulVerification()

        whenUserSignsIn()
        thenSignInIsSuccessful()
    }

    private fun givenUserHasProvidedValidEmailAndPassword() {
        signUpModel = SignUpModel(validUserEmail, validPassword)
    }

    private fun whenUserInitiatesSignUpProcess() {
        signUpOutcome = signUpService.signUp(signUpModel)
    }

    private fun thenUserReceivesVerificationEmail() {
        assertThat(emailService.lastEmailReceived, notNullValue())
    }

    private fun whenUserSignsIn() {
        signInOutcome = signInService.signIn(SignInModel(validUserEmail, validPassword))
    }

    private fun thenSignInResultsInUnverifiedUserFailure() {
        assertThat(signInOutcome is UnverifiedUserFailureSignInOutcome, equalTo(true))
    }

    private fun whenUserInitiatesSignUpVerificationWithCodeFromEmail() {
        val verificationEmail = emailService.lastEmailReceived
        val code = verificationEmail!!.code
        userVerificationOutcome = userVerificationService.verify(code)
    }

    private fun thenSignInIsSuccessful() {
        assertThat(signInOutcome is SuccessSignInOutcome, equalTo(true))
    }

    private fun thenUserIsInformedAboutTheVerificationRequirement() {
        assertThat(signUpOutcome is UserVerificationIsRequired, equalTo(true))
    }

    private fun thenUserIsInformedAboutSuccessfulVerification() {
        assertThat(userVerificationOutcome is SuccessUserVerificationOutcome, equalTo(true))
    }
}
