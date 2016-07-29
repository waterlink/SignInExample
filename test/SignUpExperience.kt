import org.junit.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.CoreMatchers.*

class SignUpExperience {
    private lateinit var signUpModel: SignUpModel
    private val emailService = EmailService()
    private val validUserEmail = Email("john@example.org")
    private val validPassword = Password("welcome")
    private var userVerificationService = UserVerificationService()
    private val signInService: SignInService = SignInService(userVerificationService)
    private lateinit var signInOutcome: SignInOutcome

    @Test
    fun userSignsUp() {
        givenUserHasProvidedValidEmailAndPassword()

        whenUserInitiatesSignUpProcess()
        thenUserReceivesConfirmationEmail()
        // thenUserIsInformedAboutTheConfirmationRequirement()

        whenUserSignsIn()
        thenSignInResultsInUnverifiedUserFailure()

        whenUserInitiatesSignUpVerificationWithCodeFromEmail()
        whenUserSignsIn()
        thenSignInIsSuccessful()
    }

    private fun givenUserHasProvidedValidEmailAndPassword() {
        signUpModel = SignUpModel(validUserEmail, validPassword)
    }

    private fun whenUserInitiatesSignUpProcess() {
        SignUpService(emailService).signUp(signUpModel)
    }

    private fun thenUserReceivesConfirmationEmail() {
        assertThat(emailService.lastEmailReceived, notNullValue())
    }

    private fun whenUserSignsIn() {
        signInOutcome = signInService.signIn(SignInModel(validUserEmail, validPassword))
    }

    private fun thenSignInResultsInUnverifiedUserFailure() {
        assertThat(signInOutcome is UnverifiedUserFailureSignInOutcome, equalTo(true))
    }

    private fun whenUserInitiatesSignUpVerificationWithCodeFromEmail() {
        val confirmationEmail = emailService.lastEmailReceived
        val code = confirmationEmail!!.code
        userVerificationService.verify(code)
    }

    private fun thenSignInIsSuccessful() {
        assertThat(signInOutcome is SuccessSignInOutcome, equalTo(true))
    }
}

class UserVerificationService {
    private var verified = false

    fun verify(code: ConfirmationCode) {
        verified = true
    }

    fun userIsVerified(signInModel: SignInModel): Boolean {
        return verified
    }

    fun verificationStatusOf(signInModel: SignInModel): UserVerificationStatus {
        if (verified) return UserIsVerified()
        return UserIsNotVerified()
    }

}

class UserIsVerified : UserVerificationStatus {
    override fun getSignInOutcome(): SignInOutcome {
        return SuccessSignInOutcome()
    }

}

class UserIsNotVerified : UserVerificationStatus {
    override fun getSignInOutcome(): SignInOutcome {
        return UnverifiedUserFailureSignInOutcome()
    }
}

interface UserVerificationStatus {
    fun getSignInOutcome(): SignInOutcome
}

interface SignInOutcome {

}

class SignInModel(email: Email, password: Password) {
}

class SignInService(private val userVerificationService: UserVerificationService) {

    fun signIn(signInModel: SignInModel): SignInOutcome {
        return userVerificationService.verificationStatusOf(signInModel).getSignInOutcome()
    }
}

class UnverifiedUserFailureSignInOutcome : SignInOutcome {

}

class SuccessSignInOutcome : SignInOutcome {

}

class EmailService {
    var lastEmailReceived: ConfirmationEmail? = null

    fun send(confirmationEmail: ConfirmationEmail) {
        lastEmailReceived = confirmationEmail
    }
}

class ConfirmationEmail(val code: ConfirmationCode) {
}

class SignUpService(private val emailService: EmailService) {
    fun signUp(signUpModel: SignUpModel) {
        val code = ConfirmationCode("THE CODE")
        emailService.send(ConfirmationEmail(code))
    }
}

class ConfirmationCode(code: String) {

}

class SignUpModel(email: Email, password: Password) {

}

class Password(password: String) {

}

class Email(email: String) {

}
