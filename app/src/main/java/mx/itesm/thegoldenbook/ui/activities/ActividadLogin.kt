package mx.itesm.thegoldenbook.ui.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.application.Settings
import mx.itesm.thegoldenbook.models.Owner
import mx.itesm.thegoldenbook.repositories.FirebaseRepository
import mx.itesm.thegoldenbook.utils.Utils
import java.io.ByteArrayOutputStream

class ActividadLogin : AppCompatActivity() {
    private val permissions: ArrayList<String> = ArrayList()
    private val auth = FirebaseAuth.getInstance()

    private lateinit var callbackManager: CallbackManager
    private lateinit var loginButton: LoginButton
    private lateinit var btnLoginEmail: Button
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton = findViewById(R.id.loginButton)
        btnLoginEmail = findViewById(R.id.btnLoginEmail)
        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)

        // Facebook Permissions
        permissions.add("email")
        permissions.add("public_profile")

        callbackManager = CallbackManager.Factory.create()

        loginButton.setReadPermissions(permissions)
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                if (loginResult != null) {
                    loginFacebookSuccess(loginResult)
                }
            }

            override fun onError(ex: FacebookException) {
                Utils.print("facebook:onError: $ex")
            }

            override fun onCancel() {
                Utils.print("facebook:onCancel")
            }
        })

        btnLoginEmail.setOnClickListener {
            loginEmail()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun loginFacebookSuccess(loginResult: LoginResult) {
        val accessToken = loginResult.accessToken
        val token = accessToken.token
        val credential = FacebookAuthProvider.getCredential(token)

        auth.signInWithCredential(credential).addOnCompleteListener(object :
            OnCompleteListener<AuthResult?> {
            override fun onComplete(task: Task<AuthResult?>) {
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Utils.print("Login Success")

                    val currentUser = auth.currentUser
                    updateUI(currentUser)
                } else {
                    // If sign in fails, display a message to the user.
                    if (task.exception == null) {
                        return
                    }

                    Utils.print("Login Exception: " + task.exception.toString())
                    // TODO setUserLogged(false)
                }
            }
        })
    }

    private fun updateUI(firebaseUser: FirebaseUser?) {
        if(firebaseUser != null) {
            val currentUser = Settings.getCurrentUser()
            if(currentUser != null) {
                Utils.print("Login correcto")
            } else {
                val firebaseId = firebaseUser.uid
                val userName = if (firebaseUser.displayName == null) "" else firebaseUser.displayName!!
                val email = if (firebaseUser.email == null) "" else firebaseUser.email!!
                val photoUrl = if (firebaseUser.photoUrl == null) "" else firebaseUser.photoUrl.toString() + "?type=large"

                val owner = Owner(firebaseId, userName, email, photoUrl, 0)
                Settings.setCurrentUser(owner)
                FirebaseRepository.instance.insert(owner)

                val bitmap = BitmapFactory.decodeResource(
                    resources,
                    R.drawable.avatar
                )

                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val data = byteArrayOutputStream.toByteArray()
                FirebaseRepository.instance.uploadDefault(owner.uid, data)

                Settings.setLogged(true)
            }

            val intent = Intent(this, ActividadMenu2::class.java)
            startActivity(intent)
            finish()
        } else {
            Settings.setLogged(false)
        }
    }

    private fun loginEmail() {
        val email = edtEmail.text.toString()
        val password = edtPassword.text.toString()

        if(email.isEmpty()) {
            Toast.makeText(this, "El campo correo electrónico no debe estar vacío", Toast.LENGTH_SHORT).show()
            return
        }

        if(password.isEmpty()) {
            Toast.makeText(this, "El campo contraseña no debe estar vacío", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val currentUser = auth.currentUser
                updateUI(currentUser)
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos.", Toast.LENGTH_SHORT).show()
                updateUI(null)
            }
        }
    }
}