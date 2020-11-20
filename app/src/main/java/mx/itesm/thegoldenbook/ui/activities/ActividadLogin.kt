package mx.itesm.thegoldenbook.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
                if(loginResult != null) {
                    loginFacebookSuccess(loginResult)
                }
            }

            override fun onError(ex: FacebookException) {
                Log.d("Jaime", "facebook:onError: $ex")
            }

            override fun onCancel() {
                Log.d("Jaime", "facebook:onCancel")
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

        auth.signInWithCredential(credential).addOnCompleteListener(object : OnCompleteListener<AuthResult?> {
            override fun onComplete(task: Task<AuthResult?>) {
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Jaime","Login Success")

                    val currentUser = auth.currentUser
                    updateUI(currentUser)
                } else {
                    // If sign in fails, display a message to the user.
                    if (task.exception == null) {
                        return
                    }

                    Log.d("Jaime", "Login Exception: " + task.exception.toString())
                    // TODO setUserLogged(false)
                }
            }
        })
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser != null) {
            val firebaseId = currentUser.uid
            val userName = if (currentUser.displayName == null) "" else currentUser.displayName!!
            val email = if (currentUser.email == null) "" else currentUser.email!!
            val photoUrl = if (currentUser.photoUrl == null) "" else currentUser.photoUrl.toString() + "?type=large"

            val owner = Owner(firebaseId, userName, email, photoUrl, 0)
            Settings.setCurrentUser(owner)
            FirebaseRepository.instance.insert(owner)

            Settings.setLogged(true)

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

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val currentUser = auth.currentUser
                updateUI(currentUser)
            } else {
                Toast.makeText(this@ActividadLogin, "Usuario o contraseña incorrectos.", Toast.LENGTH_SHORT).show()
                updateUI(null)
            }
        }
    }
}