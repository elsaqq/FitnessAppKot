import com.example.fitnessappkot.Login
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginTest {

    @Mock
    private lateinit var mockAuth: FirebaseAuth

    @Test
    fun loginUser_withValidCredentials() {
        // Mocking FirebaseAuth
        val email = "test@example.com"
        val password = "password123"
        val mockAuthResultTask = Tasks.forResult(mock(AuthResult::class.java))

        `when`(mockAuth.signInWithEmailAndPassword(email, password)).thenReturn(mockAuthResultTask)

        // Create instance of Login
        val login = Login()
        login.auth = mockAuth // Inject mocked FirebaseAuth

        // Method under test
        login.loginUser(email, password)

        // Verify that FirebaseAuth.signInWithEmailAndPassword was called
        verify(mockAuth).signInWithEmailAndPassword(email, password)
    }
}
